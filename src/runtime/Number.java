package runtime;

import scanner.*;

public class Number extends RuntimeValue {
    public double value;
    public boolean isInt;

    public Number(Token token){
        super(token.linePos, token.rowPos);
        if(token.value instanceof Long){
            value = ((Long)token.value).doubleValue();
            isInt = true;
        }else{
            value = (Double) token.value;
            isInt = false;
        }
    }

    public void add_to(Number node){
        isInt = !(!isInt || !node.isInt);
        value += node.value;
    }

    public void subtract_to(Number node){
        isInt = !(!isInt || !node.isInt);
        value -= node.value;
    }

    public void multiply_to(Number node){
        isInt = !(!isInt || !node.isInt);
        value *= node.value;
    }

    public void divide_to(Number node){
        value /= node.value;
        isInt = false;
    }

    public void floordivide_to(Number node){
        value = Math.floor(value/node.value);
        isInt = true;
    }

    public void modulus_to(Number node){
        value %= node.value;
        isInt = true;
    }

    public void power_to(Number node){
        value = Math.pow(value, node.value);
        isInt = false;
    }

    public Boolean is_equal_to(Number node){
        Token boolToken = new Token<Boolean>(TokenTypes.BOOL, linePos, rowPos);
        boolToken.value = (value == node.value);
        return new Boolean(boolToken);
    }

    public Boolean is_greater_than(Number node){
        Token boolToken = new Token<Boolean>(TokenTypes.BOOL, linePos, rowPos);
        boolToken.value = (value > node.value);
        return new Boolean(boolToken);
    }

    public Boolean is_less_than(Number node){
        Token boolToken = new Token<Boolean>(TokenTypes.BOOL, linePos, rowPos);
        boolToken.value = (value < node.value);
        return new Boolean(boolToken);
    }

    public Boolean is_not_equal(Number node){
        Token boolToken = new Token<Boolean>(TokenTypes.BOOL, linePos, rowPos);
        boolToken.value = (value != node.value);
        return new Boolean(boolToken);
    }

    public Boolean is_greater_or_equal(Number node){
        Token boolToken = new Token<Boolean>(TokenTypes.BOOL, linePos, rowPos);
        boolToken.value = (value >= node.value);
        return new Boolean(boolToken);
    }

    public Boolean is_less_or_equal(Number node){
        Token boolToken = new Token<Boolean>(TokenTypes.BOOL, linePos, rowPos);
        boolToken.value = (value <= node.value);
        return new Boolean(boolToken);
    }

    public String toString(){
        if(isInt){
            return Long.toString(Math.round(value));
        }else{
            return Double.toString(value);
        }
    }
}
