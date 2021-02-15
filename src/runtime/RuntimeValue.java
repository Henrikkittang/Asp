package runtime;

public abstract class RuntimeValue {
    public final int linePos;
    public final int rowPos;

    public RuntimeValue(int linePos, int rowPos){
        this.linePos = linePos;
        this.rowPos = rowPos;
    }

    public abstract String toString();
}
