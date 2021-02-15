package parser.nodes;

import scanner.Token;

public class ListAccessNode extends BaseNode{
    public int index;

    public ListAccessNode(Token token, Long index){
        super(token);
        this.index = index.intValue();
    }

    public String variableName(){ return token.variableName; }
    public String toString(){
        return token + "[" + index + "]";
    }
}
