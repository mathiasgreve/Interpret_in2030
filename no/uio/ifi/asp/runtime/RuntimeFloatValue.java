package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue {
    double floatVal;

    public RuntimeFloatValue(double floatnumber) {
        floatVal = floatnumber;
    }

    @Override
    public String typeName() {
        return "float";
    }

    @Override
    public String toString() {
        return floatVal + "";
    }

    @Override
    public String showInfo() {
        return floatVal + "";
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatVal;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        if (floatVal == 0.0)
            return false;
        return true;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        if (floatVal == 0.0)
            return new RuntimeBoolValue(true);
        return new RuntimeBoolValue(false);
    }

    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(-floatVal);
    }

    public RuntimeValue evalPositive(AspSyntax where) {
        return new RuntimeFloatValue(floatVal);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatVal + v.getIntValue("- operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatVal + v.getFloatValue("- operand", where));
        }
        runtimeError("Type error for -.", where);
        return null;
    }

    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatVal * v.getFloatValue("* operand", where));
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatVal * v.getIntValue("* operand", where));
        }
        runtimeError("Type error for *.", where);
        return null;
    }

    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatVal - v.getIntValue("+ operand", where));
        } else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatVal - v.getFloatValue("+ operand", where));
        }
        runtimeError("Type error for +.", where);
        return null;
    }

    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatVal
                    - v.getFloatValue("% operand", where) * Math.floor(floatVal / v.getFloatValue("% operand", where)));
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatVal
                    - v.getIntValue("% operand", where) * Math.floor(floatVal / v.getIntValue("% operand", where)));
        }
        runtimeError("Type error for %.", where);
        return null;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(floatVal / v.getFloatValue("/ operand", where));
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(floatVal / v.getIntValue("/ operand", where));
        }
        runtimeError("Type error for /.", where);
        return null;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(floatVal / v.getFloatValue("// operand", where)));
        } else if (v instanceof RuntimeIntValue) {
            return new RuntimeFloatValue(Math.floor(floatVal / v.getIntValue("// operand", where)));
        }
        runtimeError("Type error for //.", where);
        return null;
    }

    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeFloatValue) {
            if (floatVal == v.getFloatValue("== operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (floatVal == (float) v.getIntValue("== operand", where)) {
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
            if (floatVal != v.getFloatValue("!= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (floatVal != (float) v.getIntValue("!= operand", where)) {
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
            if (floatVal > v.getFloatValue("> operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (floatVal > (float) v.getIntValue("> operand", where)) {
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
            if (floatVal >= v.getFloatValue(">= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (floatVal >= (float) v.getIntValue(">= operand", where)) {
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
            if (floatVal <= v.getFloatValue("<= operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (floatVal <= (float) v.getIntValue("<= operand", where)) {
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
            if (floatVal < v.getFloatValue("< operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        } else if (v instanceof RuntimeIntValue) {
            if (floatVal < (float) v.getIntValue("< operand", where)) {
                return new RuntimeBoolValue(true);
            } else {
                return new RuntimeBoolValue(false);
            }
        }
        runtimeError("Type error for <.", where);
        return null;
    }

}
