package backtracking;

import java.util.Arrays;
import dataset.Dataset;
import dataset.DatasetGenerator;
import dataset.Utils;

public class Backtracking {
    public boolean[] solution;
    public int N, G;
    public int[] weight, value;

    public Backtracking(Dataset _dataset) {
        N = _dataset.getN();
        G = _dataset.getG();
        weight = _dataset.getWeight();
        value = _dataset.getValue();
        solution = new boolean[N];
        Arrays.fill(solution, false);
    }

    public boolean[] findSolution() {
        backtrack(0, 0, 0, solution);

        return solution;
    }

    private int max_value = 0;
    public void backtrack(int index, int curr_value, int curr_weight, boolean[] partial_sol) {
        if (index == N) {
            return;
        }
        if (curr_weight + weight[index] <= G) {
            partial_sol[index] = true;
            if (curr_value + value[index] > max_value) {
                max_value = curr_value + value[index];
                solution = partial_sol.clone();
            }
            backtrack(index + 1, curr_value + value[index], curr_weight + weight[index], partial_sol);
        }
        partial_sol[index] = false;
        backtrack(index + 1, curr_value, curr_weight, partial_sol);
    }

    public static void main(String[] args) {
        String datasetName = "dataset_10.json";
        Dataset dataset = DatasetGenerator.readDataset(datasetName);
        Backtracking backtracking = new Backtracking(dataset);
        long t_start = System.nanoTime();
        boolean[] solution = backtracking.findSolution();
        long t_finish = System.nanoTime();
        Utils.writeSolution(datasetName, "backtracking", solution, (t_finish - t_start)/1000000000.);
    }

}
