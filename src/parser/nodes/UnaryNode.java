package parser.nodes;

import parser.nodes.BaseNode;
import scanner.Token;

public class UnaryNode extends BaseNode {
    public BaseNode numberNode;

    public UnaryNode(Token operationToken, BaseNode numberNode){
        super(operationToken);
        this.numberNode = numberNode;
    }

    public String toString(){
        return "";
    }
}
