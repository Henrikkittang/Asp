package errorHandler;

// Thrown if there are any issues with the reading of source files

public class  FileError extends Error{
    public FileError(String details, String filename, int row, int column){
        super("File Error", details, filename, row, column);
    }
}
