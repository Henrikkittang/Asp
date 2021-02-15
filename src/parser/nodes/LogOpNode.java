package parser.nodes;


import parser.nodes.BaseNode;
import scanner.Token;

public class LogOpNode extends BaseNode {
    public BaseNode leftNode;
    public BaseNode rightNode;

    public LogOpNode(BaseNode leftNode, Token operationNode, BaseNode rightNode){
        super(operationNode);
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public String toString(){
        return "(" + leftNode + " " + token + " " + rightNode + ")";
    }
}
