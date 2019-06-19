package dataset;

public class ValueAndWeightContainer {
    private int value, weight;

    public ValueAndWeightContainer(int _value, int _weight) {
        value = _value;
        weight = _weight;
    }

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }
}
