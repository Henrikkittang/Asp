package runtime;

import scanner.*;

public class Boolean extends RuntimeValue{
    public boolean value;

    public Boolean(Token tok){
        super(tok.linePos, tok.rowPos);
        value = (boolean)tok.value;
    }

    public void and(Boolean other){
        value = value && other.value;
    }
    public void or(Boolean other){
        value = value || other.value;
    }

    public String toString(){
        if(value){
            return "True";
        }else{
            return "False";
        }
        // return java.lang.Boolean.toString(value);
    }
}
