package graph;
import java.util.*;

public class Network {
    private List<List<Route>> adj;

    public Network(int n) {
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
        	adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int capacity, int cost) {
        Route forward = new Route(v, capacity, cost);
        Route reverse = new Route(u, 0, -cost);
        
        forward.setReverse(reverse);
        reverse.setReverse(forward);
        
        adj.get(u).add(forward);
        adj.get(v).add(reverse);
    }

    public List<List<Route>> getAdj() {
        return adj;
    }
}
