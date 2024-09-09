package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspExprStmt extends AspSmallStmt {
    AspExpr ae;

    AspExprStmt(int n) {
        super(n);
    }

    static AspExprStmt parse(Scanner s) {
        enterParser("expr stmt");
        AspExprStmt aes = new AspExprStmt(s.curLineNum());
        aes.ae = AspExpr.parse(s);
        leaveParser("expr stmt");
        return aes;
    }

    @Override
    void prettyPrint() {
        ae.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = ae.eval(curScope);
        trace("Expression statement produced " + v.toString());
        return v;
    }

}
