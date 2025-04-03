package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NPCManager {
    private List<NPC> npcs;
    private static NPCManager instance;
    private RoomManager roomManager;
    private Player player;
    
    // Constructor
    public NPCManager(RoomManager roomManager, Player player) {
        npcs = new ArrayList<>();
        instance = this;
        this.roomManager = roomManager;
        this.player = player;
        loadNPCs();
    }

    // Static method to get instance (singleton pattern)
    public static NPCManager getInstance() {
        return instance;
    }

    // Static method to get an NPC by ID
    public static NPC getNpcById(String id) {
        if (instance == null || instance.npcs == null) {
            return null;
        }
        
        for (NPC npc : instance.npcs) {
            if (npc.getId().equals(id)) {
                return npc;
            }
        }
        return null;
    }

    public void moveNpc() {
        for (NPC npc : instance.npcs) {
            if (npc.canMove()) {
                npc.moveAround();
            }
        }
    }

    public void interactWithPlayer() {
        for (NPC npc : npcs) {
            if (npc.getCurrentRoom() == player.getCurrentRoom()) {
                npc.getBehavior().interact(player);
            }
        }
    }

    public void loadNPCs() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load NPCs from JSON
            List<NPC> npcList = objectMapper.readValue(new File("data/npcs.json"), new TypeReference<List<NPC>>() {});

            for (NPC npc : npcList) {
                // Resolve the room ID to an actual Room object
                npc.resolveRoom(roomManager);

                // Add the NPC to the room's NPC list
                if (npc.getCurrentRoom() != null) {
                    npc.getCurrentRoom().addNPC(npc);
                }

                // Handle behavior setup
                if (npc.getBehavior() instanceof RiddleBehavior) {
                    RiddleBehavior riddleBehavior = (RiddleBehavior) npc.getBehavior();
                    riddleBehavior.loadRiddlesFromJson(); // Ensure riddles are loaded from riddles.json
                }

                // Add the NPC to our list
                npcs.add(npc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get the list of NPCs
    public List<NPC> getNpcs() {
        return npcs;
    }

    public void decaCooldownForRiddleBehaviors() {
        for (NPC npc : npcs) {
            if (npc.getBehavior() instanceof RiddleBehavior) {
                RiddleBehavior riddleBehavior = (RiddleBehavior) npc.getBehavior();
                riddleBehavior.decaCooldown();
            }
        }
    }
}