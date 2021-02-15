package parser.nodes;

import parser.nodes.BaseNode;
import scanner.Token;

public class PrimitiveNode extends BaseNode {

    public PrimitiveNode(Token token){
        super(token);
    }

    public String toString(){
        return token.toString();
    }
}
