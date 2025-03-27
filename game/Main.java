package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        // Load the game data
        RoomManager roomManager = new RoomManager();
        Player player = new Player(roomManager.getRoom("basement"));
        NPCManager npcManager = new NPCManager(roomManager); // Pass roomManager to NPCManager
        npcManager.loadNPCs();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Adventure Game! Type 'help' for commands.");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing!");
                break;
            }

            Verb verb = new Verb(input, player.getCurrentRoom(), player);
            verb.doAction();
        }

        scanner.close();
    } 
    
}