package neighborhoodSearch;

import dataset.Dataset;
import dataset.DatasetGenerator;
import dataset.Utils;

import java.util.Arrays;
import java.util.Random;

public class NeighborhoodSearch {
    private int N, G;
    private int[] weight, value;

    public NeighborhoodSearch(Dataset _dataset) {
        N = _dataset.getN();
        G = _dataset.getG();
        value = _dataset.getValue();
        weight = _dataset.getWeight();
    }

    public int totalValue(boolean[] chosenElements) {
        int totalValue = 0;
        for (int i = 0; i < N; i++) {
            totalValue += chosenElements[i] ? value[i] : 0;
        }
        return totalValue;
    }

    public int totalWeight(boolean[] chosenElements) {
        int totalWeight = 0;
        for (int i = 0; i < N; i++) {
            totalWeight += chosenElements[i] ? weight[i] : 0;
        }
        return totalWeight;
    }

    public boolean[] generateRandomSolution() {
        Random random = new Random();
        boolean[] solution = new boolean[N];
        int weight = 0;

        for(int i = 0; i < N; i++) {
            solution[i] = random.nextBoolean();
            weight += solution[i] ? this.weight[i] : 0;
            if (weight > G) {
                solution[i] = false;
                return solution;
            }
        }

        return solution;
    }

    public boolean[] findSolution() {
        boolean[] bestSolution = generateRandomSolution();
        boolean[] currentSolution = Arrays.copyOf(bestSolution, N);
        boolean keepSearching = true;
        int bestValue = totalValue(bestSolution);
        int currentValue;

        while(keepSearching) {
            keepSearching = false;
            for(int i = 0; i < N; i++) {
                currentSolution[i] = !bestSolution[i];
                if(totalWeight(currentSolution) <= G && (currentValue = totalValue(currentSolution)) > bestValue) {
                    bestValue = currentValue;
                    keepSearching = true;
                    bestSolution = Arrays.copyOf(currentSolution, N);
                }
                currentSolution[i] = !currentSolution[i];
            }
        }

        return bestSolution;
    }

    public static void main(String[] args) {
        String datasetName = "dataset_100.json";
        Dataset dataset = DatasetGenerator.readDataset(datasetName);
        NeighborhoodSearch neighborhoodSearch = new NeighborhoodSearch(dataset);
        long t_start = System.nanoTime();
        boolean[] solution = neighborhoodSearch.findSolution();
        long t_finish = System.nanoTime();
        Utils.writeSolution(datasetName, "neighborhood_search", solution, (t_finish - t_start)/1000000000.);
    }
}
