package pl.codewise.geecon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OffHeapAllocations {

    public static final int THREADS = 500;

    public static void main(String[] args) throws IOException {
        System.in.read();

        ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
        for (int i = 0; i < THREADS; i++) {
            executorService.submit(OffHeapAllocations::loadFile);
        }

        System.out.println("Done!");
        System.in.read();
    }

    private static void loadFile() {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get("random.stuff"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        consume(bytes);
    }

    private static void consume(byte[] bytes) {
        int sum = 0;
        for (int i = 0; i < bytes.length; i++) {
            sum += bytes[i];
        }

        System.out.println(sum);
    }
}
