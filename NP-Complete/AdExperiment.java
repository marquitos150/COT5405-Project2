import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class AdExperiment {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        int[] userSizes = {100, 500, 1000, 2000, 5000};
        int[] channelSizes = {20, 50, 100, 200};
        double coverProb = 0.2;
        int numTrials = 10;

        String outputFile = "results.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

            writer.println("numUsers,numChannels,coverProb,numTrials,avgTimeMs,avgChosenChannels,avgCoveredUsers");

            for (int numUsers : userSizes) {
                for (int numChannels : channelSizes) {
                    ExperimentResult result = runExperiments(
                            numUsers,
                            numChannels,
                            coverProb,
                            numTrials
                    );

                    writer.printf(Locale.US,
                            "%d,%d,%.3f,%d,%.4f,%.4f,%.4f%n",
                            result.numUsers,
                            result.numChannels,
                            result.coverProb,
                            result.numTrials,
                            result.avgTimeMs,
                            result.avgChosenChannels,
                            result.avgCoveredUsers
                    );
                }
            }

            System.out.println("Experiment completed. Results saved to " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ExperimentResult runExperiments(
            int numUsers,
            int numChannels,
            double coverProb,
            int numTrials
    ) {
        long totalTimeNano = 0L;
        long totalChosenChannels = 0L;
        long totalCoveredUsers = 0L;

        for (int t = 0; t < numTrials; t++) {
            Instance instance = generateRandomInstance(numUsers, numChannels, coverProb);

            long start = System.nanoTime();
            List<AdvertisingChannel> chosen =
                    AdvertisingSelector.selectChannels(instance.targetUsers, instance.channels);
            long end = System.nanoTime();

            totalTimeNano += (end - start);
            totalChosenChannels += chosen.size();

            Set<Integer> covered = new HashSet<>();
            for (AdvertisingChannel c : chosen) {
                covered.addAll(c.getCoveredUsers());
            }
            totalCoveredUsers += covered.size();
        }

        double avgTimeMs = totalTimeNano / 1_000_000.0 / numTrials;
        double avgChosenChannels = totalChosenChannels * 1.0 / numTrials;
        double avgCoveredUsers = totalCoveredUsers * 1.0 / numTrials;

        return new ExperimentResult(
                numUsers,
                numChannels,
                coverProb,
                numTrials,
                avgTimeMs,
                avgChosenChannels,
                avgCoveredUsers
        );
    }


    public static Instance generateRandomInstance(int numUsers, int numChannels, double coverProb) {
        Set<Integer> targetUsers = new HashSet<>();
        for (int i = 0; i < numUsers; i++) {
            targetUsers.add(i);
        }

        List<AdvertisingChannel> channels = new ArrayList<>();

        for (int ch = 0; ch < numChannels; ch++) {
            Set<Integer> covered = new HashSet<>();

            for (int u = 0; u < numUsers; u++) {
                if (RANDOM.nextDouble() < coverProb) {
                    covered.add(u);
                }
            }

            if (covered.isEmpty() && numUsers > 0) {
                covered.add(RANDOM.nextInt(numUsers));
            }

            channels.add(new AdvertisingChannel("Channel-" + ch, covered));
        }

        return new Instance(targetUsers, channels);
    }


    public static class Instance {
        public final Set<Integer> targetUsers;
        public final List<AdvertisingChannel> channels;

        public Instance(Set<Integer> targetUsers, List<AdvertisingChannel> channels) {
            this.targetUsers = targetUsers;
            this.channels = channels;
        }
    }


    public static class ExperimentResult {
        public final int numUsers;
        public final int numChannels;
        public final double coverProb;
        public final int numTrials;
        public final double avgTimeMs;
        public final double avgChosenChannels;
        public final double avgCoveredUsers;

        public ExperimentResult(int numUsers,
                                int numChannels,
                                double coverProb,
                                int numTrials,
                                double avgTimeMs,
                                double avgChosenChannels,
                                double avgCoveredUsers) {
            this.numUsers = numUsers;
            this.numChannels = numChannels;
            this.coverProb = coverProb;
            this.numTrials = numTrials;
            this.avgTimeMs = avgTimeMs;
            this.avgChosenChannels = avgChosenChannels;
            this.avgCoveredUsers = avgCoveredUsers;
        }
    }
}
