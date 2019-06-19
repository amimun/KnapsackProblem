package dataset;

import geneticAlgorithm.Hyperparameters;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

public class Utils {

    public static int calculateTotalWeight(Dataset dataset, boolean[] solution) {
        int totalWeight = 0;
        for(int i = 0; i < dataset.getN(); i++) {
            if(solution[i]) {
                totalWeight += dataset.getWeight()[i];
            }
        }

        return totalWeight;
    }

    public static int calculateTotalValue(Dataset dataset, boolean[] solution) {
        int totalValue = 0;
        for(int i = 0; i < dataset.getN(); i++) {
            if(solution[i]) {
                totalValue += dataset.getValue()[i];
            }
        }

        return totalValue;
    }

    public static ValueAndWeightContainer processSolution(Dataset dataset, boolean[] solution) {
        int totalValue = 0;
        int totalWeight = 0;
        for(int i = 0; i < dataset.getN(); i++) {
            if(solution[i]) {
                totalWeight += dataset.getWeight()[i];
                totalValue += dataset.getValue()[i];
            }
        }

        if(totalWeight > dataset.getG()) {
            return new ValueAndWeightContainer(-1, totalWeight);
        }

        return new ValueAndWeightContainer(totalValue, totalWeight);
    }

    public static void writeGeneticSolution(Hyperparameters hyperparameters, String dataset_file, String methodName, boolean[] solution, double executionTime) {
        String fileName = "solutions" + File.separator + methodName + File.separator + dataset_file.substring(0, dataset_file.indexOf(".")) + "_sol"+hyperparameters.getPopulationSize()+".json";
        JSONObject result = new JSONObject();
        JSONArray hp = new JSONArray();
        hp.add("Iterations: " + hyperparameters.getNoIterations());
        hp.add("Population size: " + hyperparameters.getPopulationSize());
        hp.add("Crossover rate: " + hyperparameters.getCrossoverRate());
        hp.add("Cloning rate: " + hyperparameters.getCloningRate());
        hp.add("Mutation rate: " + hyperparameters.getMutationRate());
        result.put("Hyperparameters: ", hp);
        result.put("Execution time", executionTime + " s");
        Dataset dataset = DatasetGenerator.readDataset(dataset_file);
        ValueAndWeightContainer valueAndWeightContainer = Utils.processSolution(dataset, solution);
        result.put("Value", valueAndWeightContainer.getValue());
        result.put("Weight", valueAndWeightContainer.getWeight());

        JSONArray selectedObjects = new JSONArray();
        for(int i = 0; i < solution.length; i++) {
            if(solution[i]) {
                selectedObjects.add(i);
            }
        }

        result.put("Selected objects", selectedObjects);

        try{
            Path path = Paths.get(fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, result.toJSONString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeTabuSolution(int tenure, String dataset_file, String methodName, boolean[] solution, double executionTime) {
        String fileName = "solutions" + File.separator + methodName + File.separator + dataset_file.substring(0, dataset_file.indexOf(".")) + "_sol"+tenure+".json";
        JSONObject result = new JSONObject();
        result.put("Execution time", executionTime + " s");
        Dataset dataset = DatasetGenerator.readDataset(dataset_file);
        ValueAndWeightContainer valueAndWeightContainer = Utils.processSolution(dataset, solution);
        result.put("Value", valueAndWeightContainer.getValue());
        result.put("Weight", valueAndWeightContainer.getWeight());

        JSONArray selectedObjects = new JSONArray();
        for(int i = 0; i < solution.length; i++) {
            if(solution[i]) {
                selectedObjects.add(i);
            }
        }

        result.put("Selected objects", selectedObjects);

        try{
            Path path = Paths.get(fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, result.toJSONString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSolution(String dataset_file, String methodName, boolean[] solution, double executionTime) {
        String fileName = "solutions" + File.separator + methodName + File.separator + dataset_file.substring(0, dataset_file.indexOf(".")) + "_sol.json";
        JSONObject result = new JSONObject();
        result.put("Execution time", executionTime + " s");
        Dataset dataset = DatasetGenerator.readDataset(dataset_file);
        ValueAndWeightContainer valueAndWeightContainer = Utils.processSolution(dataset, solution);
        result.put("Value", valueAndWeightContainer.getValue());
        result.put("Weight", valueAndWeightContainer.getWeight());

        JSONArray selectedObjects = new JSONArray();
        for(int i = 0; i < solution.length; i++) {
            if(solution[i]) {
                selectedObjects.add(i);
            }
        }

        result.put("Selected objects", selectedObjects);

        try{
            Path path = Paths.get(fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, result.toJSONString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean[] generateRandomSolution(Dataset dataset) {
        Random random = new Random();
        int weight = 0, G = dataset.getG(), N = dataset.getN();
        boolean[] solution = new boolean[N];
        Arrays.fill(solution, Boolean.FALSE);

        for(int i = 0; i < N; i++) {
            solution[i] = random.nextBoolean();
            if (solution[i]) {
                weight += dataset.getWeight()[i];
            }
            if (weight > G) {
                solution[i] = false;
                break;
            }
        }

        return solution;
    }
}
