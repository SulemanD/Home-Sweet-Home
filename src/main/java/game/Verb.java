package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class Verb {

    private String input;
    private String verb;
    private String target;
    private String npcTarget;
    private Player player;
    private NPCManager npcManager;

    private Room currentRoom;
    private Message message;
    private List<Item> itemsList;
    private Map<String, Item> items = new HashMap<>();
    
    public Verb(String input, Room currentRoom, Player player, NPCManager npcManager) {
        this.input = input.trim().toLowerCase();
        this.currentRoom = currentRoom;
        parseInput(input);
        this.player = player;
        this.message = new Message("data/messages.json");
        loadItems();
        this.npcManager = npcManager;
    }

    public void loadItems() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            itemsList = objectMapper.readValue(new File("data/items.json"), 
                TypeFactory.defaultInstance().constructCollectionType(List.class, Item.class));

            for (Item item : itemsList) {
                items.put(item.getId(), item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseInput(String input) {
        input = input.trim().toLowerCase();
        String[] parts = input.split("\\s+");

        if (parts.length == 0) return;

        Set<String> ignoreWords = new HashSet<>(Arrays.asList("the", "a", "an"));

        
        List<String> filteredParts = new ArrayList<>();
        for (String part : parts) {
            if (!ignoreWords.contains(part)) {
                filteredParts.add(part);
            }
        }

        if (filteredParts.isEmpty()) return;

        this.verb = filteredParts.get(0); 

        switch (verb) {
            case "take":
            case "look":
            case "talk":
                if (filteredParts.size() > 1) {
                    this.target = String.join(" ", filteredParts.subList(1, filteredParts.size())).trim();
                }
                break;
            case "give":
                int giveIndex = filteredParts.indexOf("give");
                int toIndex = filteredParts.indexOf("to");

                if (giveIndex == 0 && toIndex > giveIndex && toIndex < filteredParts.size() - 1) {
                    this.target = String.join(" ", filteredParts.subList(1, toIndex)).trim();

                    this.npcTarget = String.join(" ", filteredParts.subList(toIndex + 1, filteredParts.size())).trim();
                } else {
                    System.out.println("Invalid format. Use: give <item> to <character>");
                }
                break;
            case "go":
                if (filteredParts.size() > 1) {
                    this.target = filteredParts.get(1).trim();
                }
                break;
            case "help":
                break;
            default:
                this.verb = null; 
        }
    }

    private String resolveNpcId(String npcName) {
        List<NPC> allNpcs = npcManager.getNpcs();
        for (NPC npc : allNpcs) {
            if (npc.getName().equalsIgnoreCase(npcName) || npc.getName().toLowerCase().contains(npcName.toLowerCase())) {
                return npc.getId();
            }
        }
        return null; 
    }

    public void doAction() {
        if (verb == null) {
            System.out.println("Invalid command, type 'help' for a list of commands");
            return;
        }
        npcManager.moveNpc();

        Set<String> validVerbs = new HashSet<>(Arrays.asList(
            "take", "give", "go", "look", "talk", "help"
        ));

        if (!validVerbs.contains(verb)) {
            System.out.println("Invalid command, type 'help' for a list of commands");
            return;
        }

        switch (verb) {
            case "take":
                takeAction();
                break;
            case "give":
                giveAction();
                break;
            case "go":
                goAction();
                break;
            case "look":
                lookAction();
                break;
            case "talk":
                talkAction();
                break;
            case "help":
                System.out.println("List of commands: \n" +
                "take <item> - take an item\n" +
                "give <item> to <character> - give an item\n" +
                "go <direction> - go in a direction\n" +
                "look <item> - look at an item\n" +
                "talk <character> - talk to a character\n" +
                "help - list of commands\n");
                break;
        }

        npcManager.interactWithPlayer();
        npcManager.decaCooldownForRiddleBehaviors();
    }

    public String getVerb() {
        return verb;
    }

    public void takeAction() {
        if (target == null || target.trim().isEmpty()) {
            System.out.println("What item do you want to take?");
            return;
        }

        Item itemToTake = null;
        for (Item item : currentRoom.getItems()) {
            if (item.getName().equalsIgnoreCase(target)) {
                itemToTake = item;
                break;
            }
        }

        if (itemToTake == null) {
            System.out.println("There is no " + target + " to take.");
            return;
        }

        if (itemToTake.isPortable()) {
            player.addItemToInventory(itemToTake);
            System.out.println("You take the " + itemToTake.getName() + ".");
            currentRoom.removeItems(itemToTake);
        } else {
            System.out.println("You cannot take the " + itemToTake.getName() + ".");
        }
    }

    public void giveAction() {
        if (target == null || npcTarget == null) {
            System.out.println("Give what to whom?");
            return;
        }

        Item itemToGive = null;
        for (Item item : player.getInventory()) {
            if (item.getName().equalsIgnoreCase(target)) {
                itemToGive = item;
                break;
            }
        }

        if (itemToGive == null) {
            System.out.println(message.getMessage("give_fail"));
            return;
        }

        NPC recipient = null;
        for (NPC npc : getNPCsInCurrentRoom()) {
            if (npc.getName().equalsIgnoreCase(npcTarget) || npc.getName().toLowerCase().contains(npcTarget.toLowerCase())) {
                recipient = npc;
                break;
            }
        }

        if (recipient == null) {
            System.out.println(message.getMessage("ghost_wrong"));
            return;
        }
      
        if (!itemToGive.getCanGiveTo().contains(recipient.getId())) {
            System.out.println(message.getMessage("ghost_wrong"));
            return;
        }

        player.removeItemFromInventory(itemToGive);
        itemToGive.disableItem();
        System.out.println(message.getMessage("give_success")
            .replace("{item}", itemToGive.getName())
            .replace("{npc}", recipient.getName()));

       
        if (recipient.getBehavior() instanceof QuestBehavior) {
            ((QuestBehavior) recipient.getBehavior()).onReceiveItem(itemToGive);
        }
    }

    public void goAction() {
        if (target == null || target.trim().isEmpty()) {
            System.out.println("Where do you want to go?");
            return;
        }
        Room nextRoom = null;

        if (target.contains("north") && (nextRoom = currentRoom.getExits().getNorthRoom()) != null) {
            player.setCurrentRoom(nextRoom);
        } else if (target.contains("south") && (nextRoom = currentRoom.getExits().getSouthRoom()) != null) {
            player.setCurrentRoom(nextRoom);
        } else if (target.contains("up") && (nextRoom = currentRoom.getExits().getUpRoom()) != null) {
            player.setCurrentRoom(nextRoom);
        } else if (target.contains("down") && (nextRoom = currentRoom.getExits().getDownRoom()) != null) {
            player.setCurrentRoom(nextRoom);
        } else if (target.contains("east") && (nextRoom = currentRoom.getExits().getEastRoom()) != null) {
            player.setCurrentRoom(nextRoom);
        } else if (target.contains("west") && (nextRoom = currentRoom.getExits().getWestRoom()) != null) {
            player.setCurrentRoom(nextRoom);
        } else {
            System.out.println(message.getMessage("no_exit"));
            return;
        }

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("currentRoom", nextRoom.getShortDesc());
        System.out.println(message.getFormattedMessage("move_success", placeholders));
    }

    public void lookAction() {
        if (target != null) {
            String normalizedTarget = target.toLowerCase().trim();
            for (Item item : currentRoom.getItems()) {
                if (item.getName().toLowerCase().equals(normalizedTarget)) {
                    System.out.println(item.getDescription());
                    return;
                }
            }
            System.out.println("You don't see that here.");
            return;
        }

        System.out.println(currentRoom.getLongDesc());
        List<Item> roomItems = currentRoom.getItems();
        if (!roomItems.isEmpty()) {
            System.out.println("\nYou see the following items:");
            for (Item item : roomItems) {
                System.out.println("- " + item.getName());
            }
        }

        List<NPC> npcsInRoom = currentRoom.getNpcs();
        if (!npcsInRoom.isEmpty()) {
            System.out.println("\nYou see the following people:");
            for (NPC npc : npcsInRoom) {
                System.out.println("- " + npc.getName());
            }
        } else {
            System.out.println("\nThere are no characters here.");
        }
    }

    private List<NPC> getNPCsInCurrentRoom() {
        List<NPC> npcsInRoom = new ArrayList<>();
        NPCManager npcManager = NPCManager.getInstance();
        if (npcManager == null) {
            System.out.println("Warning: NPC Manager not initialized");
            return npcsInRoom;
        }

        List<NPC> allNpcs = npcManager.getNpcs();
        if (allNpcs != null) {
            for (NPC npc : allNpcs) {
                if (npc.getCurrentRoom() != null && npc.getCurrentRoom().equals(currentRoom)) {
                    npcsInRoom.add(npc);
                }
            }
        }

        return npcsInRoom;
    }

    public void talkAction() {
        if (target == null) {
            System.out.println("Who do you want to talk to?");
            return;
        }

        List<NPC> npcsInRoom = getNPCsInCurrentRoom();
        if (npcsInRoom == null || npcsInRoom.isEmpty()) {
            System.out.println("There's nobody here to talk to.");
            return;
        }

        NPC targetNPC = null;
        for (NPC npc : npcsInRoom) {
            if (npc.getName().toLowerCase().contains(target.toLowerCase())) {
                targetNPC = npc;
                break;
            }
        }

        if (targetNPC == null) {
            System.out.println("You don't see anyone by that name here.");
            return;
        }

        Behavior behavior = targetNPC.getBehavior();
        if (behavior != null) {
            System.out.println(targetNPC.getName() + " turns to face you.");
            if ("riddle".equalsIgnoreCase(behavior.getType())) {
                try {
                    System.out.println(targetNPC.getName() + " says: \"I have more riddles for you, don't worry.\"");
                } catch (Exception e) {
                    System.out.println("The " + targetNPC.getName() + " seems confused and doesn't say anything.");
                }
            } else if ("quest".equalsIgnoreCase(behavior.getType())) {
                System.out.println(targetNPC.getName() + " says: \"I need your help with something.\"");
                System.out.println(message.getMessage("quest_question"));
            } else {
                System.out.println(targetNPC.getName() + " says: \"Hello there.\"");
            }
        } else {
            System.out.println(targetNPC.getName() + " doesn't seem interested in talking.");
        }
    }
}

