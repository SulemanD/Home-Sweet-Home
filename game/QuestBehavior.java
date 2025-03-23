package game;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class QuestBehavior extends Behavior{

    private List<Riddle> riddles;
    private int maxAttempts;
    private int attempts;
    private Random random; 

    private Message message;
    private Scanner scanner;

    

    public QuestBehavior(List<Item> items){
        super("quest", null, null);
        this.random = new Random();
        this.message = new Message("../data/messages.json");
        scanner = new Scanner(System.in);
    }

    public void onSuccess(){
        System.out.println(message.getMessage("ghost_correct"));
    }

    public void onFail(){
        System.out.println(message.getMessage("ghost_wrong"));
    }


    
}
