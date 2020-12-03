import model.ResponseMessage;

public class NumberGuessService {

    public static final Integer NUMBER_TO_GUESS = 65;

    public static ResponseMessage guessNumber(Integer number) {
       if(number < NUMBER_TO_GUESS) {
           return ResponseMessage.LOW;
       }
       if(number > NUMBER_TO_GUESS) {
           return ResponseMessage.HIGH;
       }
       return  ResponseMessage.EQUAL;
    }
}
