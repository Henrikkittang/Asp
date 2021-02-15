package parser.nodes;

import scanner.Token;
import java.util.List;

public class ListNode extends BaseNode {
    public List<BaseNode> elements;

    public ListNode(Token tok, List<BaseNode> elements){
        super(tok);
        this.elements = elements;
    }

    public String toString(){
        return token + " " + elements;
    }
}
