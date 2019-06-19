package dataset;

import java.io.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Amelia on 3/5/2019.
 */
public class DatasetGenerator {
    public static int[] g, v;
    public static int N, G;

    public static void initializeDataset(int _n) {
        N = _n;
        g = new int[N];
        v = new int[N];
        G = 1 + (int) (Math.random() * N * 30);
        for (int i = 0; i < N; i++) {
            g[i] = 1 + (int) (Math.random() * G/3);
            v[i] = (int) (Math.random() * 100);
            System.out.println("Object " + i + ": w = " + g[i] + " v = " + v[i]);
        }
        System.out.println("dataset.dataset weight: " + G);
    }
    public static void writeDataset(Dataset kn, String fileName){
        try(FileWriter fw = new FileWriter(fileName)) {
            JSONObject json = new JSONObject();
            json.put("N", kn.getN());
            json.put("G", kn.getG());
            JSONArray weights = new JSONArray();
            JSONArray values = new JSONArray();
            for (int i = 0; i < kn.getN(); i ++) {
                weights.add(kn.getWeight()[i]);
                values.add(kn.getValue()[i]);
            }
            json.put("g", weights);
            json.put("v", values);
            fw.write(json.toJSONString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Dataset readDataset(String fileName){
        Dataset kn = null;
        JSONParser parser = new JSONParser();
        try(FileReader fr = new FileReader(fileName)) {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(fileName));
            int N = ((Long) jsonObject.get("N")).intValue();
            int G = ((Long) jsonObject.get("G")).intValue();
            int[] g = new int[N];
            int[] v = new int[N];
            for (int i = 0; i < N; i++) {
                g[i] = ((Long) ((JSONArray) jsonObject.get("g")).get(i)).intValue();
                v[i] = ((Long) ((JSONArray) jsonObject.get("v")).get(i)).intValue();
            }
            kn = new Dataset(N, G, g, v);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return kn;
    }

    public static void main(String[] args) {
        initializeDataset(8);
        Dataset kn = new Dataset(N, G, g, v);
        writeDataset(kn, "dataset_8.json");

        initializeDataset(10);
        kn = new Dataset(N, G, g, v);
        writeDataset(kn, "dataset_10.json");

        initializeDataset(50);
        kn = new Dataset(N, G, g, v);
        writeDataset(kn, "dataset_50.json");

        initializeDataset(100);
        kn = new Dataset(N, G, g, v);
        writeDataset(kn, "dataset_100.json");
    }
}
