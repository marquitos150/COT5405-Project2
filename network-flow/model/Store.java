package model;

public class Store {
    private int id;
    private int demand;

    public Store(int id, int demand) {
        this.id = id;
        this.demand = demand;
    }

    public int getID() {
        return id;
    }

    public int getDemand() {
        return demand;
    }
}