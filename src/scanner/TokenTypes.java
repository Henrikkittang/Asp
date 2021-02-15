package scanner;

public enum TokenTypes {

    // Arithmetic Operators
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    FLOORDIVIDE("//"),
    MODULUS("%"),
    POWER("**"),

    // Assignment operators
    ASSIGN("="),
    PLUSEQUAL("+="),
    MINUSEQUAL("-="),
    MULTIPLYEQUAL("*="),
    DIVIDEEQUAL("/="),
    MODULUSEQUAL("%="),
    POWEREQUAL("**="),
    FLOORDIVIDEEQUAL("//="),

    // Comparing operators
    COMPAREEQUAL("=="),
    GREATERTHAN(">"),
    LESSTHAN("<"),
    COMPARENOTEQUAL("!="),
    COMPAREGREATEREQUAL(">="),
    COMPARELESSEQUAL("<="),

    // Logical operators
        // not supported
        AND("and"),
        OR("or"),
        NOT("not"),

    // Formatting
    LEFTPARAN("("),
    RIGHTPARAN(")" ),
    INDENT("INDENT"),
    DEDENT("DEDENT"),
    COLON(":"),
    NEWLINE("\n"),
    SEMICOLON(";"),
    EOF("E-O-F"),
        // Not supported
        LEFTCURLYBRACKER("{"),
        RIGHTCURLEYBRACKET("}"),
        LEFTSQAUREBRACKET("[" ),
        RIGHTSQAUREBRACKET("]" ),
        ARGSPERATOR(","),

    // Types
    INT("INT"),
    FLOAT("FLOAT"),
    BOOL("BOOL"),
        // Not supported
        STRING("STRING"),

    // Keywords
    IF("if"),
    ELIF("elif"),
    ELSE("else"),
    WHILE("while"),
    TRUE("True"),
    FALSE("False"),
    CONTINUE("continue"),
    BREAK("break"),
        // Not supported
        FOR("for"),
        IN("in"),
        DEF("def"),


    // Internal
    VARIABLE("VARIABLE");

    public final String image;
    TokenTypes(String string){
        this.image = string;
    }
}
