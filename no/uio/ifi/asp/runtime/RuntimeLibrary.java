// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {

        // len
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(
                    ArrayList<RuntimeValue> actParams, AspSyntax where) {
                checkNumParams(actParams, 1, "len", where);
                return actParams.get(0).evalLen(where);
            }
        });

        // print
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actParams, AspSyntax where) {
                for (int i = 0; i < actParams.size(); i++) {
                    if (i > 0)
                        System.out.print(" ");
                    System.out.print(actParams.get(i).toString());
                }
                System.out.println();
                return new RuntimeNoneValue();
            }
        });

        // str
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actParams, AspSyntax where) {
                checkNumParams(actParams, 1, "str", where);
                String s = actParams.get(0).toString();
                return new RuntimeStringValue(s);
            }
        });

        // float
        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actParams, AspSyntax where) {
                checkNumParams(actParams, 1, "float", where);
                RuntimeValue v = actParams.get(0);
                assert v instanceof RuntimeStringValue || v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue
                        : "value has to be int, float or string";
                return new RuntimeFloatValue(v.getFloatValue(v.showInfo() + " to float", where));
            }
        });

        // int
        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actParams, AspSyntax where) {
                checkNumParams(actParams, 1, "int", where);
                RuntimeValue v = actParams.get(0);
                assert v instanceof RuntimeStringValue || v instanceof RuntimeIntValue || v instanceof RuntimeFloatValue
                        : "value has to be int, float or string";
                return new RuntimeIntValue(v.getIntValue("input",where));
            }
        });

        // range
        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actParams, AspSyntax where) {
                checkNumParams(actParams, 2, "int", where);
                RuntimeValue v1 = actParams.get(0);
                RuntimeValue v2 = actParams.get(1);
                ArrayList<RuntimeValue> list = new ArrayList<>();

                if (v1 instanceof RuntimeIntValue && v2 instanceof RuntimeIntValue) {

                    while (v1.evalLess(v2, where).getBoolValue("comparison for range function", where)) {
                        if (list.isEmpty()) {
                            list.add(v1);
                        } else {
                            list.add(v1);
                        }

                        v1 = v1.evalAdd(new RuntimeIntValue(1), where);
                    }

                }
                RuntimeListValue listRange = new RuntimeListValue(list);
                return listRange;
            }
        });

        // input
        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actParams, AspSyntax where) {
                // skriver ut
                checkNumParams(actParams, 1, "input", where);
                System.out.print(actParams.get(0).toString());

                // les en linje fra tastaturet
                String line = keyboard.nextLine();

                // resultatet er en string
                return new RuntimeStringValue(line);
            }
        });

    }

    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
            int nCorrect, String id, AspSyntax where) {
        if (actArgs.size() != nCorrect)
            RuntimeValue.runtimeError("Wrong number of parameters to " + id + "!", where);
    }
}
