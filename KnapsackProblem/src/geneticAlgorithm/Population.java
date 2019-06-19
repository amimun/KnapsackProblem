package geneticAlgorithm;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class Population {
    private Chromosome[] chromosomes;
    private Hyperparameters hyperparameters;
    private int clonedIndividuals, mutatedIndividuals, crossedIndividuals;

    public Population(Chromosome[] _chromosomes, Hyperparameters _hyperparameters) {
        chromosomes = _chromosomes;
        Arrays.sort(chromosomes, Collections.reverseOrder(Comparator.comparingInt(Chromosome::getFitValue)));
        hyperparameters = _hyperparameters;
        clonedIndividuals = (int) (hyperparameters.getCloningRate() * hyperparameters.getPopulationSize());
        mutatedIndividuals = (int) (hyperparameters.getMutationRate() * hyperparameters.getPopulationSize());
        crossedIndividuals = (int) (hyperparameters.getCrossoverRate() * hyperparameters.getPopulationSize());
    }

    private int getTotalValue(Chromosome[] chromosomes) {
        int totalValue = 0;
        for (int i = 0; i < chromosomes.length; i++) {
            totalValue += chromosomes[i].getFitValue();
        }

        return totalValue;
    }

    private Chromosome getRandomChromosome() {
        Random random = new Random();
        return chromosomes[random.nextInt(chromosomes.length)];
    }

    private Chromosome spinRouletteWheel(int totalValue, Chromosome[] chromosomes) {
        Random random = new Random();
        int i, stopAt = random.nextInt(totalValue);
        for (i = 0; i < chromosomes.length; i++) {
            stopAt -= chromosomes[i].getFitValue();
            if (stopAt <= 0) {
                break;
            }
        }
        return chromosomes[i];
    }

    private Chromosome[] getPairOfChromosomes(Chromosome[] chromosomes) {
        Chromosome[] pair = new Chromosome[2];
        Chromosome[] chromosomesCopy = chromosomes.clone();
        int totalValue = getTotalValue(chromosomesCopy), length = chromosomesCopy.length, k = 0;
        pair[0] = spinRouletteWheel(totalValue, chromosomesCopy);
        Chromosome[] newChromosomes = new Chromosome[length-1];
        for (int i = 0; i < length; i++) {
            if (chromosomesCopy[i].equals(pair[0])) {
                continue;
            }
            newChromosomes[k++] = chromosomesCopy[i];
        }
        pair[1] = spinRouletteWheel(totalValue-pair[0].getFitValue(), newChromosomes);
        return pair;
    }

    public Chromosome[] getClonedChromosomes(int noOfClonedChromosomes) {
        return Arrays.copyOf(chromosomes, noOfClonedChromosomes);
    }

    public Chromosome[] getMutatedChromosomes(int noOfMutatedChromosomes) {
        Chromosome[] mutatedChromosomes = new Chromosome[noOfMutatedChromosomes];
        for (int i = 0; i < noOfMutatedChromosomes; i++) {
            mutatedChromosomes[i] = getRandomChromosome().mutateIndividual();
        }
        return mutatedChromosomes;
    }

    public Chromosome[] getCrossedChromosomes(int noOfCrossedChromosomes) {
        Chromosome[] crossedChromosomes = new Chromosome[noOfCrossedChromosomes];
        Chromosome[] children;
        int i = 0;
        while (i < noOfCrossedChromosomes) {
            Chromosome[] partners = getPairOfChromosomes(chromosomes);
            children = partners[0].crossWithPartner(partners[1]);
            for (int j = 0; j < 2 && i < noOfCrossedChromosomes; j++) {
                if (children[j] != null) {
                    crossedChromosomes[i++] = children[j];
                }
            }
        }
        return crossedChromosomes;
    }

    public Population getNextPopulation() {
        Chromosome[] clonedChromosomes = getClonedChromosomes(clonedIndividuals);
        Chromosome[] mutatedChromosomes = getMutatedChromosomes(mutatedIndividuals);
        Chromosome[] crossedChromosomes = getCrossedChromosomes(crossedIndividuals);
        int n = clonedIndividuals+mutatedIndividuals+crossedIndividuals;
        Chromosome[] newChromosomes = new Chromosome[clonedIndividuals+mutatedIndividuals+crossedIndividuals];
        int pos = 0;
        System.arraycopy(clonedChromosomes,0,newChromosomes, 0, clonedIndividuals);
        pos+= clonedIndividuals;
        System.arraycopy(mutatedChromosomes,0,newChromosomes, pos, mutatedIndividuals);
        pos+=mutatedIndividuals;
        System.arraycopy(crossedChromosomes,0,newChromosomes, pos, crossedIndividuals);
        Population newPopulation = new Population(newChromosomes, hyperparameters);
        return newPopulation;
    }

    public Chromosome[] getChromosomes() {
        return chromosomes;
    }
}
