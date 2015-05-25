package pl.codewise.geecon;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

public class EscapeAnalysisWithEnumerationTest {

    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.add(2);
        vector.add(3);
        vector.add(4);
        vector.add(5);

        int sum = 0;
        for (long i = 0; i < 1000_000_000L; i++) {
            sum += getSum(vector, sum);
        }

        System.out.println(sum);
    }

    private static int getSum(Vector<Integer> vector, int sum) {
        Iterable<Integer> iterator = () -> new EnumerationAsIterator<>(vector.elements());
        for (Integer element : iterator) {
            sum += element;
        }

        return sum;
    }
}

class EnumerationAsIterator<E> implements Iterator<E> {

    private Enumeration<E> enumeration;

    public EnumerationAsIterator(Enumeration<E> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    @Override
    public E next() {
        return enumeration.nextElement();
    }
}