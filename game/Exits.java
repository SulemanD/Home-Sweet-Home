package game;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Exits {
    private String north;
    private String south;
    private String east;
    private String west;
    private String up;
    private String down;

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

    public String getNorth() { return north; }
    public void setNorth(String north) { this.north = north; }

    public String getSouth() { return south; }
    public void setSouth(String south) { this.south = south; }

    public String getEast() { return east; }
    public void setEast(String east) { this.east = east; }

    public String getWest() { return west; }
    public void setWest(String west) { this.west = west; }

    public String getUp() { return up; }
    public void setUp(String up) { this.up = up; }
    
    public String getDown() { return down; }
    public void setDown(String down) { this.down = down; }
}