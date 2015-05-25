package pl.codewise.geecon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2AllocationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Log4j2AllocationTest.class);

    public static void main(String[] args) {
        while (true) {
            LOGGER.debug("{} {}", "a", "b");
        }
    }
}
