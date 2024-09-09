package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

public class AspFactorPrefix extends AspSyntax {
    TokenKind tk;

    AspFactorPrefix(int n) {
        super(n);
    }

    static AspFactorPrefix parse(Scanner s) {
        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());
        enterParser("factor prefix");

        if (s.isFactorPrefix()) {
            afp.tk = s.curToken().kind;
            skip(s, s.curToken().kind);
        }

        leaveParser("factor prefix");
        return afp;
    }

    @Override
    void prettyPrint() {
        prettyWrite(tk.toString() + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }
}
