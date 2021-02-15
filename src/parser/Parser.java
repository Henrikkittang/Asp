package parser;

import java.util.ArrayList;
import java.util.List;

import com.company.Main;
import errorHandler.SyntaxError;
import parser.nodes.*;
import scanner.*;

/*
* expression: whole sentence, deals with variable assignment
* comparing expression: two arithmetic expression, ==, >, <
* arithmetic expression: two terms, + and -
* term: two factors, *, /, // and %
* factor: unary number, '-' and '+' prefix, eks -5
* power: atom raised to the power of something, **
* atom: number or whole new expression, int, float or expr()
* ifExpr: atom and new expression
*
* The errors thrown by the parser are: 'SyntaxError'
* */

public class Parser {
    private final List<Token> tokens;
    private  List<BaseNode> statements = new ArrayList<>();
    private Token<?> curTok;
    private int tokIdx = 0;
    public final String filename;

    public Parser(Scanner scanner){
        tokens = scanner.getTokens();
        filename = scanner.filename;
        nextToken();
    }

    public List<BaseNode> parse() {
        statements = new ArrayList<>();
        curTok = null;
        tokIdx = 0;
        nextToken();

        if(curTok.token_type != TokenTypes.EOF){
            makeStatements();
            return statements;
        }else{
            System.exit(0);
            return null;
        }
    }

    private void nextToken(){
        if(tokIdx < tokens.size()){
            curTok = tokens.get(tokIdx);
        }else{
            curTok = null;
        }
        tokIdx++;
    }

    private Token<?> peekNextToken(){
        return tokens.get(tokIdx);
    }

    private WhileExprNode whileExpr(){
        Main.logger.noteOperation("found while loop");

        Token<?> tok = curTok; nextToken();

        if(curTok.token_type == TokenTypes.LEFTPARAN) nextToken();
        BaseNode inputExpression = expr();
        if(curTok.token_type == TokenTypes.RIGHTPARAN) nextToken();


        if(curTok.token_type == TokenTypes.COLON){
            nextToken();

            if(curTok.token_type == TokenTypes.NEWLINE) nextToken();

            if(curTok.token_type == TokenTypes.INDENT){
                nextToken();

                List<BaseNode> content = new ArrayList<>();
                while(curTok.token_type != TokenTypes.DEDENT){
                    content.add(expr());
                    if(curTok.token_type == TokenTypes.NEWLINE) nextToken();
                }
                nextToken();
                return new WhileExprNode(tok, inputExpression, content);
            }else{
                Main.error(new SyntaxError("Expected indent", filename, curTok.linePos, curTok.linePos));
            }
        }else{
            Main.error(new SyntaxError("Expected :", filename, curTok.linePos, curTok.linePos));
        }
        return null;
    }

    private IfExprNode ifExpr(){

        Token<?> tok = curTok;
        List<IfExprPair> pairs = new ArrayList<>();
        while (curTok.token_type == TokenTypes.IF || curTok.token_type == TokenTypes.ELIF || curTok.token_type == TokenTypes.ELSE){
            Main.logger.noteOperation("found if-elif-else statement(s)");

            BaseNode inputExpression = null;
            if(curTok.token_type == TokenTypes.IF || curTok.token_type == TokenTypes.ELIF ){
                nextToken();
                if(curTok.token_type == TokenTypes.LEFTPARAN) nextToken();
                inputExpression = expr();
                if(curTok.token_type == TokenTypes.RIGHTPARAN) nextToken();
            }else{
                nextToken();
            }
            if(curTok.token_type == TokenTypes.COLON){
                nextToken();

                if(curTok.token_type == TokenTypes.NEWLINE) nextToken();

                if(curTok.token_type == TokenTypes.INDENT){
                    nextToken();

                    List<BaseNode> content = new ArrayList<>();
                    while(curTok.token_type != TokenTypes.DEDENT){
                        content.add(expr());
                        if(curTok.token_type == TokenTypes.NEWLINE) nextToken();
                    }
                    nextToken();
                    pairs.add(new IfExprPair(inputExpression, content));
                }else{
                    Main.error(new SyntaxError("Expected indent", filename, curTok.rowPos, curTok.linePos));
                }
            }else{
                Main.error(new SyntaxError("Expected :", filename, curTok.rowPos, curTok.linePos));
            }

        }
        return new IfExprNode(tok, pairs);
    }

    private ListNode listNode(){
        Main.logger.noteOperation("found list");
        Token<?> tok = curTok;

        List<BaseNode> elems = new ArrayList<>();
        while(curTok.token_type != TokenTypes.RIGHTSQAUREBRACKET){
            nextToken();
            elems.add(expr());
            if(curTok.token_type != TokenTypes.ARGSPERATOR && curTok.token_type != TokenTypes.RIGHTSQAUREBRACKET)
                Main.error(new SyntaxError("Expected , not " + curTok, filename, curTok.rowPos, curTok.linePos));
            if(curTok.token_type == TokenTypes.NEWLINE) nextToken();
        }
        nextToken();
        return new ListNode(tok, elems);
    }

    private BaseNode atom(){
        Token<?> tok = curTok;
        if(curTok.token_type == TokenTypes.INT || curTok.token_type == TokenTypes.FLOAT || curTok.token_type == TokenTypes.BOOL){
            Main.logger.noteOperation("found atom");
            nextToken();
            return new PrimitiveNode(tok);
        }else if(curTok.token_type == TokenTypes.VARIABLE){
            Main.logger.noteOperation("found variable");
            nextToken();
            if(curTok.token_type == TokenTypes.LEFTSQAUREBRACKET){
                Main.logger.noteOperation("found list access");
                nextToken();
                ListAccessNode tempNode = new ListAccessNode(tok, (Long) curTok.value);
                nextToken();
                nextToken();
                return tempNode;
            }
            return new VariableNode(tok);
        }else if(curTok.token_type == TokenTypes.CONTINUE || curTok.token_type == TokenTypes.BREAK){
            Main.logger.noteOperation("Found " + curTok.token_type);
            nextToken();
            return new ControlFlowNode(tok);
        } else if(curTok.token_type == TokenTypes.LEFTSQAUREBRACKET){
            Main.logger.noteOperation("found left bracket");
            return listNode();
        }else if(curTok.token_type == TokenTypes.IF){
            return ifExpr();
        } else if(curTok.token_type == TokenTypes.WHILE){
            return whileExpr();
        } else if(curTok.token_type == TokenTypes.LEFTPARAN){
            Main.logger.noteOperation("found new expression in parentheses");
            nextToken();
            if(curTok.token_type == TokenTypes.RIGHTPARAN){
                nextToken();
                return expr();
            }else{
                Main.error(new SyntaxError("Expected )", filename, curTok.rowPos, curTok.linePos));
                return null;
            }
        }else{
            Main.error(new SyntaxError("Expected number, variable or parentheses, not '" + tok.token_type.image +"'" , filename, curTok.rowPos, curTok.linePos));
            return null;
        }
    }

    private BaseNode power(){
        BaseNode left = atom();
        while(curTok.token_type  == TokenTypes.POWER){
            Main.logger.noteOperation("found power operator");

            Token<?> operation_token = curTok; nextToken();
            left = new BinOpNode(left, operation_token, factor());
        }
        return left;
    }

    private BaseNode factor(){
        if(curTok.token_type == TokenTypes.PLUS || curTok.token_type == TokenTypes.MINUS){
            Main.logger.noteOperation("found unary factor");

            Token<?> tok = curTok; nextToken();
            return new UnaryNode(tok, factor());
        }
        return power();
    }

    private BaseNode term(){
        BaseNode left = factor();
        while(  curTok.token_type == TokenTypes.MULTIPLY ||
                curTok.token_type == TokenTypes.DIVIDE ||
                curTok.token_type == TokenTypes.FLOORDIVIDE ||
                curTok.token_type == TokenTypes.MODULUS){

            Main.logger.noteOperation("found term");

            Token<?> operation = curTok; nextToken();
            left = new BinOpNode(left, operation, factor());
        }
        return left;
    }

    private BaseNode arithExpr(){
        BaseNode left = term();
        while(curTok.token_type == TokenTypes.PLUS || curTok.token_type == TokenTypes.MINUS){
            Main.logger.noteOperation("found arithmetic expression");

            Token<?> operation = curTok; nextToken();
            left = new BinOpNode(left, operation, term());
        }
        return left;
    }

    private BaseNode compExpr(){
        BaseNode left = arithExpr();
        while(  curTok.token_type == TokenTypes.COMPAREEQUAL ||
                curTok.token_type == TokenTypes.GREATERTHAN ||
                curTok.token_type == TokenTypes.LESSTHAN ||
                curTok.token_type == TokenTypes.COMPARENOTEQUAL ||
                curTok.token_type == TokenTypes.COMPAREGREATEREQUAL ||
                curTok.token_type == TokenTypes.COMPARELESSEQUAL){

            Main.logger.noteOperation("found comparing expression");

            Token<?> operation = curTok; nextToken();
            left = new CompOpNode(left, operation, arithExpr());
        }
        return left;
    }

    private BaseNode logExpr(){
        BaseNode left = compExpr();
        while(  curTok.token_type == TokenTypes.AND ||
                curTok.token_type == TokenTypes.OR){

            Main.logger.noteOperation("found logical expression");

            Token<?> operation = curTok; nextToken();
            left = new LogOpNode(left, operation, compExpr());
        }
        return left;
    }

    private BaseNode expr(){
        BaseNode left = logExpr();
        if(left instanceof VariableNode || left instanceof ListAccessNode){
            if( curTok.token_type == TokenTypes.ASSIGN ||
                curTok.token_type == TokenTypes.PLUSEQUAL ||
                curTok.token_type == TokenTypes.MINUSEQUAL ||
                curTok.token_type == TokenTypes.MULTIPLYEQUAL ||
                curTok.token_type == TokenTypes.DIVIDEEQUAL ||
                curTok.token_type == TokenTypes.MODULUSEQUAL ||
                curTok.token_type == TokenTypes.POWEREQUAL ||
                curTok.token_type == TokenTypes.FLOORDIVIDEEQUAL){

                Main.logger.noteOperation("found variable assignment: " + curTok);

                Token<?> tok = curTok; nextToken();
                return new AssignOpNode(left, tok, expr());
            }
        }
        return left;
    }

    /*private BaseNode expr(){
        if(curTok.token_type == TokenTypes.VARIABLE){
            Token<?> peek = peekNextToken();
            if( peek.token_type == TokenTypes.ASSIGN ||
                peek.token_type == TokenTypes.PLUSEQUAL ||
                peek.token_type == TokenTypes.MINUSEQUAL ||
                peek.token_type == TokenTypes.MULTIPLYEQUAL ||
                peek.token_type == TokenTypes.DIVIDEEQUAL ||
                peek.token_type == TokenTypes.MODULUSEQUAL ||
                peek.token_type == TokenTypes.POWEREQUAL ||
                peek.token_type == TokenTypes.FLOORDIVIDEEQUAL ||
                peek.token_type == TokenTypes.LEFTSQAUREBRACKET){

                Main.logger.noteOperation("found variable assignment: " + curTok);

                Token<?> tok = curTok; nextToken();
                VariableNode varNode = new VariableNode(tok);
                tok = curTok; nextToken();
                return new AssignOpNode(varNode, tok, expr());
            }
        }
        return logExpr();
    }*/

    private void makeStatements(){
        while (curTok.token_type != TokenTypes.EOF){
            if(curTok.token_type == TokenTypes.NEWLINE){
                nextToken();
                continue;
            }
            statements.add(expr());
        }
    }

}
