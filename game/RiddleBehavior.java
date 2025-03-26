package game;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RiddleBehavior extends Behavior{

    private List<Riddle> riddles;
    private int maxAttempts;
    private int attempts;
    private Random random; 

    private Message message;
    private RoomManager roomManager;
    private Scanner scanner;

    

    public RiddleBehavior(int maxAttempts){
        super("riddle", maxAttempts, "Solve the riddle" , null);
        loadRiddlesFromJson();
        this.attempts = 0;
        this.random = new Random();
        this.roomManager = new RoomManager();
        this.message = new Message("../data/messages.json");
        scanner = new Scanner(System.in);
        
    }
    
    public void loadRiddlesFromJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.riddles = objectMapper.readValue(new File("../data/riddles.json"),  objectMapper.getTypeFactory().constructCollectionType(List.class, Riddle.class));
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

        if (scanner.nextLine().toLowerCase().equals(currentRiddle.getAnswer())){
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
