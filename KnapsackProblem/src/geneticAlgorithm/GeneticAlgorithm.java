package geneticAlgorithm;

import dataset.Dataset;
import dataset.DatasetGenerator;
import dataset.Utils;

public class GeneticAlgorithm {
    private Dataset dataset;

    public GeneticAlgorithm(Dataset _dataset) {
        dataset = _dataset;
    }

    public Population generateRandomPopulation(Hyperparameters hyperparameters) {
        Chromosome[] chromosomes = new Chromosome[hyperparameters.getPopulationSize()];
        for (int i = 0; i < hyperparameters.getPopulationSize(); i++) {
            boolean[] genes = Utils.generateRandomSolution(dataset);
            chromosomes[i] = new Chromosome(genes, dataset);
        }
        return new Population(chromosomes, hyperparameters);
    }

    public static boolean[] runGeneticAlgorithm(Population currentPopulation, int noOfIterations) {
        for (int i = 0; i < noOfIterations; i++) {
            currentPopulation = currentPopulation.getNextPopulation();
        }
        return currentPopulation.getChromosomes()[0].getGenes();
    }

    public static void main(String[] args) {
        String datasetName = "dataset_50.json";
        Dataset dataset = DatasetGenerator.readDataset(datasetName);
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(dataset);
        Hyperparameters[] hyperparameters = {
                new Hyperparameters(10, 200, 0.7, 0.2, 0.1),
                new Hyperparameters(80, 2000, 0.8, 0.15, 0.05),
                new Hyperparameters(100, 1000, 0.85, 0.05, 0.1)
        };
        for (Hyperparameters hps : hyperparameters) {
            Population currentPopulation = geneticAlgorithm.generateRandomPopulation(hps);
            double t_start = System.nanoTime();
            boolean[] solution = runGeneticAlgorithm(currentPopulation, hps.getNoIterations());
            double t_finish = System.nanoTime();
            Utils.writeGeneticSolution(hps, datasetName, "genetic_algorithm", solution, (t_finish - t_start) / 1000000000.);
        }
    }
}

