package model;

public class Warehouse {
    private int id;
    private int supply;

    public Warehouse(int id, int supply) {
        this.id = id;
        this.supply = supply;
    }

    public int getID() {
        return id;
    }

    public int getSupply() {
        return supply;
    }
}
