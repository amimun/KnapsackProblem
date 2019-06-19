package tabuSearch;

import java.util.ArrayList;

public class FixedSizeQueue {
    private ArrayList<Integer> elements = new ArrayList<>();
    private int size;

    public FixedSizeQueue(int _size) {
        size = _size;
    }

    public void addElement(Integer element) {
        if (elements.size() == size) {
            Integer oldestElement = elements.get(0);
            elements.remove(oldestElement);
        }
        elements.add(element);
    }

    public ArrayList<Integer> getElements() {
        return elements;
    }

    public boolean contains(Integer element) {
        return elements.contains(element);
    }
}
