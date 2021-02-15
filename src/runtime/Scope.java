package runtime;

import parser.nodes.ListAccessNode;

import java.util.HashMap;
import java.util.Map;

public class Scope {
    private final Map<String, RuntimeValue> variables = new HashMap<>();
    private final Scope upperScope;

    public Scope(Scope upperScope){
        this.upperScope = upperScope;
    }

    public RuntimeValue get(String str){
        if(variables.containsKey(str)) return variables.get(str);
        else if(upperScope != null) return upperScope.get(str);
        else return null;
    }

    public void set(String str, RuntimeValue rtv){
        Scope scp = this;
        while(scp != null){
            if(scp.variables.containsKey(str)){
                scp.variables.put(str, rtv);
                return;
            }
            scp = scp.upperScope;
        }
        variables.put(str, rtv);
    }

    public void setListElement(ListAccessNode node, RuntimeValue rtv){
        Scope scp = this;
        String str = node.variableName();
        while(scp != null){
            if(scp.variables.containsKey(str)){
                scp.variables.put(str, rtv);
                return;
            }
            scp = scp.upperScope;
        }

        ListType tempList =  (ListType)scp.variables.get(str);
        tempList.elements.set(node.index, rtv);
        scp.variables.put(str, tempList);
    }

    public Scope getUpperScope() { return upperScope; }
}
