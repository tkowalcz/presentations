package pl.codewise.hyperion.histogram;

import com.google.common.util.concurrent.Uninterruptibles;
import org.HdrHistogram.Histogram;
import org.HdrHistogram.Recorder;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class HistogramsOutputs {

    public static final int MILLISECONDS_IN_HOUR = 60 * 60 * 1000;

    public static void main(String[] args) {
        Recorder recorder = new Recorder(1, MILLISECONDS_IN_HOUR, 2);

        for (int i = 0; i < 20; i++) {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

            HistogramUtils.populateHistogram(1, value -> recorder.recordValue(value.longValue()));
            Histogram histogram = recorder.getIntervalHistogram();

            long value = 0;
            long expectedIntervalBetweenValueSamples = 0;

            histogram.recordValueWithExpectedInterval(
                    value,
                    expectedIntervalBetweenValueSamples
            );

            histogram.copyCorrectedForCoordinatedOmission(
                    expectedIntervalBetweenValueSamples
            );

            double startTimestamp = TimeUnit.MILLISECONDS.toSeconds(histogram.getStartTimeStamp());
            double intervalLength = TimeUnit.MILLISECONDS.toSeconds(histogram.getEndTimeStamp() - histogram.getStartTimeStamp());
            double intervalMax = histogram.getMaxValueAsDouble();

            ByteBuffer byteBuffer = ByteBuffer.allocate(histogram.getNeededByteBufferCapacity());
            histogram.encodeIntoCompressedByteBuffer(byteBuffer);

            ByteBuffer histo = Base64.getEncoder().encode((ByteBuffer) byteBuffer.flip());

            String base64Histogram = new String(histo.array(), 0, histo.limit(), Charset.defaultCharset());
            System.out.printf("%s %s %s %s\n", startTimestamp, intervalLength, intervalMax, base64Histogram);
        }
    }
}
