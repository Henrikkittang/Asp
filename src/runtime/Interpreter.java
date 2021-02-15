package runtime;

/*
* Executes the AST provided by the parser in a dept-first order
* Initialises and resolves variables
* Runs the correct functions
*
* The errors thrown by the Interpreter are: 'RuntimeError'
* */


import com.company.Main;
import errorHandler.IndentError;
import errorHandler.InternalAspError;
import errorHandler.RuntimeError;
import parser.nodes.*;
import scanner.*;
import parser.*;

import java.util.List;

public class Interpreter {
    private final String filename;
    private final List<BaseNode> ast;
    private Scope curScope = new Scope(null);

    public Interpreter(Parser parser){
        filename = parser.filename;
        ast = parser.parse();
    }

    public void execute(){
        for(BaseNode curNode : ast){
            RuntimeValue total = visit(curNode);
            System.out.print("Result: ");
            System.out.println(total);
        }
    }

    private RuntimeValue visit(BaseNode node){
        if(node instanceof PrimitiveNode)
            return visitPrimitive((PrimitiveNode) node);
        else if(node instanceof UnaryNode)
            return visitUnary((UnaryNode) node);
        else if(node instanceof VariableNode)
            return visitVariable((VariableNode) node);
        else if(node instanceof AssignOpNode)
            return visitAssignOp((AssignOpNode) node);
        else if (node instanceof LogOpNode)
            return visitLogOp((LogOpNode)node);
        else if(node instanceof CompOpNode)
            return visitCompOp((CompOpNode) node);
        else if(node instanceof BinOpNode)
            return visitBinOp((BinOpNode) node);
        else if(node instanceof ListNode)
            return visitListNode((ListNode) node);
        else if(node instanceof ListAccessNode)
            return visitListAccessNode((ListAccessNode)node);
        else if(node instanceof ControlFlowNode)
            return visitControlFlowNode((ControlFlowNode) node);
        else if(node instanceof IfExprNode)
            return visitIfExpr((IfExprNode) node);
        else if(node instanceof WhileExprNode)
            return visitWhileExpr((WhileExprNode) node);

        return null;
    }

    private RuntimeValue visitPrimitive(PrimitiveNode node){
        Main.logger.noteNode(node);
        if(node.token.token_type == TokenTypes.INT || node.token.token_type == TokenTypes.FLOAT)
            return new Number(node.token);
        else if(node.token.token_type == TokenTypes.BOOL)
            return new Boolean(node.token);

        return null;
    }

    private Number visitUnary(UnaryNode node){
        Main.logger.noteNode(node);
        Number curNum = (Number)visit(node.numberNode);

        if(curNum == null)
            Main.error(new InternalAspError("Error detected in visitUnary", filename, 0, 0));
        else if(node.token.token_type == TokenTypes.MINUS)
            curNum.value *= -1;

        return curNum;
    }

    private RuntimeValue visitVariable(VariableNode node){
        Main.logger.noteNode(node);

        String variableName = node.variableName();
        RuntimeValue val = curScope.get(variableName);

        if(val == null)
            Main.error(new RuntimeError("Variable missing declaration", filename, node.token.linePos, node.token.rowPos));

        return val;

    }

    private None visitAssignOp(AssignOpNode node){
        Main.logger.noteNode(node);

        System.out.println(node);
        if(node.leftNode instanceof ListAccessNode){
            curScope.setListElement((ListAccessNode)node.leftNode, visit(node.rightNode));
            return new None(node.token);
        }

        String variableName = node.leftNode.token.variableName;
        RuntimeValue right = visit(node.rightNode);

        if(right == null) {
            Main.error(new InternalAspError("Error detected in visitAssignOp", filename, 0, 0));
            return null;
        }
        if(node.token.token_type == TokenTypes.ASSIGN){
            curScope.set(variableName, right);
            return new None(node.token);
        }

        Number newNum = (Number)curScope.get(variableName);
        if(node.token.token_type == TokenTypes.PLUSEQUAL){
            newNum.add_to((Number) right);
        }else if(node.token.token_type == TokenTypes.MINUSEQUAL){
            newNum.subtract_to((Number) right);
        }else if(node.token.token_type == TokenTypes.MULTIPLYEQUAL){
            newNum.multiply_to((Number) right);
        }else if(node.token.token_type == TokenTypes.DIVIDEEQUAL){
            if(((Number) right).value == 0)
                Main.error(new RuntimeError("Division by zero", filename, right.linePos, right.rowPos));
            newNum.divide_to((Number) right);
        }else if(node.token.token_type == TokenTypes.MODULUSEQUAL){
            if(((Number) right).value == 0)
                Main.error(new RuntimeError("Division by zero", filename, right.linePos, right.rowPos));
            newNum.modulus_to((Number) right);
        }else if(node.token.token_type == TokenTypes.POWEREQUAL){
            if(newNum.value < 0 && ((Number) right).value < 1)
                Main.error(new RuntimeError("Square root of negative number", filename, right.linePos, right.rowPos));
            newNum.power_to((Number) right);
        }else if(node.token.token_type == TokenTypes.FLOORDIVIDEEQUAL){
            if(((Number) right).value == 0)
                Main.error(new RuntimeError("Division by zero", filename, right.linePos, right.rowPos));
            newNum.floordivide_to((Number) right);
        } else{
            // Should never get called
            Main.error(new InternalAspError("Expected assignment operator", filename, node.token.linePos, node.token.rowPos));
            return null;
        }

        curScope.set(variableName, newNum);
        return new None(node.token);
    }

    private Number visitBinOp(BinOpNode node){
        Main.logger.noteNode(node);

        Number left = (Number)visit(node.leftNode);
        Number right = (Number)visit(node.rightNode);

        if(left == null || right == null) {
            Main.error(new InternalAspError("Error detected in visitBinOp", filename, 0, 0));
            return null;
        }

        if(node.token.token_type == TokenTypes.PLUS){
            left.add_to(right);
        }else if(node.token.token_type == TokenTypes.MINUS){
            left.subtract_to(right);
        }else if(node.token.token_type == TokenTypes.MULTIPLY){
            left.multiply_to(right);
        }else if(node.token.token_type == TokenTypes.DIVIDE){
            if(right.value == 0){
                Main.error(new RuntimeError("Division by zero", filename, right.linePos, right.rowPos));
            }
            left.divide_to(right);
        } else if(node.token.token_type == TokenTypes.FLOORDIVIDE) {
            left.floordivide_to(right);
        } else if(node.token.token_type == TokenTypes.MODULUS) {
            if(right.value == 0){
                Main.error(new RuntimeError("Division by zero", filename, right.linePos, right.rowPos));
            }
            left.modulus_to(right);
        }else if(node.token.token_type == TokenTypes.POWER){
            if(left.value < 0 && right.value < 1){
                Main.error(new RuntimeError("Square root of negative number", filename, right.linePos, right.rowPos));
            }
            left.power_to(right);
        }else{
            // Never gets called, should be detected in parser
            Main.error(new InternalAspError("Expected operator", filename, node.token.linePos, node.token.rowPos));
        }
        return left;
    }

    private ListType visitListNode(ListNode node){
        ListType list = new ListType(node.token);
        for(BaseNode n : node.elements)
            list.elements.add(visit(n));
        return list;
    }

    private RuntimeValue visitListAccessNode(ListAccessNode node){
        Main.logger.noteNode(node);

        String variableName = node.variableName();
        ListType val = (ListType) curScope.get(variableName);

        node.index = node.index < 0 ? val.elements.size()+ node.index : node.index;

        if(val == null)
            Main.error(new RuntimeError("Variable missing declaration", filename, node.token.linePos, node.token.rowPos));
        else if(node.index > val.elements.size())
            Main.error(new IndentError("Index out of range", filename, node.token.linePos, node.token.rowPos));


        return val.elements.get(node.index);
    }

    private Boolean visitLogOp(LogOpNode node){
        Main.logger.noteNode(node);

        Boolean left = (Boolean)visit(node.leftNode);
        Boolean right = (Boolean)visit(node.rightNode);

        if(left == null || right == null) {
            Main.error(new InternalAspError("Error detected in visitLogOp", filename, 0, 0));
            return null;
        }

        if(node.token.token_type == TokenTypes.AND)
            left.and(right);
        else if(node.token.token_type == TokenTypes.OR)
            left.or(right);

        return left;
    }

    private Boolean visitCompOp(CompOpNode node){
        Main.logger.noteNode(node);

        Number left = (Number)visit(node.leftNode);
        Number right = (Number)visit(node.rightNode);

        if(left == null || right == null) {
            Main.error(new InternalAspError("Error detected in visitCompOp", filename, 0, 0));
            return null;
        }

        Boolean bool;
        if(node.token.token_type == TokenTypes.COMPAREEQUAL){
            bool = left.is_equal_to(right);
        }else if(node.token.token_type == TokenTypes.GREATERTHAN){
            bool = left.is_greater_than(right);
        }else if(node.token.token_type == TokenTypes.LESSTHAN){
            bool = left.is_less_than(right);
        }else if(node.token.token_type == TokenTypes.COMPARENOTEQUAL){
            bool = left.is_not_equal(right);
        }else if(node.token.token_type == TokenTypes.COMPAREGREATEREQUAL){
            bool = left.is_greater_or_equal(right);
        }else if(node.token.token_type == TokenTypes.COMPARELESSEQUAL){
            bool = left.is_less_or_equal(right);
        }else{
            // Never gets called, should be detected in parser
            Main.error(new InternalAspError("Expected operator", filename, node.token.linePos, node.token.rowPos));
            return null;
        }
        return bool;
    }

    public ControlFlowType visitControlFlowNode(ControlFlowNode node){
        Main.logger.noteNode(node);
        return new ControlFlowType(node.token);
    }

    public RuntimeValue visitIfExpr(IfExprNode node){
        Main.logger.noteNode(node);

        System.out.println(node.pairs);

        for(IfExprPair p : node.pairs){

            // if p.expression is null, then the input doesn't exists, aka it it a else statement
            Boolean expr = ( (p.expression == null) ? null : (Boolean)visit(p.expression) );

            if(expr == null || expr.value){
                curScope = new Scope(curScope);
                for(BaseNode gn : p.content){
                    RuntimeValue result = visit(gn);
                    if(result instanceof ControlFlowType){
                        return result;
                    }
                }
                curScope = curScope.getUpperScope();
                break;
            }
        }
        return new None(node.token);
    }

    private None visitWhileExpr(WhileExprNode node){
        Main.logger.noteNode(node);
        if(node.token.token_type == TokenTypes.WHILE){
            curScope = new Scope(curScope);
            Boolean input = (Boolean)visit(node.inputExpr);
            while(input.value){
                for(BaseNode n : node.nodes) {
                    RuntimeValue result = visit(n);
                    if(result instanceof ControlFlowType){
                        if(((ControlFlowType)result).isContinue) break;
                        else return new None(node.token);
                    }
                }
                input = (Boolean)visit(node.inputExpr);
            }
            curScope = curScope.getUpperScope();
        }

        return new None(node.token);
    }
}
