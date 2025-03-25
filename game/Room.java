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
 
    // Getters and Setters]

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



   
}