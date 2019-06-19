package geneticAlgorithm;

import dataset.Dataset;
import dataset.Utils;
import dataset.ValueAndWeightContainer;

import java.util.Arrays;
import java.util.Random;

public class Chromosome {
    private boolean[] genes;
    private Dataset dataset;
    private int fit_value, totalWeight;

    public Chromosome(boolean[] _genes, Dataset _dataset) {
        genes = new boolean[_dataset.getN()];
        System.arraycopy(_genes, 0,genes,0,_dataset.getN());
        dataset = _dataset;
        ValueAndWeightContainer valueAndWeightContainer = Utils.processSolution(dataset, genes);
        fit_value = valueAndWeightContainer.getValue();
        totalWeight = valueAndWeightContainer.getWeight();
    }

    public boolean isValid() {
        return fit_value != -1;
    }

    public Chromosome mutateIndividual() {
        boolean validMutation = false;
        boolean[] mutatedGenes = Arrays.copyOf(genes, dataset.getN());
        Chromosome mutatedChromosome = null;

        while (!validMutation) {
            Random random = new Random();
            int randomGene = random.nextInt(dataset.getN());
            if (!mutatedGenes[randomGene]) {
                if (totalWeight + dataset.getWeight()[randomGene] <= dataset.getG()) {
                    mutatedGenes[randomGene] = true;
                    mutatedChromosome = new Chromosome(mutatedGenes, dataset);
                    validMutation = true;
                }
            } else {
                mutatedGenes[randomGene] = false;
                mutatedChromosome = new Chromosome(mutatedGenes, dataset);
                validMutation = true;
            }
        }
        return mutatedChromosome;
    }

    public Chromosome[] crossWithPartner(Chromosome partner) {
        Chromosome[] children = new Chromosome[2];
        int length = dataset.getN();
        Random random = new Random();
        int p = random.nextInt(length - 1) + 1;
        boolean[] childGenes = new boolean[length];
        for (int i = 0; i < p; i++) {
            childGenes[i] = this.genes[i];
        }
        for (int i = p; i < length; i++) {
            childGenes[i] = partner.genes[i];
        }
        Chromosome firstChild = new Chromosome(childGenes, dataset);
        if (firstChild.isValid()) {
            children[0] = firstChild;
        }

        for (int i = 0; i < p; i++) {
            childGenes[i] = partner.genes[i];
        }
        for (int i = p; i < length; i++) {
            childGenes[i] = this.genes[i];
        }
        Chromosome secondChild = new Chromosome(childGenes, dataset);
        if (secondChild.isValid()) {
            children[1] = secondChild;
        }

        return children;
    }

    public int getFitValue() {
        return fit_value;
    }

    public boolean[] getGenes() {
        return genes;
    }

    @Override
    public String toString() {
        return "Fit value: " + fit_value + ", Weight: " + totalWeight + ", Data: " + Arrays.toString(genes) + "\n";
    }
}
