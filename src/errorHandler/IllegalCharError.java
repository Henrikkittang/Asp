package errorHandler;

// Thrown if the scanner don't recognize the character(s) as a valid token

public class IllegalCharError extends Error {
    public IllegalCharError(String details, String filename, int row, int column){
        super("Illegal character", details, filename, row, column);
    }
}

