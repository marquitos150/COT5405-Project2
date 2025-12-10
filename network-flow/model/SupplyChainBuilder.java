package model;

import graph.Network;

import java.util.List;

public class SupplyChainBuilder {
	
	public class Graph {
		public Network n;
	    public int source;
	    public int sink;
	    
	    public Graph(Network n, int source, int sink) {
            this.n = n;
            this.source = source;
            this.sink = sink;
        }
	}

    public Graph build(List<Warehouse> warehouses, List<Store> stores, int[][] costMatrix) {
        int w = warehouses.size();
        int s = stores.size();

        int source = 0;
        int firstWarehouse = 1;
        int firstStore = w + 1;
        int sink = w + s + 1;

        Network network = new Network(sink + 1);
        
        // add edges from source -> warehouses
        for (int i = 0; i < w; i++) {
            Warehouse wi = warehouses.get(i);
            network.addEdge(source, firstWarehouse + i, wi.getSupply(), 0);
        }

        // add edges from warehouses -> stores
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < s; j++) {
            	int cost = costMatrix[i][j];
            	
                network.addEdge(firstWarehouse + i, firstStore + j, Integer.MAX_VALUE, cost);
            }
        }

        // add edges from stores -> sink
        for (int j = 0; j < s; j++) {
            Store sj = stores.get(j);
            network.addEdge(firstStore + j, sink, sj.getDemand(), 0);
        }

        return new Graph(network, source, sink);
    }
}
