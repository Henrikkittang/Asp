package com.company;

/*
* This projects implements the programing language Asp, which is a subset of Python
* All valid Asp code should be valid Python code but not visa-versa
*
* All components may throw 'InternalAspError' although it is not specified.
* Is is not an error that is ever supposed to be thrown since it is a result of
* an error in my own java code and not the user inputted Asp code
*
* */


/*
* To do:
* - for-loops
* - logical operates: and, or, not
* - lists, strings and dictionaries, access-operator ( [] ),
* - user-defined functions
* - built in functions, return statements
*
* - other stuff: in, not in, is, is not, assert, pass
*
* */

import parser.*;
import parser.nodes.BaseNode;
import runtime.Interpreter;
import runtime.Number;
import runtime.RuntimeValue;
import scanner.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Logger logger = new Logger();

    public static void error(errorHandler.Error err){
        logger.noteError(err);
        System.exit(1);
    }

    public static void main(String[] args) {
        String file_path = "C:\\Users\\Henrik\\IdeaProjects\\sandbox_interpreter\\src\\source.txt";

        // Main.logger.doLogScanner = true;
        Scanner scanner = new Scanner(file_path);
        scanner.makeTokens();

        Main.logger.doLogParser = true;
        Parser parser = new Parser(scanner);
        List<BaseNode> ast = parser.parse();
        System.out.println(ast);

        Main.logger.doLogRuntime = true;
        Interpreter interpreter = new Interpreter(parser);
        interpreter.execute();


    }
}
