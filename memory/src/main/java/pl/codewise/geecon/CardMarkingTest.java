package pl.codewise.geecon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CardMarkingTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardMarkingTest.class);

    public static Object[][] objects;

    public static void main(String[] args) {
        objects = new Object[1024 * 1024][];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = new Object[350];
            for (int j = 0; j < objects[i].length; j++) {
                objects[i][j] = new SomeClass();
            }
        }

        System.out.println("Done!");

        while (true) {
            LOGGER.debug("{} {}", "a", "b");
        }
    }
}

class SomeClass {

    private Object reference;

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }
}
