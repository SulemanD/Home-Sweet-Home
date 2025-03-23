package game;

import java.util.List;

public class Room {
    private String id;
    private String name;
    private String longDesc;
    private String shortDesc;
    private List<Item> items;
    private List<NPC> npcs;
    private int floor;
    private Exits exits;
 
    // Getters and Setters

    public int getFloor(){
        return floor;
    }

    public void setFloor(int floor){
        this.floor = floor;
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

   
}