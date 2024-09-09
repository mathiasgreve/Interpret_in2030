package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.*;

public class RuntimeFunc extends RuntimeValue {
    AspFuncDef def; // referer til hvor denne funksjonen er definert i syntakstreet
    RuntimeScope defScope; // peker til skopet den er deklarert i
    public String name;

    public RuntimeFunc(AspFuncDef funcDef, RuntimeScope scope, String name) {
        def = funcDef;
        defScope = scope;
        this.name = name;
    }

    public RuntimeFunc(String name) {
        this.name = name;
    }

    @Override
    public String typeName() {
        return name;
    }

    @Override
    public String showInfo() {
        return name;
    }

    // Hentet fra forelesning høst 2023 Dag Langmyhr:
    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
            AspSyntax where) {

        // Sjekker for riktig antall paramtere
        if (def.antParams() != actualParams.size()) {
            Main.error("Wrong amount of parametres!\nFormal parametres: " + def.antParams() + "\nActual parametres: "
                    + actualParams.size());
        }

        // Oppretter nytt skop
        RuntimeScope newScope = new RuntimeScope(defScope);

        // Initierer de formelle paramterene
        // - går gjennom paramterene og tilordner dem med assign()
        //   i det nye skopet
        for (int i = 0; i < actualParams.size(); i++) {
            newScope.assign(def.formalParams.get(i).t.name, actualParams.get(i));
        }

        // Utfører funksjonens innhold
        try {
            def.as.eval(newScope);
        } catch (RuntimeReturnValue rrv) {
            return rrv.value;
        }

        return new RuntimeNoneValue();
    }

}
