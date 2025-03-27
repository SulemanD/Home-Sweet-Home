package game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class NPCManager {
    private List<NPC> npcs;
    private static NPCManager instance;
    private RoomManager roomManager;
    
    // Constructor
    public NPCManager(RoomManager roomManager) {
        npcs = new ArrayList<>();
        instance = this;
        this.roomManager = roomManager;
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

    public void loadNPCs() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Load NPCs from JSON - this will use the new constructor that accepts roomId as String
            List<NPC> npcList = objectMapper.readValue(new File("data/npcs.json"), new TypeReference<List<NPC>>() {});
            
            for (NPC npc : npcList) {
                // Resolve the room ID to an actual Room object
                npc.resolveRoom(roomManager);
                
                // Handle behavior setup if needed
                if ("riddle".equalsIgnoreCase(npc.getBehavior().getType())) {
                    npc.getBehavior().setType("riddle");
                } else if ("quest".equalsIgnoreCase(npc.getBehavior().getType())) {
                    npc.getBehavior().setType("quest");
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
}