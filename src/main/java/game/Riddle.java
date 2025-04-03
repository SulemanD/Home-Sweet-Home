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
        
        if (answers != null && !answers.isEmpty()) {
            this.answer = answers.get(0);
        } else {
            this.answer = "";
        }
    }
    
    
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

