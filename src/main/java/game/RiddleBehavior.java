package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiddleBehavior extends Behavior{

    int COOL_DOWN_TIME = 3; // Cooldown time in seconds

    private List<Riddle> riddles;
    private int maxAttempts;
    private int attempts;
    private Random random; 
    private int cooldown;

    private Message message;
    private RoomManager roomManager;
    private Scanner scanner;
    private Player player;
    private NPCManager npcManager;

    // Add JsonCreator constructor for Jackson deserialization from JSON
    @JsonCreator
    public RiddleBehavior(
        @JsonProperty("type") String type,
        @JsonProperty("maxAttempts") int maxAttempts,
        @JsonProperty("objective") String objective,
        @JsonProperty("questItem") String questItem,
        @JsonProperty("riddles") List<Riddle> riddles) {
        super(type, maxAttempts, objective, questItem);
        this.maxAttempts = maxAttempts;
        this.riddles = new ArrayList<>(); // Initialize empty list
        this.attempts = 0;
        this.random = new Random();
        this.roomManager = null; // Will be set later
        this.message = new Message("data/messages.json");
        scanner = new Scanner(System.in);
        loadRiddlesFromJson(); // Always load riddles from riddles.json
        this.cooldown = 0;
    }

    // Keep the existing constructor for programmatic creation
    public RiddleBehavior(int maxAttempts){
        super("riddle", maxAttempts, "Solve the riddle" , null);
        loadRiddlesFromJson();
        this.attempts = 0;
        this.random = new Random();
        this.roomManager = null; // Will be set later
        this.message = new Message("data/messages.json");
        scanner = new Scanner(System.in);
    }
    
    /**
     * Set the RoomManager, Player, and NPCManager references
     */
    public void setGameReferences(RoomManager roomManager, Player player, NPCManager npcManager) {
        this.roomManager = roomManager;
        this.player = player;
        this.npcManager = npcManager;
    }
    
    public void loadRiddlesFromJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // First parse the JSON structure to get the "riddles" array
            JsonNode rootNode = objectMapper.readTree(new File("data/riddles.json"));
            JsonNode riddlesNode = rootNode.get("riddles");
            
            // Then convert the "riddles" array to a List of Riddle objects
            this.riddles = objectMapper.convertValue(riddlesNode, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, Riddle.class));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void decaCooldown(){
        if (cooldown > 0) {cooldown--;}
    }

    public void onSuccess(){
        System.out.println(message.getMessage("riddle_correct"));
        cooldown = 3;
    }
    
    public void onFail(){
        System.out.println(message.getMessage("riddle_incorrect"));
    }

    public void interact(Player player){
        try {
            Thread.sleep(1000); // Pause for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        askRiddle();
    }

    public void askRiddle() {
        if (cooldown == 0) {
            if (riddles.isEmpty()) {
                System.out.println("No riddles available.");
                return;
            }
            Riddle currentRiddle = riddles.get(random.nextInt(riddles.size())); // Randomize riddle

            // Prepare placeholders for the riddle question
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("riddle", currentRiddle.getQuestion());

            // Format and display the riddle question
            System.out.println(message.getFormattedMessage("riddle_question", placeholders));

            // Get the user's answer and validate it
            String userAnswer = scanner.nextLine().trim();
            while (!currentRiddle.isCorrectAnswer(userAnswer)) {
                onFail();
                attempts++;
                if (attempts == maxAttempts) {
                    onMaxAttempts();
                    return;
                }
                userAnswer = scanner.nextLine().trim();
            }
            onSuccess();
        }
    }

    public void onMaxAttempts() {
       
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("item", "some item"); 

    
        System.out.println(message.getFormattedMessage("bernard_item_loss", placeholders));
        System.out.println(message.getMessage("rooms_shuffled"));
        cooldown = COOL_DOWN_TIME;
        attempts = 0;

        // Use the complete shuffling method that updates all references
        if (roomManager != null) {
            roomManager.shuffleRoomsComplete(player, npcManager);
        }
    }

    public Riddle getCurrentRiddle() {
        if (riddles != null && !riddles.isEmpty()) {
            return riddles.get(0);
        }
        return null;
    }
}
