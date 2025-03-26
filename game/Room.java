package game;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    private String id;
    private String name;
    private String longDesc;
    private String shortDesc;
    private List<Item> items;
    private Exits exits;
 
    // Getters and Setters]

    public Room(@JsonProperty("id") String id, 
    @JsonProperty("name") String name, 
    @JsonProperty("longDesc") String longDesc, 
    @JsonProperty("shortDesc") String shortDesc, 
    @JsonProperty("items") List<Item> items) {
        this.id = id;
        this.name = name;
        this.longDesc = longDesc;
        this.shortDesc = shortDesc;
        this.items = new ArrayList<>();
        this.exits = null;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getLongDesc(){
        return longDesc;
    }

    public String getShortDesc(){
        return shortDesc;
    }

    public void addItems(Item item){
        items.add(item);
    }

    public void removeItems(Item item){
        items.remove(item);
    }

    public void clearItems(){
        items.clear();
    }

    public List<Item> getItems() {
        return items;
    }

    public Exits getExits(){
        return exits;
    }

    public void setExits(Exits exits){
        this.exits = exits;
    }

    public List<Room> getConnectedRooms() {
        List<Room> rooms = new ArrayList<>();
        if (exits.getNorth() != null) rooms.add(exits.getNorth());
        if (exits.getSouth() != null) rooms.add(exits.getSouth());
        if (exits.getEast() != null) rooms.add(exits.getEast());
        if (exits.getWest() != null) rooms.add(exits.getWest());
        if (exits.getUp() != null) rooms.add(exits.getUp());
        if (exits.getDown() != null) rooms.add(exits.getDown());
        return rooms;
    }



   
}