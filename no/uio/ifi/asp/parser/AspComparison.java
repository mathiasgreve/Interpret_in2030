package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspComparison extends AspSyntax {
    ArrayList<AspCompOpr> aco = new ArrayList<>();
    ArrayList<AspTerm> at = new ArrayList<>();

    AspComparison(int n) {
        super(n);
    }

    static AspComparison parse(Scanner s) {
        enterParser("comparison");

        AspComparison ac = new AspComparison(s.curLineNum());
        while (true) { // parser 'term' og går i loop dersom det finnes 'comp opr' etter termen
            ac.at.add(AspTerm.parse(s));
            if (!s.isCompOpr())
                break;
            ac.aco.add(AspCompOpr.parse(s));
        }

        leaveParser("comparison");
        return ac;
    }

    @Override
    void prettyPrint() {
        int n = 0;
        for (AspTerm t : at) {
            if (n > 0) {
                aco.get(n - 1).prettyPrint();
            }
            t.prettyPrint();
            n++;
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = at.get(0).eval(curScope);
        RuntimeValue comp = null;
        for (int i = 1; i < at.size(); i++) {

            TokenKind k = aco.get(i - 1).tk;
            switch (k) {
                case lessToken:
                    comp = v.evalLess(at.get(i).eval(curScope), this);
                    break;
                case greaterToken:
                    comp = v.evalGreater(at.get(i).eval(curScope), this);
                    break;
                case doubleEqualToken:
                    comp = v.evalEqual(at.get(i).eval(curScope), this);
                    break;
                case greaterEqualToken:
                    comp = v.evalGreaterEqual(at.get(i).eval(curScope), this);
                    break;
                case lessEqualToken:
                    comp = v.evalLessEqual(at.get(i).eval(curScope), this);
                    break;
                case notEqualToken:
                    comp = v.evalNotEqual(at.get(i).eval(curScope), this);
                    break;
                default:
                    Main.panic("Illegal comparison operator: " + k);
            }

            if (!comp.getBoolValue("comparison.loop result: 'false' => no need to continue comparisons", this)) {
                break;
            }
            v = at.get(i).eval(curScope);
        }

        if (comp != null) {
            return comp;
        }

        return v;
    }

}
