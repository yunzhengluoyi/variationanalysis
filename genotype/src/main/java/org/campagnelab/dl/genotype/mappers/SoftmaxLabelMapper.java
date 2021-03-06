package org.campagnelab.dl.genotype.mappers;

import org.campagnelab.dl.framework.mappers.ConfigurableFeatureMapper;
import org.campagnelab.dl.varanalysis.protobuf.BaseInformationRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Label that encodes  the combination of called alleles in a sample using one-hot encoding and a softmax for decoding.<p/>
 * <p>
 * Created by fac2003 on 2/7/17.
 */
public class SoftmaxLabelMapper extends CountSortingLabelMapper implements ConfigurableFeatureMapper {


    private final float epsilon;
    public int maxCalledAlleles;
    static private Logger LOG = LoggerFactory.getLogger(SoftmaxLabelMapper.class);

    /**
     * @param sortCounts
     * @param maxCalledAlleles
     * @param epsilon          amount of label smoothing to apply.
     */
    public SoftmaxLabelMapper(boolean sortCounts, int maxCalledAlleles, float epsilon) {

        super(sortCounts);
        if (!sortCounts) LOG.warn("You should only useSoftmaxLabelMapper with unsorted counts in tests. ");
        this.maxCalledAlleles = maxCalledAlleles;
        this.epsilon = epsilon;
    }

    public SoftmaxLabelMapper(boolean sortCounts, int ploidy) {

        this(sortCounts, ploidy, 0);
    }

    @Override
    public int numberOfLabels() {
        return (int) Math.pow(2, maxCalledAlleles);
    }

    private int cachedValue;

    protected void setCachedValue(boolean... isCalled) {
        cachedValue = 0;
        int index = 0;
        for (boolean called : isCalled) {
            cachedValue |= (called ? 1 : 0) << index;
            index++;
        }
    }

    @Override
    public void prepareToNormalize(BaseInformationRecords.BaseInformation record, int indexOfRecord) {
        super.prepareToNormalize(record, indexOfRecord);
        cachedValue = 0;
        int index = 0;
        for (BaseInformationRecords.CountInfo count : sortedCountRecord.getSamples(0).getCountsList()) {
            cachedValue |= (count.getIsCalled() ? 1 : 0) << index;
            index++;
            if (index > maxCalledAlleles) {
   //             break;
            }
        }
    }

    @Override
    public float produceLabel(BaseInformationRecords.BaseInformation record, int labelIndex) {
        int n = numberOfLabels();
        float v = epsilon / (n - 1f);
        return (cachedValue == labelIndex) ? 1f - epsilon : v;
    }


    public static final String PLOIDY_PROPERTY = "genotypes.ploidy";

    @Override
    public void configure(Properties readerProperties) {

        String value = readerProperties.getProperty(PLOIDY_PROPERTY);
        try {
            maxCalledAlleles = Integer.parseInt(value) + 1;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Unable to read ploidy from sbi properties file.");
        }
    }
}
