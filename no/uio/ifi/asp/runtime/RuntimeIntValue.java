package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeIntValue extends RuntimeValue {
    long intVal;

    public RuntimeIntValue(long integer) {
        this.intVal = integer;
    }

    @Override
    public String typeName() {
        return "integer";
    }

    @Override
    public String toString() {
        return intVal + "";
    }

    @Override
    public String showInfo() {
        return intVal + "";
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return intVal;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        double floatVal = (float) intVal;
        return floatVal;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (intVal == 0)
            return false;
        return true;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (intVal == 0)
            return new RuntimeBoolValue(true);
        return new RuntimeBoolValue(false);
    }

    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeIntValue(-intVal);
    }

    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeIntValue(intVal);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intVal + v.getIntValue("+ operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intVal + v.getFloatValue("+ operand", where));
        }
        runtimeError("Type error for +.", where);
        return null;
    }

    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intVal - v.getIntValue("- operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intVal - v.getFloatValue("- operand", where));
        }
        runtimeError("Type error for -.", where);
        return null;
    }

    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intVal - v.getFloatValue("% operand", where)
                    * Math.floor(intVal / v.getFloatValue("/ operand", where)));
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(Math.floorMod(intVal, v.getIntValue("% operand", where)));
        }
        runtimeError("Type error for %.", where);
        return null;
    }

    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intVal * v.getFloatValue("* operand", where));
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intVal * v.getIntValue("* operand", where));
        }
        runtimeError("Type error for *.", where);
        return null;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intVal / v.getFloatValue("/ operand", where));
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(intVal / Float.valueOf(v.getIntValue("/ operand", where)));
        }
        runtimeError("Type error for /.", where);
        return null;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(intVal / v.getFloatValue("// operand", where)));
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(Math.floorDiv(intVal, v.getIntValue("// operand", where)));
        }
        runtimeError("Type error for //.", where);
        return null;
    }

    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            if ((float) intVal == v.getFloatValue("== operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (intVal == v.getIntValue("== operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }
        runtimeError("Type error for ==.", where);
        return null;
    }

    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            if ((float) intVal != v.getFloatValue("!= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (intVal != v.getIntValue("!= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        runtimeError("Type error for !=.", where);
        return null;
    }

    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            if ((float) intVal > v.getFloatValue("> operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (intVal > v.getIntValue("> operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for >.", where);
        return null;
    }

    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            if ((float) intVal >= v.getFloatValue(">= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (intVal >= v.getIntValue(">= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for >=.", where);
        return null;
    }

    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            if ((float) intVal <= v.getFloatValue("<= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (intVal <= v.getIntValue("<= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for <=.", where);
        return null;
    }

    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            if ((float) intVal < v.getFloatValue("< operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (intVal < v.getIntValue("< operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for <.", where);
        return null;
    }

}
