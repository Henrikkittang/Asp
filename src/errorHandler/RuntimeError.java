package errorHandler;

// Thrown if there are any errors during runtime
// Examples: division by zero, negative number raised to the power of exponent less than zero

public class RuntimeError extends Error{
    public RuntimeError(String details, String filename, int row, int column){
        super("Runtime error", details, filename, row, column);
    }
}
