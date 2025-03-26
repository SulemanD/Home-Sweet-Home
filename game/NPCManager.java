package game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class NPCManager {
    private List<NPC> npcs;  // Instance variable

    // Constructor
    public NPCManager() {
        npcs = new ArrayList<>();
    }

    public void loadNPCs() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<NPC> npcList = objectMapper.readValue(new File("data/npcs.json"), new TypeReference<List<NPC>>() {});

            // Process each NPC object and create the corresponding NPC with behavior
            for (NPC npc : npcList) {
                String id = npc.getId();
                String name = npc.getName();
                Room currentRoom = npc.getCurrentRoom();

                // Handle behavior based on type and create the appropriate behavior object
                Behavior behavior = null;
                if ("riddle".equalsIgnoreCase(npc.getBehavior().getType())) {
                    behavior = createRiddleBehavior(npc.getBehavior()); // Create the RiddleBehavior
                } else if ("quest".equalsIgnoreCase(npc.getBehavior().getType())) {
                    behavior = createQuestBehavior(npc.getBehavior()); // Create the QuestBehavior
                }

                // Create a new NPC object with the behavior and add it to the list
                NPC newNpc = new NPC(id, name, currentRoom, behavior);
                npcs.add(newNpc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Behavior createRiddleBehavior(Behavior behavior) {
        // Ensure that the behavior passed contains the required properties for RiddleBehavior
        // You can cast behavior to RiddleBehavior and initialize it properly
        if (behavior instanceof RiddleBehavior) {
            RiddleBehavior riddleBehavior = (RiddleBehavior) behavior;
            return new RiddleBehavior(riddleBehavior.getMaxAttempts());
        }
        return null;
    }

    private Behavior createQuestBehavior(Behavior behavior) {
        // Handle the creation of QuestBehavior
        return new QuestBehavior(behavior.getQuestItem());
    }

    // Get the list of NPCs
    public List<NPC> getNpcs() {
        return npcs;
    }
}