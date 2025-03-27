package game;

import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NPC {
    private String id;
    private String name;
    private Room currentRoom;
    private String roomId; // New field to store the room ID from JSON
    private Behavior behavior;
    private String questItem;
    private Random random;

    // Constructor for Jackson deserialization
    @JsonCreator
    public NPC(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("currentRoom") String roomId,
        @JsonProperty("behavior") Behavior behavior
    ) {
        this.id = id;
        this.name = name;
        this.roomId = roomId; // Store the room ID string
        this.behavior = behavior;
        this.questItem = behavior.getQuestItem();
        this.random = new Random();
        // Room will be resolved later using RoomManager
    }
    
    // Constructor for direct object creation
    public NPC(String id, String name, Room currentRoom, Behavior behavior) {
        this.id = id;
        this.name = name;
        this.currentRoom = currentRoom;
        this.behavior = behavior;
        this.questItem = behavior.getQuestItem();
        this.random = new Random();
    }

    // Method to resolve room from ID
    public void resolveRoom(RoomManager roomManager) {
        if (roomId != null && currentRoom == null) {
            this.currentRoom = roomManager.getRoom(roomId);
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Behavior getBehavior() {
        return behavior;
    }
    public String getQuestItem() {
        return questItem;
    }

    public void moveAround() {
        // Implementation here
        List<Room> connectedRooms = currentRoom.getConnectedRooms();
        currentRoom = connectedRooms.get(random.nextInt(connectedRooms.size()));
    }
}