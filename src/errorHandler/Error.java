package errorHandler;

public class Error {
    private final String errorName;
    private final String details;
    private final String filename;
    private final int row;
    private final int column;

    public Error(String errorName, String details, String filename, int row, int column){
        this.errorName = errorName;
        this.details = details;
        this.filename = filename;
        this.row = row;
        this.column = column;
    }

    public String toString() {
        return "\n" + errorName + " in " + filename+":"+column+":"+row + " at line " + column + ", row " + row + ". \n \t" + details;
    }
}


