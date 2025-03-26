package game;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Verb {

    private String input;
    private String verb;
    private String target;
    private Player player;

    private Room currentRoom;
    private Message message;

    private Map<String, Item> items = new HashMap<>();
    
 
    

    public Verb(String input, Room currentRoom, Player player){
        this.input = input.trim().toLowerCase();
        this.currentRoom = currentRoom;
        parseInput(input);
        this.player = player;
        this.message = new Message("../data/messages.json");
    }

    public void loadItems(){
        try{

            ObjectMapper objectMapper = new ObjectMapper();
            items = objectMapper.readValue(new File("../data/items.json"), new TypeReference<Map<String, Item>>() {});

        } catch (IOException e){
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
            case "help":
                System.out.println("List of commands: \n" +
                "take <item> - take an item\n" +
                "give <item> to <character> - give an item\n" +
                "go <direction> - go in a direction\n" +
                "look <item> - look at an item\n" +
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
        if(target.contains("north") && player.getCurrentRoom().getExits().getNorth() != null){
            //go north
            player.setCurrentRoom(currentRoom.getExits().getNorth());
            System.out.println(message.getMessage("move_sucess"));
        }
        else if(target.contains("south") && player.getCurrentRoom().getExits().getSouth() != null){
            //go south
            player.setCurrentRoom(currentRoom.getExits().getSouth());
        }
        else if(target.contains("up") && player.getCurrentRoom().getExits().getUp() != null){
            //go up
            player.setCurrentRoom(currentRoom.getExits().getUp());
        }
        else if(target.contains("down") && player.getCurrentRoom().getExits().getDown() != null){
            //go down
            player.setCurrentRoom(currentRoom.getExits().getDown());
        }
        else if(target.contains("east") && player.getCurrentRoom().getExits().getEast() != null){
            //go east
            player.setCurrentRoom(currentRoom.getExits().getEast());
        }
        else if(target.contains("west") && player.getCurrentRoom().getExits().getWest() != null){
            //go west
            player.setCurrentRoom(currentRoom.getExits().getWest());
        }
        else{
            System.out.println(message.getMessage("no_exit"));
        }
    }

    public void lookAction(){
        //look at target
        if(items.containsKey(target) && currentRoom.getItems().contains(items.get(target))){
            System.out.println(items.get(target).getDescription());
        }
        else{
            System.out.println(message.getMessage(currentRoom.getLongDesc()));
        }
    }

    


    

}

