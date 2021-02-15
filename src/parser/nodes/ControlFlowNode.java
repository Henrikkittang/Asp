package parser.nodes;

import parser.nodes.BaseNode;
import scanner.Token;

public class ControlFlowNode extends BaseNode {

    public ControlFlowNode(Token token){
        super(token);
    }

    public String toString(){ return token.toString(); }
}
