package game;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Player {
    private Room currentRoom; // Current room of the player
    private List<Item> inventory; // List to store items

    public Player(@JsonProperty("startingRoom") Room currentRoom) {
        this.currentRoom = currentRoom;
        this.inventory = new ArrayList<>(); // Initialize inventory
    }

    public void addItemToInventory(Item item) {
        inventory.add(item);
    }

    public void removeItemFromInventory(Item item) {
        inventory.remove(item);
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public List<Item> getInventory() {
        return inventory;
    }
}