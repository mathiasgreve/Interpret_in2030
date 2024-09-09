package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspTerm extends AspSyntax {
    ArrayList<AspTermOpr> ato = new ArrayList<>();
    ArrayList<AspFactor> af = new ArrayList<>();

    AspTerm(int n) {
        super(n);
    }

    static AspTerm parse(Scanner s) {
        enterParser("term");

        AspTerm at = new AspTerm(s.curLineNum());
        while (true) { // parser 'factor' og går i flere loops dersom det finnes 'term opr' etter
                       // factoren
            at.af.add(AspFactor.parse(s));
            if (!s.isTermOpr())
                break;
            at.ato.add(AspTermOpr.parse(s));
        }

        leaveParser("term");
        return at;
    }

    @Override
    void prettyPrint() {
        int n = 0;
        for (AspFactor f : af) {
            if (n > 0) {
                ato.get(n - 1).prettyPrint();
            }
            f.prettyPrint();
            n++;
        }
    }

    // eval-metoden hentet fra forelesning PPT Uke 41 2023 - Dag Langmyhr
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = af.get(0).eval(curScope);
        for (int i = 1; i < af.size(); ++i) {
            TokenKind k = ato.get(i - 1).tk;
            switch (k) {
                case minusToken:
                    v = v.evalSubtract(af.get(i).eval(curScope), this);
                    break;
                case plusToken:
                    v = v.evalAdd(af.get(i).eval(curScope), this);
                    break;
                default:
                    Main.panic("Illegal term operator: " + k + "!");
            }
        }
        return v;
    }

}
