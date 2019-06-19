package geneticAlgorithm;

import dataset.Dataset;

public class Hyperparameters {
    private int populationSize, noIterations;
    private double crossoverRate, mutationRate, cloningRate;

    public Hyperparameters(int population_size, int no_iterations, double crossover_rate, double mutation_rate, double cloning_rate) {
        populationSize = population_size;
        noIterations = no_iterations;
        crossoverRate = crossover_rate;
        mutationRate = mutation_rate;
        cloningRate = cloning_rate;
    }

    public double getCloningRate() {
        return cloningRate;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public int getNoIterations() {
        return noIterations;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public int getPopulationSize() {
        return populationSize;
    }
}
