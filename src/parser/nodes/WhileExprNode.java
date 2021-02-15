package parser.nodes;

import parser.nodes.BaseNode;
import scanner.Token;

import java.util.List;

public class WhileExprNode extends BaseNode {

    public BaseNode inputExpr;
    public List<BaseNode> nodes;

    public WhileExprNode(Token tok, BaseNode inputExpr, List<BaseNode> nodes){
        super(tok);
        this.inputExpr = inputExpr;
        this.nodes = nodes;
    }

    public String toString(){
        String total = "("  + inputExpr.toString() + " while (";
        for(BaseNode node : nodes){
            total += node.toString() + ", ";
        }
        return total + "))";
    }
}
