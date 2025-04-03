package game;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Riddle {
    private String question;
    private String answer;
    private List<String> answers;
    
    @JsonCreator
    public Riddle(
        @JsonProperty("question") String question,
        @JsonProperty("answers") List<String> answers) {
        this.question = question;
        this.answers = answers;
        
        // Set the first answer as the primary answer if available
        if (answers != null && !answers.isEmpty()) {
            this.answer = answers.get(0);
        } else {
            this.answer = "";
        }
    }
    
    // Default constructor for direct creation
    public Riddle() {
    }

    public String getQuestion() {
        return this.question;
    }
    
    public String getAnswer() {
        return this.answer;
    }
    
    public List<String> getAnswers() {
        return this.answers;
    }
    
    // Check if a provided answer matches any valid answer
    public boolean isCorrectAnswer(String userAnswer) {
        if (answers != null) {
            for (String validAnswer : answers) {
                if (validAnswer.equalsIgnoreCase(userAnswer.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}

