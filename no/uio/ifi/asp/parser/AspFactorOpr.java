package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspFactorOpr extends AspSyntax {
    TokenKind tk;

    AspFactorOpr(int n) {
        super(n);
    }

    static AspFactorOpr parse(Scanner s) {
        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());
        enterParser("factor opr");

        if (s.isFactorOpr()) {
            afo.tk = s.curToken().kind;
            skip(s, s.curToken().kind);
        }

        leaveParser("factor opr");
        return afo;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + tk.toString() + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }
}
