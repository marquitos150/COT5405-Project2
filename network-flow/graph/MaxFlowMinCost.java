package graph;

import java.util.*;

public class MaxFlowMinCost {
	
	public class Result {
		public int maxFlow;
		public int minCost;
		
		public Result(int maxFlow, int minCost) {
			this.maxFlow = maxFlow;
			this.minCost = minCost;
		}
	}

    public Result getMaxFlowMinCost(Network network, int source, int sink) {
        int n = network.getAdj().size();
        int maxFlow = 0;
        int minCost = 0;

        while (true) {
            int[] dist = new int[n];
            Arrays.fill(dist, Integer.MAX_VALUE);
            dist[source] = 0;
            
            Route[] parent = new Route[n];

            Queue<Integer> q = new LinkedList<>();
            q.add(source);

            boolean[] inQueue = new boolean[n];
            inQueue[source] = true;

            while (!q.isEmpty()) {
                int u = q.poll();
                inQueue[u] = false;

                for (Route r : network.getAdj().get(u)) {
                	int to = r.getDestination();
                	int cost = r.getCost();
                	int remaining = r.capacityLeftOver();
                	
                    if (remaining > 0 && dist[to] > dist[u] + cost) {
                        dist[to] = dist[u] + cost;
                        parent[to] = r;
                        if (!inQueue[to]) {
                            q.add(to);
                            inQueue[to] = true;
                        }
                    }
                }
            }

            if (parent[sink] == null) break;

            int bottleneck = Integer.MAX_VALUE;
            for (Route r = parent[sink]; r != null; r = parent[r.getReverse().getDestination()]) {
                bottleneck = Math.min(bottleneck, r.capacityLeftOver());
            }

            for (Route r = parent[sink]; r != null; r = parent[r.getReverse().getDestination()]) {
                r.addFlow(bottleneck);
                minCost += bottleneck * r.getCost();
            }

            maxFlow += bottleneck;
        }

        return new Result(maxFlow, minCost);
    }
}
