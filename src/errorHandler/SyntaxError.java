package errorHandler;

// Thrown by the parser if the order of tokens dosent make sense
// Examples: 1++2, ((1) - 4), ect.

public class SyntaxError extends Error{
    public SyntaxError(String details, String filename, int row, int column){
        super("Syntax error", details, filename, row, column);
    }}
