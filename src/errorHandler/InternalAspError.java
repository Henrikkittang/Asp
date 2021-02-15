package errorHandler;

// Thrown if any of the components doesn't behave as expected

public class InternalAspError extends Error{
    public InternalAspError(String details, String filename, int row, int column){
        super("Internal Asp error", details, filename, row, column);
    }
}
