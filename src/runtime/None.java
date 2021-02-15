package runtime;

import scanner.Token;

public class None extends RuntimeValue{
    public None(Token token){
        super(token.linePos, token.rowPos);
    }

    public String toString(){ return "None"; }
}
