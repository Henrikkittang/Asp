package runtime;

import scanner.Token;
import scanner.TokenTypes;

public class ControlFlowType extends RuntimeValue{

    boolean isContinue;
    public ControlFlowType(Token token){
        super(token.linePos, token.rowPos);
        isContinue = token.token_type == TokenTypes.CONTINUE;
    }

    public String toString(){ return isContinue ? "continue" : "break"; }
}
