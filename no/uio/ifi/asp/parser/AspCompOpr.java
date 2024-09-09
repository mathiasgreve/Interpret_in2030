package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspCompOpr extends AspSyntax {
    TokenKind tk;

    AspCompOpr(int n) {
        super(n);
    }

    static AspCompOpr parse(Scanner s) {
        enterParser("comp opr");

        AspCompOpr aco = new AspCompOpr(s.curLineNum());

        if (s.isCompOpr()) {
            aco.tk = s.curToken().kind;
            skip(s, s.curToken().kind);
        }

        leaveParser("comp opr");
        return aco;
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
