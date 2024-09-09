package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspForStmt extends AspCompoundStmt {
    AspName an;
    AspExpr ae;
    AspSuite as;

    AspForStmt(int n) {
        super(n);
    }

    static AspForStmt parse(Scanner s) {
        enterParser("for stmt");

        AspForStmt afs = new AspForStmt(s.curLineNum());

        skip(s, forToken);

        afs.an = AspName.parse(s);
        skip(s, inToken);
        afs.ae = AspExpr.parse(s);
        skip(s, colonToken);
        afs.as = AspSuite.parse(s);

        leaveParser("for stmt");
        return afs;
    }

    @Override
    void prettyPrint() {
        prettyWrite("for ");
        an.prettyPrint();
        prettyWrite(" in ");
        ae.prettyPrint();
        prettyWrite(" : ");
        as.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = ae.eval(curScope); // evaluerer først kontrolluttrykket - må være en liste
        if (v instanceof RuntimeListValue) {
            RuntimeListValue list = (RuntimeListValue) v;
            int i = 1;
            for (RuntimeValue val : list.listVal) { // innmaten i løkka blir utført så mange ganger som listens lengde
                                                    // tilsier

                trace("for #" + i + ": " + an.t.name + " = " + val);

                // tilordne elementet (val) til variabelen <name>
                curScope.assign(an.t.name, val);

                // evaluer <suite>
                as.eval(curScope);
                i++;
            }
        } else {
            RuntimeValue.runtimeError("For-loop error! Data type: '"+v.typeName() + "' is not a list", this);
        }
        return null;
    }

}
