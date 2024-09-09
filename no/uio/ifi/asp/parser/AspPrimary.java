package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPrimary extends AspSyntax {
    AspAtom aa;
    ArrayList<AspPrimarySuffix> aps = new ArrayList<>();

    AspPrimary(int n) {
        super(n);
    }

    static AspPrimary parse(Scanner s) {
        enterParser("primary");
        AspPrimary ap = new AspPrimary(s.curLineNum());

        ap.aa = AspAtom.parse(s);

        while (s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken) {
            ap.aps.add(AspPrimarySuffix.parse(s));
        }

        leaveParser("primary");
        return ap;
    }

    @Override
    void prettyPrint() {
        aa.prettyPrint();
        for (AspPrimarySuffix ps : aps) {
            ps.prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v;
        v = aa.eval(curScope);

        boolean harArg = false;
        int i;
        for (i = 0; i < aps.size(); i++) {
            if (aps.get(i) instanceof AspArguments) {
                harArg = true;
                break;
            } else if (aps.get(i) instanceof AspSubscription) {
                v = v.evalSubscription(aps.get(i).eval(curScope), this);
            } else {
                Main.error("Primary suffix is not subscription or argument");
            }
        }

        if (harArg) {

            // Sjekk om 'v' faktisk er en funksjon
            if (v instanceof RuntimeFunc) {

                // Beregner de aktuelle parametrene
                RuntimeListValue actParams = (RuntimeListValue) aps.get(i).eval(curScope);

                trace("Call function: " + v.showInfo() + " with params: " + actParams.toString());

                v = v.evalFuncCall(actParams.listVal, this);

            }
        }

        return v;

    }
}
