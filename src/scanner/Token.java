package scanner;

import static scanner.TokenTypes.*;

public class Token<T> {
    public T value;
    public String variableName = null;
    public TokenTypes token_type;

    public final int rowPos;
    public final int linePos;

    public Token(TokenTypes token_type, int rowPos, int linePos){
        this.token_type = token_type;
        this.rowPos = rowPos;
        this.linePos = linePos;
    }

    public String toString(){
        String temp = token_type.toString();

        if(token_type == VARIABLE){
            temp += ": " + variableName;
        }else if(token_type == FLOAT || token_type == INT || token_type == BOOL){
            temp += ": " + value;
        }

        // temp += " | at line " + linePos + ", row " + rowPos;
        return temp;
    }
}
