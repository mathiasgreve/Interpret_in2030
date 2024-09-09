package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import java.util.HashMap;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactor extends AspSyntax {
    ArrayList<AspPrimary> ap = new ArrayList<>();
    HashMap<Integer, AspFactorPrefix> afp = new HashMap<>();
    ArrayList<AspFactorOpr> afo = new ArrayList<>();

    AspFactor(int n) {
        super(n);
    }

    static AspFactor parse(Scanner s) {
        enterParser("factor");

        AspFactor af = new AspFactor(s.curLineNum());

        int n = 0; // indeks for hvilke 'primary' som er tilknyttet en 'factor prefix'

        while (true) { // looper gjennom dersom det finnes 'factor opr' etter 'primary'

            // hvis det finnes pluss eller minus parser factor prefix
            if (s.isFactorPrefix()) {
                af.afp.put(n, AspFactorPrefix.parse(s));
            }

            af.ap.add(AspPrimary.parse(s));

            if (!s.isFactorOpr())
                break;

            af.afo.add(AspFactorOpr.parse(s));
            n++;
        }

        leaveParser("factor");
        return af;
    }

    @Override
    void prettyPrint() {
        int n = 0;
        for (AspPrimary p : ap) { // printer alle 'primary'
            if (n > 0)
                afo.get(n - 1).prettyPrint(); // printer eventuelle 'factor opr' etter én loop
            if (afp.containsKey(n))
                afp.get(n).prettyPrint(); // printer eventuelle 'factor prefix'
            p.prettyPrint();
            n++;
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = ap.get(0).eval(curScope);
        for (int i = 0; i < ap.size(); i++) {
            if (i > 0) {
                TokenKind t = afo.get(i - 1).tk;
                RuntimeValue w = ap.get(i).eval(curScope);
                if (hasMinusPrefix(afp, i, w)) {
                    w = w.evalNegate(this);
                }
                switch (t) {
                    case astToken:
                        v = v.evalMultiply(w, this);
                        break;
                    case slashToken:
                        v = v.evalDivide(w, this);
                        break;
                    case percentToken:
                        v = v.evalModulo(w, this);
                        break;
                    case doubleSlashToken:
                        v = v.evalIntDivide(w, this);
                        break;
                    default:
                        Main.panic("Illegal factor operator: " + t);

                }
            }

            if (i == 0) {
                if (afp.containsKey(i)) {
                    if (afp.get(i).tk == plusToken) {
                        v = v.evalPositive(this);
                    } else if (afp.get(i).tk == minusToken) {
                        v = v.evalNegate(this);
                    }
                }
            }
        }

        return v;
    }

    private boolean hasMinusPrefix(HashMap<Integer, AspFactorPrefix> afp, int i, RuntimeValue r) {
        if (afp.containsKey(i)) {
            if (afp.get(i).tk == plusToken) {
                return false;
            } else if (afp.get(i).tk == minusToken) {
                return true;
            }
        }
        return false;
    }
}
