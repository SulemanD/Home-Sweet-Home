package game;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Exits {
    private Room north;
    private Room south;
    private Room east;
    private Room west;
    private Room up;
    private Room down;

    public Exits(
        @JsonProperty("north") Room north,
        @JsonProperty("south") Room south,
        @JsonProperty("east") Room east,
        @JsonProperty("west") Room west,
        @JsonProperty("up") Room up,
        @JsonProperty("down") Room down) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    public Room getNorth() { return north; }
    public void setNorth(Room north) { this.north = north; }

    public Room getSouth() { return south; }
    public void setSouth(Room south) { this.south = south; }

    public Room getEast() { return east; }
    public void setEast(Room east) { this.east = east; }

    public Room getWest() { return west; }
    public void setWest(Room west) { this.west = west; }

    public Room getUp() { return up; }
    public void setUp(Room up) { this.up = up; }
    
    public Room getDown() { return down; }
    public void setDown(Room down) { this.down = down; }
}