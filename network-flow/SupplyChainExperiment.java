import graph.MaxFlowMinCost;
import model.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SupplyChainExperiment {

	public static void main(String[] args) throws IOException {
		System.out.println("=== Supply Chain Min-Cost Max-Flow Example ===");

        // Example data
        List<Warehouse> warehouses = List.of(
            new Warehouse(0, 15),
            new Warehouse(1, 20)
        );

        List<Store> stores = List.of(
            new Store(0, 10),
            new Store(1, 12),
            new Store(2, 13)
        );
        
        int[][] costMatrix = {
            {4, 6, 3},   // W0 → S0,S1,S2
            {2, 5, 4}    // W1 → S0,S1,S2
        };
        
        // Build the network
        SupplyChainBuilder builder = new SupplyChainBuilder();
        SupplyChainBuilder.Graph network = builder.build(warehouses, stores, costMatrix);

        // Run min-cost max-flow
        MaxFlowMinCost mcmf = new MaxFlowMinCost();

        MaxFlowMinCost.Result res = mcmf.getMaxFlowMinCost(
        	network.n,
            network.source,
            network.sink
        );

        System.out.println("\nMax Flow: " + res.maxFlow);
        System.out.println("Min Cost: " + res.minCost);
        System.out.println();
        
        System.out.println("=== Supply Chain Max Flow Min Cost Runtime ===");
        int[] warehouseSizes = {5, 10, 20, 50, 100};  // different numbers of warehouses
        int[] storeSizes = {50, 100, 200, 500, 1000};     // corresponding number of stores

        Random rand = new Random();
        
        try {
            // Make sure data folder exists
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }
            
            FileWriter csvWriter = new FileWriter("data/network-flow-results.csv");
    		csvWriter.write("warehouses,stores,maxflow,mincost,Time(ms)\n");

            for (int w : warehouseSizes) {
            	for (int s : storeSizes) {
            		// Generate warehouses
                    List<Warehouse> myWarehouses = new ArrayList<>();
                    for (int i = 0; i < w; i++) {
                        myWarehouses.add(new Warehouse(i, 10 + rand.nextInt(40))); // random supply 10-50
                    }

                    // Generate stores
                    List<Store> myStores = new ArrayList<>();
                    for (int j = 0; j < s; j++) {
                        myStores.add(new Store(j, 5 + rand.nextInt(15))); // random demand 5-20
                    }

                    // Generate random cost matrix
                    int[][] myCostMatrix = new int[w][s];
                    for (int i = 0; i < w; i++) {
                        for (int j = 0; j < s; j++) {
                            myCostMatrix[i][j] = 1 + rand.nextInt(50); // cost 1-50
                        }
                    }

                    // Build network
                    SupplyChainBuilder myBuilder = new SupplyChainBuilder();
                    SupplyChainBuilder.Graph myNetwork = myBuilder.build(myWarehouses, myStores, myCostMatrix);

                    // Run algorithm and measure time
                    MaxFlowMinCost maxFlowMinCost = new MaxFlowMinCost();
                    long start = System.nanoTime();
                    MaxFlowMinCost.Result result = maxFlowMinCost.getMaxFlowMinCost(myNetwork.n, myNetwork.source, myNetwork.sink);
                    long end = System.nanoTime();

                    double timeMilliSeconds = (end - start) / 1e6;

                    // Print results
                    System.out.println("Warehouses: " + w + ", Stores: " + s);
                    System.out.println("Max Flow: " + result.maxFlow + ", Min Cost: " + result.minCost);
                    System.out.println("Execution Time: " + timeMilliSeconds + " milliseconds\n");
                    
                    csvWriter.write(w + "," + s + "," + result.maxFlow + "," + result.minCost + "," + String.format("%.3f", timeMilliSeconds) + "\n");
            	}
            }
            
            csvWriter.close();
            
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}
}