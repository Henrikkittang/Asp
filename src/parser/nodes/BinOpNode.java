package parser.nodes;

import parser.nodes.BaseNode;
import scanner.Token;

public class BinOpNode extends BaseNode {
    public BaseNode leftNode;
    public BaseNode rightNode;

    public BinOpNode(BaseNode leftNode, Token operationToken, BaseNode rightNode){
        super(operationToken);
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public String toString(){
        return "(" + leftNode + " " + token + " " + rightNode + ")";
    }
}
