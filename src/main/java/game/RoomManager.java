package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class RoomManager {

    private List<Room> rooms;
    private Map<String, Room> roomMap;
    private List<Item> itemsList;
    private Random random;
    private String roomsJson;
    private String itemsJson;
    private static Map<String, Item> itemRegistry;

    public RoomManager() {
        this.rooms = new ArrayList<>();
        this.roomMap = new HashMap<>();
        this.random = new Random();
        this.itemRegistry = new HashMap<>();
        this.itemsList = new ArrayList<>();
        loadItems();
        loadRoomsWithoutExits();
        assignExits();
        shuffleItems();
    }

    public void loadRoomsWithoutExits() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            rooms = objectMapper.readValue(new File("data/rooms.json"), TypeFactory.defaultInstance().constructCollectionType(List.class, Room.class));

            for (Room room : rooms) {
                roomMap.put(room.getId(), room);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void assignExits() {
        for (Room room : rooms) {
            Exits exits = room.getExits();
            if (exits != null) {
                if (exits.getNorth() != null) {
                    exits.setNorth(roomMap.get(exits.getNorth()));
                }
                if (exits.getSouth() != null) {
                    exits.setSouth(roomMap.get(exits.getSouth()));
                }
                if (exits.getEast() != null) {
                    exits.setEast(roomMap.get(exits.getEast()));
                }
                if (exits.getWest() != null) {
                    exits.setWest(roomMap.get(exits.getWest()));
                }
                if (exits.getUp() != null) {
                    exits.setUp(roomMap.get(exits.getUp()));
                }
                if (exits.getDown() != null) {
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Item getItemById(String itemId) {
        return itemRegistry.get(itemId);
    }

    public Room getRoom(String id) {
        for (Room room : rooms) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        return null;
    }

    public void shuffleItems() {
        List<Item> availableItems = new ArrayList<>();
        for (Item item : itemsList) {
            if (!item.isDisabled()) {
                availableItems.add(item);
            }
        }
        Collections.shuffle(availableItems, random);
        int itemIndex = 0;
        for (Room room : rooms) {
            room.clearItems();
            if (itemIndex < availableItems.size()) {
                Item item = availableItems.get(itemIndex);
                room.addItems(item);
                itemIndex++;
            }
        }
    }

    public void shuffleRooms() {
        List<Room> preShuffle = new ArrayList<>(rooms);
        Collections.shuffle(rooms, random);

        Map<Room, Room> roomMapping = new HashMap<>();
        for (int i = 0; i < preShuffle.size(); i++) {
            roomMapping.put(preShuffle.get(i), rooms.get(i));
        }

        for (int i = 0; i < rooms.size(); i++) {
            Room originalRoom = preShuffle.get(i);
            Room shuffledRoom = rooms.get(i);
            Exits originalExits = originalRoom.getExits();
            Exits newExits = new Exits();

            if (originalExits.getNorthRoom() != null) {
                newExits.setNorth(roomMapping.get(originalExits.getNorthRoom()));
            }
            if (originalExits.getSouthRoom() != null) {
                newExits.setSouth(roomMapping.get(originalExits.getSouthRoom()));
            }
            if (originalExits.getEastRoom() != null) {
                newExits.setEast(roomMapping.get(originalExits.getEastRoom()));
            }
            if (originalExits.getWestRoom() != null) {
                newExits.setWest(roomMapping.get(originalExits.getWestRoom()));
            }
            if (originalExits.getUpRoom() != null) {
                newExits.setUp(roomMapping.get(originalExits.getUpRoom()));
            }
            if (originalExits.getDownRoom() != null) {
                newExits.setDown(roomMapping.get(originalExits.getDownRoom()));
            }

            shuffledRoom.setExits(newExits);
        }
    }
}
