import java.util.*;

public class AdvertisingSelector {

    public static List<AdvertisingChannel> selectChannels(
            Set<Integer> targetUsers,
            List<AdvertisingChannel> channels
    ) {
        Set<Integer> uncovered = new HashSet<>(targetUsers);

        List<AdvertisingChannel> chosen = new ArrayList<>();

        Set<AdvertisingChannel> remaining = new HashSet<>(channels);

        while (!uncovered.isEmpty() && !remaining.isEmpty()) {
            AdvertisingChannel bestChannel = null;
            int bestGain = 0;

            for (AdvertisingChannel channel : remaining) {
                int gain = 0;
                for (Integer user : channel.getCoveredUsers()) {
                    if (uncovered.contains(user)) {
                        gain++;
                    }
                }

                if (gain > bestGain) {
                    bestGain = gain;
                    bestChannel = channel;
                }
            }

            if (bestChannel == null || bestGain == 0) {
                break;
            }

            chosen.add(bestChannel);

            uncovered.removeAll(bestChannel.getCoveredUsers());

            remaining.remove(bestChannel);
        }

        if (!uncovered.isEmpty()) {
            System.out.println("Warning: could not fully cover all users. Uncovered users: " + uncovered);
        }

        return chosen;
    }
}
