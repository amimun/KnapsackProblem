package dataset;

/**
 * Created by Amelia on 3/3/2019.
 */
public class Dataset {
    private int N, G;
    private int[] g, v;

    public Dataset(int N, int G, int[] g, int[] v) {
        this.N = N;
        this.G = G;
        this.g = g;
        this.v = v;
    }

    @Override
    public String toString() {
        String data = "";
        for (int i = 0; i < N; i++) {
            data += data + "Object " + i + " with weight " + g[i] + " and value " + v[i] + ".\n";
        }
        return "G = " + G + "\nN = " + N +"\n" + data;
    }

    public int getG() {
        return G;
    }

    public int getN() {
        return N;
    }

    public int[] getValue() {
        return v;
    }

    public int[] getWeight() {
        return g;
    }
}
