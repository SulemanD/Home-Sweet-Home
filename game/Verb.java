package game;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class Verb {

    private String input;
    private String verb;
    private String target;
    private Player player;

    private Room currentRoom;
    private Message message;
    private List<Item> itemsList;
    private Map<String, Item> items = new HashMap<>();
    
    public Verb(String input, Room currentRoom, Player player){
        this.input = input.trim().toLowerCase();
        this.currentRoom = currentRoom;
        parseInput(input);
        this.player = player;
        this.message = new Message("data/messages.json");
        loadItems(); // Add this line to load items when Verb is constructed
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

    public void parseInput(String input){
        String [] parts = this.input.split(" ");
        if (parts.length >0){
            this.verb = parts[0];
        }
        if (parts.length > 1){
            this.target = input.substring(input.indexOf(parts[1]));
        }
    }

    public void doAction(){
        switch(verb){
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
            default:
                System.out.println("Invalid command, type 'help' for a list of commands");
                break;
        }
    }

    public String getVerb(){
        return verb;
    }

    public void takeAction(){
        //add target to inventory
        if(items.containsKey(target) && currentRoom.getItems().contains(items.get(target)) && items.get(target).isPortable()){
            player.addItemToInventory(items.get(target));
            System.out.println(message.getMessage("take_sucess"));
        }
        else{
            System.out.println(message.getMessage("take_fail"));
        }
    }

    public Item giveAction(){
        //remove target from inventory
        if(items.containsKey(target)==true && player.getInventory().contains(items.get(target))){
            if (items.get(target).useItem(target)){}
            player.removeItemFromInventory(items.get(target));
            System.out.println(message.getMessage("give_sucess"));
            items.get(target).disableItem();
            return items.get(target);
        }
        else{
            System.out.println(message.getMessage("give_fail"));
            player.removeItemFromInventory(items.get(target));
            items.get(target).disableItem();
            return null;
        }
    }

    public void goAction(){
        if(target.contains("north") && player.getCurrentRoom().getExits().getNorthRoom() != null){
            //go north
            player.setCurrentRoom(currentRoom.getExits().getNorthRoom());
            System.out.println(message.getMessage("move_sucess"));
        }
        else if(target.contains("south") && player.getCurrentRoom().getExits().getSouthRoom() != null){
            //go south
            player.setCurrentRoom(currentRoom.getExits().getSouthRoom());
        }
        else if(target.contains("up") && player.getCurrentRoom().getExits().getUpRoom() != null){
            //go up
            player.setCurrentRoom(currentRoom.getExits().getUpRoom());
        }
        else if(target.contains("down") && player.getCurrentRoom().getExits().getDownRoom() != null){
            //go down
            player.setCurrentRoom(currentRoom.getExits().getDownRoom());
        }
        else if(target.contains("east") && player.getCurrentRoom().getExits().getEastRoom() != null){
            //go east
            player.setCurrentRoom(currentRoom.getExits().getEastRoom());
        }
        else if(target.contains("west") && player.getCurrentRoom().getExits().getWestRoom() != null){
            //go west
            player.setCurrentRoom(currentRoom.getExits().getWestRoom());
        }
        else{
            System.out.println(message.getMessage("no_exit"));
        }
    }

    public void lookAction(){
        // If a target is specified, look at that item
        if(target != null && items.containsKey(target) && currentRoom.getItems().contains(items.get(target))){
            System.out.println(items.get(target).getDescription());
        } 
        // Otherwise, look at the room
        else {
            // Display room description
            System.out.println(currentRoom.getLongDesc());
            
            // Display items in the room
            if (!currentRoom.getItems().isEmpty()) {
                System.out.println("\nYou see the following items:");
                for (Item item : currentRoom.getItems()) {
                    System.out.println("- " + item.getName());
                }
            }
            
            // Display NPCs in the room
            List<NPC> npcsInRoom = getNPCsInCurrentRoom();
            if (npcsInRoom != null && !npcsInRoom.isEmpty()) {
                System.out.println("\nYou see the following characters:");
                for (NPC npc : npcsInRoom) {
                    System.out.println("- " + npc.getName());
                }
            }
        }
    }
    
    private List<NPC> getNPCsInCurrentRoom() {
        List<NPC> npcsInRoom = new ArrayList<>();
        
        // Make sure NPCManager.getInstance() is not null
        NPCManager npcManager = NPCManager.getInstance();
        if (npcManager == null) {
            System.out.println("Warning: NPC Manager not initialized");
            return npcsInRoom;
        }
        
        // Make sure getNpcs() does not return null
        List<NPC> allNpcs = npcManager.getNpcs();
        if (allNpcs == null) {
            System.out.println("Warning: No NPCs loaded");
            return npcsInRoom;
        }
        
        // Now safely iterate through NPCs
        for (NPC npc : allNpcs) {
            if (npc != null && npc.getRoomId() != null && 
                npc.getRoomId().equals(currentRoom.getId())) {
                npcsInRoom.add(npc);
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
        
        // Find the NPC by name (case-insensitive comparison)
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
        
        // Handle different NPC behavior types
        Behavior behavior = targetNPC.getBehavior();
        if (behavior != null) {
            // Display the NPC's name for dialogue (since getDialogue isn't available)
            System.out.println(targetNPC.getName() + " turns to face you.");
            
            // Check behavior type
            if ("riddle".equalsIgnoreCase(behavior.getType())) {
                // For riddle-type NPCs, display the behavior information
                try {
                    System.out.println(targetNPC.getName() + " says: \"I have a riddle for you.\"");
                    System.out.println("Riddle: " + behavior.toString());
                    System.out.println("(Type your answer directly)");
                } catch (Exception e) {
                    System.out.println("The " + targetNPC.getName() + " seems confused and doesn't offer a riddle.");
                }
            } else if ("quest".equalsIgnoreCase(behavior.getType())) {
                // For quest-type NPCs, provide appropriate feedback
                System.out.println(targetNPC.getName() + " says: \"I need your help with something.\"");
                System.out.println("You sense " + targetNPC.getName() + " has a quest for you.");
            } else {
                // Generic dialogue
                System.out.println(targetNPC.getName() + " says: \"Hello there.\"");
            }
        } else {
            // Default dialogue
            System.out.println(targetNPC.getName() + " doesn't seem interested in talking.");
        }
    }
}

