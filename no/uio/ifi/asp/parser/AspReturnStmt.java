package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspReturnStmt extends AspSmallStmt {
    AspExpr ae;

    AspReturnStmt(int n){
        super(n);
    }

    static AspReturnStmt parse(Scanner s){
        enterParser("return stmt");
        AspReturnStmt ars = new AspReturnStmt(s.curLineNum());

        skip(s,returnToken);
        ars.ae = AspExpr.parse(s);

        leaveParser("return stmt");
        return ars;
    }

    @Override
    void prettyPrint() {
        prettyWrite("return ");
        ae.prettyPrint();
    }

    // hentet fra forelesning 06.11.2023 Uke 45 Dag Langmyhr
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = ae.eval(curScope);
        trace("return "+v.showInfo());
        throw new RuntimeReturnValue(v, lineNum);
    }

}
