package parser.nodes;

import scanner.Token;

public class AssignOpNode extends BaseNode {
    public BaseNode leftNode;
    public BaseNode rightNode;

    public AssignOpNode(BaseNode leftNode, Token operationToken, BaseNode rightNode){
        super(operationToken);
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public String toString(){
        return "(" + leftNode + " " + token + " " + rightNode + ")";
    }
}
