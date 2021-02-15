package com.company;


import parser.nodes.*;
import scanner.*;


public class Logger {
    public boolean doLogScanner = false, doLogParser = false, doLogRuntime = false;

    public void noteToken(Token tok){

        if(doLogScanner){
            System.out.print("Scanner: ");
            System.out.println(tok);
        }
    }

    public void noteOperation(String str){
        if(doLogParser){
            System.out.print("Parser: ");
            System.out.println(str);
        }
    }


    public void noteNode(BaseNode node){
        if(doLogRuntime){
            System.out.print("Runtime: found ");
            if(node instanceof PrimitiveNode){
                System.out.println("primitive node");
            }else if(node instanceof UnaryNode){
                System.out.println("unary node");
            }else if(node instanceof VariableNode){
                System.out.println("variable node");
            }else if(node instanceof AssignOpNode){
                System.out.println("assignment node");
            }else if(node instanceof BinOpNode){
                System.out.println("binary operation node");
            }else if(node instanceof IfExprNode){
                System.out.println("if-test node");
            }else if(node instanceof CompOpNode){
                System.out.println("found comparing node node");
            }else if(node instanceof ListAccessNode){
                System.out.println("found list Access node");
            }else if(node instanceof WhileExprNode){
                System.out.println("found while node");
            }else if(node instanceof ControlFlowNode){
                System.out.println("found control flow node node");
            }else{
                System.out.println("unknown node");
            }
        }
    }

    public void noteError(errorHandler.Error err){
        System.out.println(err);
    }
}
