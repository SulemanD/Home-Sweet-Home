package game;

import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NPC {
    private String id;
    private String name;
    private Room currentRoom;
    private Behavior behavior;
    private String questItem;
    private Random random;

    // Constructor-based injection with @JsonProperty
    @JsonCreator
    public NPC(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("currentRoom") Room currentRoom,
        @JsonProperty("behavior") Behavior behavior
    ) {
        this.id = id;
        this.name = name;
        this.currentRoom = currentRoom;
        this.behavior = behavior;
        this.questItem = behavior.getQuestItem();
        this.random = new Random();
    }

    // No getters or setters are needed
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