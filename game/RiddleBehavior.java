package game;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiddleBehavior extends Behavior{

    private List<Riddle> riddles;
    private int maxAttempts;
    private int attempts;
    private Random random; 

    private Message message;
    private RoomManager roomManager;
    private Scanner scanner;

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
        this.riddles = riddles != null ? riddles : new ArrayList<>();
        this.attempts = 0;
        this.random = new Random();
        this.roomManager = new RoomManager();
        this.message = new Message("data/messages.json");
        scanner = new Scanner(System.in);
        if (riddles == null) {
            loadRiddlesFromJson();
        }
    }

    // Keep the existing constructor for programmatic creation
    public RiddleBehavior(int maxAttempts){
        super("riddle", maxAttempts, "Solve the riddle" , null);
        loadRiddlesFromJson();
        this.attempts = 0;
        this.random = new Random();
        this.roomManager = new RoomManager();
        this.message = new Message("data/messages.json");
        scanner = new Scanner(System.in);
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

    public void onSuccess(){
        System.out.println(message.getMessage("riddle_correct"));
    }
    
    public void onFail(){
        System.out.println(message.getMessage("riddle_incorrect"));
    }

    public void askRiddle(){
        Riddle currentRiddle = riddles.get(random.nextInt(riddles.size()));
        System.out.println(message.getMessage("riddle_question") + currentRiddle.getQuestion());

        // Use the new isCorrectAnswer method instead of direct string comparison
        String userAnswer = scanner.nextLine().toLowerCase();
        if (currentRiddle.isCorrectAnswer(userAnswer)){
            onSuccess();
        }
        else {
            onFail();
            attempts++;
            if (attempts == maxAttempts){onMaxAttempts();}
        }
    }

    public void onMaxAttempts() {
        // Implementation here
        System.out.println("Max attempts reached");
        System.out.println(message.getMessage("benared_item_loss"));
        System.out.println(message.getMessage("floor_shuffled"));
        roomManager.shuffleRooms();
        roomManager.shuffleItems();
    }
}
