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
        NPCManager npcManager = new NPCManager(roomManager, player); // Pass roomManager and player to NPCManager

        Scanner scanner = new Scanner(System.in);
        System.out.println("The wind brushes against your face, and as you open your eyes the light blinds you...");
        System.out.println("Slowly but surely you stand up, covered in a mysterious substance, your clothes torn and wet...");
        System.out.println("In the horizon you see an old victorian house, covered in cobwebs, with nothing to lose you make the trek in the pouring rain...");
        System.out.println("After what feels like an hour, you reach the house. As you open the creeky door, the doorknob falls off and you step in...");
        System.out.println("... ~ ...");
        System.out.println("... ~ ...");
        System.out.println("... ~ ...");
        System.out.println("You wake up in a " + player.getCurrentRoom().getName() + " unsure of where you are. The darkness consumes you...");
        System.out.println("... ~ ...");
        System.out.println("... ~ ...");
        System.out.println("... ~ ...");
        System.out.println("An unknown voice speaks to you 'How did you sleep?'...'Do you want some HELP'?");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing!");
                break;
            }

            Verb verb = new Verb(input, player.getCurrentRoom(), player, npcManager);
            verb.doAction();
        }

        scanner.close();
    }

