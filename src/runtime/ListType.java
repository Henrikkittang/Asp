package runtime;

import scanner.Token;

import java.util.ArrayList;
import java.util.List;

public class ListType extends RuntimeValue{
    public List<RuntimeValue> elements = new ArrayList<>();

    public ListType(Token token){
        super(token.linePos, token.rowPos);
    }

    public String toString(){
        return "" + elements;
    }
}
