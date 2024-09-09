package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIfStmt extends AspCompoundStmt {
    ArrayList<AspExpr> ae = new ArrayList<>();
    ArrayList<AspSuite> as = new ArrayList<>();
    boolean hasElse = false;

    AspIfStmt(int n) {
        super(n);
    }

    static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");

        AspIfStmt ais = new AspIfStmt(s.curLineNum());
        skip(s, ifToken);
        while (true) { // parser 'expr'- og 'suite' helt til den IKKE finner en elifToken
            ais.ae.add(AspExpr.parse(s));
            skip(s, colonToken);
            ais.as.add(AspSuite.parse(s));
            if (s.curToken().kind != elifToken)
                break;
            else if (s.curToken().kind == elifToken) {
                skip(s, elifToken);
            }
        }
        if (s.curToken().kind == elseToken) { // sjekker om det er et else-statement
            skip(s, elseToken);
            ais.hasElse = true;
            skip(s, colonToken);
            ais.as.add(AspSuite.parse(s));
        }

        leaveParser("if stmt");
        return ais;
    }

    @Override
    void prettyPrint() {
        prettyWrite("if ");
        int n = 0;
        for (AspExpr e : ae) {
            if (n > 0)
                prettyWrite("elif ");
            e.prettyPrint();
            prettyWrite(": ");
            as.get(n).prettyPrint();
            n++;
        }
        if (hasElse) {
            prettyWrite("else: ");
            as.get(n).prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        int teller = 1;
        for (AspExpr expr : ae) {
            if (expr.eval(curScope).getBoolValue("if expr", this)) {
                if (teller == 1) {
                    trace("if true: Alt #" + teller);
                    as.get(0).eval(curScope);
                    return null;
                } else {
                    trace("elif true: Alt #" + teller);
                    as.get(teller - 1).eval(curScope);
                    return null;
                }
            }
            teller++;
        }
        if (hasElse) {
            trace("else: ");
            as.get(as.size() - 1).eval(curScope);
        }
        return null;
    }
}
