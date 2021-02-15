package errorHandler;

public class IndexError extends Error{
    public IndexError(String details, String filename, int row, int column){
        super("Index out of range", details, filename, row, column);
    }
}
