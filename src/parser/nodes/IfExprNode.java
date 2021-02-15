package parser.nodes;

import scanner.Token;
import java.util.List;

public class IfExprNode extends BaseNode {

    public List<IfExprPair> pairs;

    public IfExprNode(Token token, List<IfExprPair> pairs){
        super(token);
        this.pairs = pairs;
    }

    public String toString(){
        return token.toString() + " " + pairs;
    }

}
