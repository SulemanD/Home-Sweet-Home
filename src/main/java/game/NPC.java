package game;

import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NPC {
    private String id;
    private String name;
    private Room currentRoom;
    private String roomId; 
    private Behavior behavior;
    private String questItem;
    private Random random;
    private boolean canMove;

    
    @JsonCreator
    public NPC(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("currentRoom") String roomId,
        @JsonProperty("behavior") Behavior behavior,
        @JsonProperty("canMove") boolean canMove,
        @JsonProperty("questItem") String questItem) {
        this.id = id;
        this.name = name;
        this.roomId = roomId; 
        this.behavior = behavior;
        this.questItem = behavior.getQuestItem();
        this.random = new Random();
        this.canMove = canMove;
    }
    


    
    public void resolveRoom(RoomManager roomManager) {
        if (roomId != null && currentRoom == null) {
            this.currentRoom = roomManager.getRoom(roomId);
            if (this.currentRoom != null) {
                this.currentRoom.addNPC(this); 
            }
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
    public boolean canMove() {
        return this.canMove;
    }

    public void moveAround() {

        if (canMove) {
        List<Room> nextRoom = currentRoom.getConnectedRooms();
        Room newRoom = nextRoom.get(random.nextInt(nextRoom.size()));
            this.currentRoom.removeNPC(this);
            newRoom.addNPC(this);            
            this.currentRoom = newRoom;

        };
            
            
        }


}



