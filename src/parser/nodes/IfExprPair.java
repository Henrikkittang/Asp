package parser.nodes;

import parser.nodes.BaseNode;

import java.util.List;

public class IfExprPair {
    public final BaseNode expression;
    public final List<BaseNode> content;

    public IfExprPair(BaseNode expression, List<BaseNode> content){
        this.expression = expression;
        this.content = content;
    }

    public String toString(){
        return expression + " -> " + content;
    }
}
