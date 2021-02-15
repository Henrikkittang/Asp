package errorHandler;

// Thrown if the indent level don't match the predefined tab length, 4 by default

public class IndentError extends Error{
    public IndentError(String details, String filename, int row, int column){
        super("Indent Error", details, filename, row, column);
    }
}
