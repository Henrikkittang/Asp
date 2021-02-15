package parser.nodes;

import scanner.Token;

public class VariableNode extends BaseNode {

    public VariableNode(Token token){
        super(token);
    }
    public String variableName(){ return token.variableName; }

    public String toString(){
        return token.toString();
    }
}
