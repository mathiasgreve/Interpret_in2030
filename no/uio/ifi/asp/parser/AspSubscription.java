package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSubscription extends AspPrimarySuffix {
    AspExpr ae;

    AspSubscription(int n) {
        super(n);
    }

    static AspSubscription parse(Scanner s) {
        enterParser("subscription");
        AspSubscription as = new AspSubscription(s.curLineNum());
        skip(s, leftBracketToken);
        as.ae = AspExpr.parse(s);
        skip(s, rightBracketToken);
        leaveParser("subscription");
        return as;
    }

    @Override
    void prettyPrint() {
        prettyWrite("[");
        ae.prettyPrint();
        prettyWrite("]");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return ae.eval(curScope);
    }
}
