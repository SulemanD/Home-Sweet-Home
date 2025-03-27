package game;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;

public class RoomManager{

    private List<Room> rooms;
    private Map<String, Room> roomMap;
    private List<Item> itemsList;
    private Random random;
    private String roomsJson;
    private String itemsJson;
    private static Map<String, Item> itemRegistry;

    public RoomManager(){
        this.rooms = new ArrayList<>();
        this.roomMap = new HashMap<>();
        this.random = new Random();
        this.itemRegistry = new HashMap<>();
        this.itemsList = new ArrayList<>();
        loadItems();
        loadRoomsWithoutExits();
        assignExits();
    }

    public void loadRoomsWithoutExits(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            rooms = objectMapper.readValue(new File("data/rooms.json"), TypeFactory.defaultInstance().constructCollectionType(List.class, Room.class));

            for (Room room : rooms) {
                roomMap.put(room.getId(), room);
                System.out.println("Loaded: " + room.getId());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void assignExits(){
        for (Room room : rooms) {
            Exits exits = room.getExits();
            if (exits != null) {
                if (exits.getNorth() != null){
                    exits.setNorth(roomMap.get(exits.getNorth()));
                }
                if (exits.getSouth() != null){
                    exits.setSouth(roomMap.get(exits.getSouth()));
                }
                if (exits.getEast() != null){
                    exits.setEast(roomMap.get(exits.getEast()));
                }
                if (exits.getWest() != null){
                    exits.setWest(roomMap.get(exits.getWest()));
                }
                if (exits.getUp() != null){
                    exits.setUp(roomMap.get(exits.getUp()));
                }
                if (exits.getDown() != null){
                    exits.setDown(roomMap.get(exits.getDown()));
                }
            }
        }
    }

    public void loadItems() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            itemsList = objectMapper.readValue(new File("data/items.json"), 
                TypeFactory.defaultInstance().constructCollectionType(List.class, Item.class));

            for (Item item : itemsList) {
                itemRegistry.put(item.getId(), item);
                System.out.println("Loaded item: " + item.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Item getItemById(String itemId) {
        return itemRegistry.get(itemId);
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
            
            if (currentExits != null && preShuffleExits != null) {
                // Modify individual exits if needed
                if (preShuffleExits.getNorthRoom() != null) {
                    currentExits.setNorth(preShuffleExits.getNorthRoom());
                } else {
                    currentExits.setNorth(null);
                }
                
                if (preShuffleExits.getSouthRoom() != null) {
                    currentExits.setSouth(preShuffleExits.getSouthRoom());
                } else {
                    currentExits.setSouth(null);
                }
                
                if (preShuffleExits.getEastRoom() != null) {
                    currentExits.setEast(preShuffleExits.getEastRoom());
                } else {
                    currentExits.setEast(null);
                }
                
                if (preShuffleExits.getWestRoom() != null) {
                    currentExits.setWest(preShuffleExits.getWestRoom());
                } else {
                    currentExits.setWest(null);
                }
                
                if (preShuffleExits.getUpRoom() != null) {
                    currentExits.setUp(preShuffleExits.getUpRoom());
                } else {
                    currentExits.setUp(null);
                }
                
                if (preShuffleExits.getDownRoom() != null) {
                    currentExits.setDown(preShuffleExits.getDownRoom());
                } else {
                    currentExits.setDown(null);
                }

                // Set the exits of the current room
                currentRoom.setExits(currentExits);
            }
        }
    }
}