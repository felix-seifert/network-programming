package model;

public enum ResponseMessage {
    LOW("That's too low. Please guess higher."),
    HIGH("That's too high. Please guess lower."),
    EQUAL("Correct guess. The number of guesses you took are ");

    public final String label;

    private ResponseMessage(String label) {
        this.label = label;
    }
}
