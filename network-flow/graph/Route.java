package graph;

public class Route {
    private int to;
    private int capacity;
    private int cost;
    private int flow;
    private Route reverse;

    public Route(int to, int capacity, int cost) {
        this.to = to;
        this.capacity = capacity;
        this.cost = cost;
        this.flow = 0;
    }

    public int capacityLeftOver() {
        return capacity - flow;
    }

    public void addFlow(int f) {
        flow += f;
        reverse.flow -= f;
    }
    
    public Route getReverse() {
    	return reverse;
    }
    
    public void setReverse(Route reverse) {
    	this.reverse = reverse;
    }
    
    public int getDestination() {
    	return to;
    }
    
    public int getCost() {
    	return cost;
    }
    
}
