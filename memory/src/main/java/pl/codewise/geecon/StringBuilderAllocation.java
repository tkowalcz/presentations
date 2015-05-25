package pl.codewise.geecon;

public class StringBuilderAllocation {

    public static void main(String[] args) {
        String containsTest = "contains or not?";
        StringBuilder builder = new StringBuilder("Test string that is long");

        while (true) {
            if (containsTest.contains(builder)) {
                System.out.println("Contains! (??)");
            }
        }
    }
}
