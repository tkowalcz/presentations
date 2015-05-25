package openjdk.java.lang;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;

public class EscapeAnalysisWithEnumerationTest {

    public static void main(String[] args) throws SocketException {
        int sum = 0;
        for (long i = 0; i < 1000_000_000L; i++) {
            sum += getSum(sum);
        }

        System.out.println(sum);
    }

    private static int getSum(int sum) throws SocketException {
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        Iterable<NetworkInterface> iterator = () -> new EnumerationAsIterator<>(enumeration);
        sum = getSum(sum, iterator);

        return sum;
    }

    private static int getSum(int sum, Iterable<NetworkInterface> iterator) {
        for (NetworkInterface element : iterator) {
            sum += element.getIndex();
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