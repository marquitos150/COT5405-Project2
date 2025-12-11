import java.util.*;



public class AdvertisingChannel {

    private final String id;
    private final Set<Integer> coveredUsers;

    public AdvertisingChannel(String id, Set<Integer> coveredUsers) {
        this.id = id;
        this.coveredUsers = new HashSet<>(coveredUsers);
    }

    public String getId() {
        return id;
    }

    public Set<Integer> getCoveredUsers() {
        return Collections.unmodifiableSet(coveredUsers);
    }

    @Override
    public String toString() {
        return "AdvertisingChannel{" +
                "id='" + id + '\'' +
                ", coveredUsers=" + coveredUsers +
                '}';
    }
}
