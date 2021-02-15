package parser.nodes;

import parser.nodes.BaseNode;
import scanner.Token;

public class CompOpNode extends BaseNode {
    public BaseNode leftNode;
    public BaseNode rightNode;

    public CompOpNode(BaseNode leftNode, Token operatioToken, BaseNode rightNode){
        super(operatioToken);
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public String toString(){
        return "(" + leftNode + " " + token + " " + rightNode + ")";
    }
}
