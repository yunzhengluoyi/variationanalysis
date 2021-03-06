package org.campagnelab.dl.framework.architecture.nets;

import org.deeplearning4j.nn.conf.LearningRatePolicy;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * An interface for classes that assemble neural network configuration according to some architecture.
 * Created by fac2003 on 6/10/16.
 *
 * @author Fabien Campagne
 */
public interface NeuralNetAssembler {
    public MultiLayerConfiguration createNetwork();

    public void setWeightInitialization(WeightInit init);

    public void setLearningRate(double learningRate);

    public void setLearningRatePolicy(LearningRatePolicy learningRatePolicy);

    public void setSeed(long seed);

    //  public void setInputs(int numInputs, int numOutputs, int[] numHiddenNodes);

    public void setNumInputs(int numInputs);

    public void setNumOutputs(int numOutputs);

    public void setNumHiddenNodes(int numHiddenNodes);

    public void setRegularizationRate(Double regularizationRate);
    public void setDropoutRate(Double rate);

    void setLossFunction(LossFunctions.LossFunction lossFunction);
}
