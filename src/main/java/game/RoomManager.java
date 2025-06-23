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
        return roomMap.get(id);
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
        // Store the original room order and their IDs
        List<Room> preShuffle = new ArrayList<>(rooms);
        List<String> originalIds = new ArrayList<>();
        for (Room room : preShuffle) {
            originalIds.add(room.getId());
        }
        
        // Shuffle the rooms list
        Collections.shuffle(rooms, random);
        
        // Create a mapping from original room objects to shuffled room objects
        Map<Room, Room> roomMapping = new HashMap<>();
        for (int i = 0; i < preShuffle.size(); i++) {
            roomMapping.put(preShuffle.get(i), rooms.get(i));
        }
        
        // Update the roomMap to reflect the new room positions
        roomMap.clear();
        for (int i = 0; i < rooms.size(); i++) {
            roomMap.put(originalIds.get(i), rooms.get(i));
        }
        
        // Update exits for each room based on the mapping
        for (int i = 0; i < rooms.size(); i++) {
            Room originalRoom = preShuffle.get(i);
            Room shuffledRoom = rooms.get(i);
            Exits originalExits = originalRoom.getExits();
            
            if (originalExits != null) {
                Exits newExits = new Exits();
                
                // Map each exit to the corresponding shuffled room
                if (originalExits.getNorthRoom() != null) {
                    Room mappedNorth = roomMapping.get(originalExits.getNorthRoom());
                    if (mappedNorth != null) {
                        newExits.setNorth(mappedNorth);
                    }
                }
                if (originalExits.getSouthRoom() != null) {
                    Room mappedSouth = roomMapping.get(originalExits.getSouthRoom());
                    if (mappedSouth != null) {
                        newExits.setSouth(mappedSouth);
                    }
                }
                if (originalExits.getEastRoom() != null) {
                    Room mappedEast = roomMapping.get(originalExits.getEastRoom());
                    if (mappedEast != null) {
                        newExits.setEast(mappedEast);
                    }
                }
                if (originalExits.getWestRoom() != null) {
                    Room mappedWest = roomMapping.get(originalExits.getWestRoom());
                    if (mappedWest != null) {
                        newExits.setWest(mappedWest);
                    }
                }
                if (originalExits.getUpRoom() != null) {
                    Room mappedUp = roomMapping.get(originalExits.getUpRoom());
                    if (mappedUp != null) {
                        newExits.setUp(mappedUp);
                    }
                }
                if (originalExits.getDownRoom() != null) {
                    Room mappedDown = roomMapping.get(originalExits.getDownRoom());
                    if (mappedDown != null) {
                        newExits.setDown(mappedDown);
                    }
                }
                
                shuffledRoom.setExits(newExits);
            }
        }
    }
    
    /**
     * Updates the player's current room reference after room shuffling
     */
    public void updatePlayerRoom(Player player) {
        if (player != null && player.getCurrentRoom() != null) {
            // Find the room with the same ID as the player's current room
            String currentRoomId = player.getCurrentRoom().getId();
            Room newRoom = roomMap.get(currentRoomId);
            if (newRoom != null) {
                player.setCurrentRoom(newRoom);
            }
        }
    }
    
    /**
     * Updates all NPC room references after room shuffling
     */
    public void updateNPCRooms(NPCManager npcManager) {
        if (npcManager != null) {
            for (NPC npc : npcManager.getNpcs()) {
                if (npc.getCurrentRoom() != null) {
                    String currentRoomId = npc.getCurrentRoom().getId();
                    Room newRoom = roomMap.get(currentRoomId);
                    if (newRoom != null) {
                        // Remove from old room and add to new room
                        npc.getCurrentRoom().removeNPC(npc);
                        newRoom.addNPC(npc);
                        npc.setCurrentRoom(newRoom);
                    }
                }
            }
        }
    }
    
    /**
     * Complete room shuffling process that updates all references
     */
    public void shuffleRoomsComplete(Player player, NPCManager npcManager) {
        shuffleRooms();
        updatePlayerRoom(player);
        updateNPCRooms(npcManager);
    }
}
