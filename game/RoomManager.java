package game;

import java.util.*;

import javax.swing.SizeRequirements;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.net.http.WebSocket.Listener;

public class RoomManager{

    private List<Room> rooms;
    private List<Item> itemsList;
    private String jsonFilePath;
    private Random random;

    public RoomManager(String jsonFilePath){

        this.jsonFilePath = jsonFilePath;
        this.rooms = new ArrayList<>();
        this.random = new Random();
        
        loadRooms();
        loadItems();
        
    }

    public void loadRooms(){
        try{

            ObjectMapper objectMapper = new ObjectMapper();
            rooms = objectMapper.readValue(new File("../data/rooms.json"), new TypeReference<List<Room>>() {});

            shuffleItems();

        } catch (IOException e){
            e.printStackTrace();
        }
        
        
    }

    public void loadItems(){
        try{

            ObjectMapper objectMapper = new ObjectMapper();
            itemsList = objectMapper.readValue(new File("../data/items.json"), new TypeReference<List<Item>>() {});

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Room getRoom(String id){
        for (Room room : rooms) {
            if (room.getId().equals(id)){
                return room;
            }
        }
        return null;
    }

    public void shuffleItems(){
        List<Item> availabelItems = new ArrayList<>();
        // Add all items that are not disabled to the available items list
        for (Item item : itemsList) {
            if (!item.isDisabled()){
                availabelItems.add(item);
            }
        }
        // Shuffle the items order
        Collections.shuffle(availabelItems, random);
        // Add the items to the rooms (call shuffleRooms() before this)
        int itemIndex = 0;
        for (Room room : rooms) {
            room.clearItems();
            if (itemIndex < availabelItems.size()){
                room.addItems(availabelItems.get(itemIndex));
                itemIndex++;
            }
        }
    }
    

    public void shuffleRooms() {
        List <Room> preShuffle = new ArrayList<>(rooms);
        Collections.shuffle(rooms, random);
        
        // Now, go through each room and set the exits
    for (int i = 0; i < rooms.size(); i++) {
        Room currentRoom = rooms.get(i);
        Room preShuffleRoom = preShuffle.get(i);
        
        // Get the exits from the pre-shuffled room
        Exits preShuffleExits = preShuffleRoom.getExits();
        
        // Set the exits of the current room based on the pre-shuffled room
        Exits currentExits = currentRoom.getExits();
        
        // Modify individual exits if needed
        if (preShuffleExits.getUp() != null) {
            currentExits.setUp(preShuffleExits.getUp());
        }
        else {
            currentExits.setUp(null);
        }
        if (preShuffleExits.getDown() != null) {
            currentExits.setDown(preShuffleExits.getDown());
        }
        else {
            currentExits.setDown(null);
        }
        if (preShuffleExits.getNorth() != null) {
            currentExits.setNorth(preShuffleExits.getNorth());
        }
        else {
            currentExits.setNorth(null);
        }
        if (preShuffleExits.getSouth() != null) {
            currentExits.setSouth(preShuffleExits.getSouth());
        }
        else {
            currentExits.setSouth(null);
        }
        if (preShuffleExits.getEast() != null) {
            currentExits.setEast(preShuffleExits.getEast());
        }
        else {
            currentExits.setEast(null);
        }
        if (preShuffleExits.getWest() != null) {
            currentExits.setWest(preShuffleExits.getWest());
        }
        else {
            currentExits.setWest(null);
        }
            

        // Set the exits of the current room
        currentRoom.setExits(currentExits);
    }
        
    }


    
}