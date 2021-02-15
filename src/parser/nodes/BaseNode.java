package parser.nodes;

import scanner.Token;

public abstract class BaseNode {
    public Token token = null;

    BaseNode(Token token){
        this.token = token;
    }

    public abstract String toString();
}
