package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeListValue extends RuntimeValue {
    public ArrayList<RuntimeValue> listVal;

    public RuntimeListValue(ArrayList<RuntimeValue> liste) {
        listVal = liste;
    }

    @Override
    public String typeName() {
        return "list";
    }

    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i < listVal.size(); i++) {
            if (i > 0)
                s += ", ";
            s += listVal.get(i).showInfo();
        }
        s += "]";
        return s;
    }

    @Override
    public String showInfo() {
        String s = "[";
        for (int i = 0; i < listVal.size(); i++) {
            if (i > 0)
                s += ", ";
            s += listVal.get(i).showInfo();
        }
        s += "]";
        return s;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (listVal.isEmpty())
            return false;
        return true;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("list", where));
    }

    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(listVal.size());
    }

    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            if (v.getIntValue("* operand int for list multiplication", where) <= Integer.MAX_VALUE) {
                ArrayList<RuntimeValue> nyliste = new ArrayList<>();
                for (int i = 0; i < (int) v.getIntValue("int for list multiplication", where); i++) {
                    nyliste.addAll(listVal);
                }
                return new RuntimeListValue(nyliste);
            } else if (v.getIntValue("int for list multiplication", where) < 0) {
                runtimeError("cannot multiply list with a negative integer", where);
            } else {
                runtimeError(String.format("multiplication operand: %d too big",
                        v.getIntValue("* operand int for list multiplication", where)), where);
            }
        }
        runtimeError("type error for list multiplication", where);
        return null;
    }

    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        int index = (int) v.getIntValue("list index", where);

        if (index < listVal.size()) {
            RuntimeValue val = listVal.get(index);
            return val; // eller skal jeg lage og returnere et nytt element??
        }

        runtimeError(String.format("List index (%d) out of range (%d).", index, listVal.size() - 1), where);
        return null;
    }

    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        return new RuntimeBoolValue(!(v instanceof RuntimeNoneValue));
    }

    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        return new RuntimeBoolValue(v instanceof RuntimeNoneValue);
    }

    protected ArrayList<RuntimeValue> getList() {
        return listVal;
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        // sjekker indeksens type (RuntimeInt) og verdi og setter høyresideverdien inn
        // på riktig plass i ArrayList
        if (inx instanceof RuntimeIntValue) {
            if (inx.getIntValue("list index int", where) < listVal.size()) {
                listVal.set((int) inx.getIntValue("list index int", where), val);
            }
        }
    }
}
