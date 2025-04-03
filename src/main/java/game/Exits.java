package game;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Exits {
    private String north; // Keep as String for JSON deserialization
    private String south;
    private String east;
    private String west;
    private String up;
    private String down;
    
    // Actual Room references (not serialized)
    private transient Room northRoom;
    private transient Room southRoom;
    private transient Room eastRoom;
    private transient Room westRoom;
    private transient Room upRoom;
    private transient Room downRoom;

    public Exits(
        @JsonProperty("north") String north,
        @JsonProperty("south") String south,
        @JsonProperty("east") String east,
        @JsonProperty("west") String west,
        @JsonProperty("up") String up,
        @JsonProperty("down") String down) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    Exits() {
        // Default constructor for Jackson deserialization
    }

    // String getters - for original IDs from JSON
    public String getNorth() { return north; }
    public String getSouth() { return south; }
    public String getEast() { return east; }
    public String getWest() { return west; }
    public String getUp() { return up; }
    public String getDown() { return down; }
    
    // Room getters - for actual room references
    public Room getNorthRoom() { return northRoom; }
    public Room getSouthRoom() { return southRoom; }
    public Room getEastRoom() { return eastRoom; }
    public Room getWestRoom() { return westRoom; }
    public Room getUpRoom() { return upRoom; }
    public Room getDownRoom() { return downRoom; }
    
    // Room setters
    public void setNorth(Room room) { this.northRoom = room; }
    public void setSouth(Room room) { this.southRoom = room; }
    public void setEast(Room room) { this.eastRoom = room; }
    public void setWest(Room room) { this.westRoom = room; }
    public void setUp(Room room) { this.upRoom = room; }
    public void setDown(Room room) { this.downRoom = room; }
}