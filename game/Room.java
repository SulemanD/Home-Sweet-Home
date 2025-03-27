package game;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Room {
    private String id;
    private String name;
    private String longDesc;
    private String shortDesc;
    private int floor; // Added floor property
    @JsonDeserialize(contentAs = String.class)
    private List<String> items;
    @JsonDeserialize(contentAs = String.class)
    private List<String> npcs;
    private Exits exits;
    private List<Item> itemObjects;
    private List<NPC> npcObjects;
 
    // Getters and Setters]

    public Room(@JsonProperty("id") String id, 
    @JsonProperty("name") String name, 
    @JsonProperty("longDesc") String longDesc, 
    @JsonProperty("shortDesc") String shortDesc, 
    @JsonProperty("items") List<String> items,
    @JsonProperty("npcs") List<String> npcs,
    @JsonProperty("floor") int floor) { // Added floor parameter
        this.id = id;
        this.name = name;
        this.longDesc = longDesc;
        this.shortDesc = shortDesc;
        this.items = items;
        this.npcs = npcs;
        this.floor = floor; // Initialize floor property
        this.itemObjects = new ArrayList<>();
        this.npcObjects = new ArrayList<>();
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

    public int getFloor() {
        return floor;
    }

    public void addItems(Item item){
        itemObjects.add(item);
    }

    public void removeItems(Item item){
        itemObjects.remove(item);
    }

    public void clearItems(){
        itemObjects.clear();
    }

    public List<Item> getItems() {
        // Convert string IDs to actual Item objects if needed
        if (itemObjects.isEmpty() && items != null) {
            for (String itemId : items) {
                Item item = RoomManager.getItemById(itemId);
                if (item != null) {
                    itemObjects.add(item);
                }
            }
        }
        return itemObjects;
    }

    public List<NPC> getNpcs() {
        // Convert string IDs to actual NPC objects if needed
        if (npcObjects.isEmpty() && npcs != null) {
            for (String npcId : npcs) {
                NPC npc = NPCManager.getNpcById(npcId);
                if (npc != null) {
                    npcObjects.add(npc);
                }
            }
        }
        return npcObjects;
    }

    public void addNpc(NPC npc) {
        npcObjects.add(npc);
    }

    public void removeNpc(NPC npc) {
        npcObjects.remove(npc);
    }

    public void clearNpcs() {
        npcObjects.clear();
    }

    public Exits getExits(){
        return exits;
    }

    public void setExits(Exits exits){
        this.exits = exits;
    }

    public List<Room> getConnectedRooms() {
        List<Room> rooms = new ArrayList<>();
        if (exits.getNorthRoom() != null) rooms.add(exits.getNorthRoom());
        if (exits.getSouthRoom() != null) rooms.add(exits.getSouthRoom());
        if (exits.getEastRoom() != null) rooms.add(exits.getEastRoom());
        if (exits.getWestRoom() != null) rooms.add(exits.getWestRoom());
        if (exits.getUpRoom() != null) rooms.add(exits.getUpRoom());
        if (exits.getDownRoom() != null) rooms.add(exits.getDownRoom());
        return rooms;
    }
}