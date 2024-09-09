package no.uio.ifi.asp.runtime;

import java.util.HashMap;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.*;

public class RuntimeDictValue extends RuntimeValue {
    HashMap<String, RuntimeValue> dictVal;

    public RuntimeDictValue(HashMap<String, RuntimeValue> hm) {
        dictVal = hm;
    }

    @Override
    public String typeName() {
        return "dict";
    }

    @Override
    public String toString() {
        String s = "{";
        int i = 0;

        for (String key : dictVal.keySet()) {
            if (i > 0) {
                s += ", ";
            }
            s += key + ":" + dictVal.get(key).showInfo();

            i++;
        }
        return s + "}";
    }

    @Override
    public String showInfo() {
        String s = "{";
        int i = 0;

        for (String key : dictVal.keySet()) {
            if (i > 0) {
                s += ", ";
            }
            s += "'"+key + "': " + dictVal.get(key).showInfo();

            i++;
        }
        return s + "}";
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (dictVal.size() == 0)
            return false;
        return true;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("dict", where));
    }

    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(dictVal.size());
    }

    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        String key = v.getStringValue("index of single letter", where);

        if (hasKey(key)) {
            RuntimeValue val = getVal(key);
            return val; // eller skal jeg lage og returnere et nytt element??
        }

        runtimeError(String.format("Dictionary key '%s' not in dict.", key), where);
        return null;
    }

    private boolean hasKey(String s) {
        for (HashMap.Entry<String, RuntimeValue> set : dictVal.entrySet()) {
            if (set.getKey().toString().equals(s)) {
                return true;
            }
        }
        return false;
    }

    private RuntimeValue getVal(String key) {
        for (HashMap.Entry<String, RuntimeValue> set : dictVal.entrySet()) {
            if (set.getKey().toString().equals(key)) {
                return set.getValue();
            }
        }
        return null;
    }

    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        return new RuntimeBoolValue(!(v instanceof RuntimeNoneValue));
    }

    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        return new RuntimeBoolValue(v instanceof RuntimeNoneValue);
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        // sjekker indeksens type (RuntimeInt) og verdi og setter høyresideverdien inn
        // på riktig plass i HashMap
        if (inx instanceof RuntimeStringValue) {
            if (dictVal.containsKey(inx.toString())) {
                dictVal.replace(inx.toString(), val);
            } else {
                dictVal.put(inx.toString(), val);
            }
        }
    }

}
