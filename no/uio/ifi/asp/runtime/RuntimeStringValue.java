package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {
    String stringVal;

    public RuntimeStringValue(String string) {
        this.stringVal = string;
    }

    @Override
    public String typeName() {
        return "string";
    }

    @Override
    public String showInfo() { // forelesning PPT uke 41 2023, Dag Langmyhr
        if (stringVal.indexOf('\'') >= 0)
            return '"' + stringVal + '"';
        else
            return "'" + stringVal + "'";
    }

    @Override
    public String toString() {
        return stringVal;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (stringVal.trim().isEmpty())
            return false;
        return true;
    }

    public String getStringValue(String what, AspSyntax where) {
        return stringVal;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        try {
            double floatVal = Float.valueOf(stringVal);
            return floatVal;
        } catch (NumberFormatException e) {
            runtimeError("String " + this.showInfo() + " is not a legal float", where);
        }
        return 0.0;

    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        try {
            long floatVal = Long.valueOf(stringVal);
            return floatVal;
        } catch (NumberFormatException e) {
            runtimeError("String " + this.showInfo() + " is not a legal int", where);
        }
        return 0;

    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (stringVal.equals(""))
            return new RuntimeBoolValue(true);
        return new RuntimeBoolValue(false);
    }

    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(stringVal.length());
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            return new RuntimeStringValue(stringVal + v.toString());
        }
        runtimeError("Type error for +.", where);
        return null;
    }

    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (stringVal.equals(v.getStringValue("== operand", where))) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==", where);
        return null;
    }

    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (!stringVal.equals(v.getStringValue("!= operand", where))) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=", where);
        return null;
    }

    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (stringVal.compareTo(v.getStringValue("> operand", where)) == 1) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for >", where);
        return null;
    }

    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (stringVal.compareTo(v.getStringValue(">= operand", where)) > -1) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for >=", where);
        return null;

    }

    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (stringVal.compareTo(v.getStringValue("<= operand", where)) < 1) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for <=", where);
        return null;

    }

    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            if (stringVal.compareTo(v.getStringValue("< operand", where)) < 0) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for <", where);
        return null;

    }

    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            if (v.getIntValue("* operand int for string multiplication", where) <= Integer.MAX_VALUE) {
                return new RuntimeStringValue(stringVal.repeat((int) v.getIntValue("* operand", where)));
            } else if (v.getIntValue("int for string multiplication", where) < 0) {
                runtimeError("cannot multiply string with a negative integer", where);
            } else {
                runtimeError(String.format("multiplication operand: %d too big",
                        v.getIntValue("* operand int for string multiplication", where)), where);
            }
        }
        runtimeError("type error for string multiplication", where);
        return null;
    }

    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        int index = (int) v.getIntValue("index of single letter", where);

        if (index < stringVal.length())
            return new RuntimeStringValue("" + stringVal.charAt(index));
        runtimeError(String.format("string index (%d) out of range (%d).", index, stringVal.length() - 1), where);
        return null;
    }
}
