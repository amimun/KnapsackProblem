package tabuSearch;

import dataset.Dataset;
import dataset.DatasetGenerator;
import dataset.Utils;

import java.util.Arrays;

public class TabuSearch {
    private boolean[] bestSolution;
    private int bestValue, currentBestValue;
    private int tabuTenure, iterations;
    private Dataset dataset;
    private FixedSizeQueue tabuMoves;

    public TabuSearch(Dataset _dataset, int _tabuTenure, int _iterations, boolean[] _randomSolution) {
        dataset = _dataset;
        tabuTenure = _tabuTenure;
        iterations = _iterations;
        tabuMoves = new FixedSizeQueue(tabuTenure);
        bestSolution = Arrays.copyOf(_randomSolution, dataset.getN());
        bestValue = Utils.processSolution(dataset, bestSolution).getValue();
    }

    private boolean[][] getNeighboringSolutions(boolean[] solution){
        int n = solution.length;
        boolean[][] neighboringSolutions = new boolean[n][];
        for (int i = 0; i < n; i++) {
            neighboringSolutions[i] = Arrays.copyOf(solution, n);
            neighboringSolutions[i][i] = !neighboringSolutions[i][i];
        }
        return neighboringSolutions;
    }

    private boolean[] and(boolean[] a, boolean[] b) {
        int len = a.length;
        boolean[] solution = new boolean[len];
        for (int i = 0; i < len; i++) {
            solution[i] = a[i] && b[i];
        }
        return solution;
    }

    private boolean isTabu(int move){
        return tabuMoves.contains(move);
    }

    private boolean[] getBestSolution(boolean[] currentSolution, boolean[][] neighboringSolutions){
        currentBestValue = 0;
        int move = - 1, n = dataset.getN();
        boolean[] currentBestSolution = new boolean[n];
        for (int i = 0; i < dataset.getN(); i++) {
            int value = Utils.processSolution(dataset, neighboringSolutions[i]).getValue();
            boolean setFalse = Arrays.equals(neighboringSolutions[i], and(neighboringSolutions[i], currentBestSolution));
            if (value > currentBestValue && /*setFalse ? true :*/ !isTabu(i)) {
                currentBestValue = value;
                currentBestSolution = Arrays.copyOf(neighboringSolutions[i], n);
                move = i;
            } else if (value > bestValue) {
                bestValue = value;
                currentBestValue = value;
                currentBestSolution = Arrays.copyOf(neighboringSolutions[i], n);
                bestSolution = Arrays.copyOf(neighboringSolutions[i], n);
                //move = i;
            }
        }
        if (move != -1) {
            tabuMoves.addElement(move);
        }
        return currentBestSolution;
    }

    public boolean[] runTabuSearch() {
        int n = dataset.getN();
        boolean[] currentSolution = Arrays.copyOf(bestSolution, n);
        for (int i = 0; i < iterations; i++) {
            boolean[][] neighbors = getNeighboringSolutions(currentSolution);
            currentSolution = getBestSolution(currentSolution, neighbors);
            if (currentSolution == null) {
                return bestSolution;
            }
        }
        return bestSolution;
    }

    public static void main(String[] args){
        String datasetName = "dataset_8.json";
        int[] tenures_8 = {2, 3, 5};
        int[] tenures_10 = {3, 5, 7};
        int[] tenures_50 = {5, 10, 30};
        int[] tenures_100 = {10, 20, 40};
        TabuSearch tabuSearch;
        Dataset dataset = DatasetGenerator.readDataset(datasetName);
        for (int tenure : tenures_8) {
            tabuSearch = new TabuSearch(dataset, tenure, 1000, Utils.generateRandomSolution(dataset));
            double t_start = System.nanoTime();
            boolean[] solution = tabuSearch.runTabuSearch();
            double t_finish = System.nanoTime();
            Utils.writeTabuSolution(tenure, datasetName, "tabu_search", solution, (t_finish - t_start) / 1000000000.);
        }
    }

    @Override
    public String toString(){
        return "";
    }
}
