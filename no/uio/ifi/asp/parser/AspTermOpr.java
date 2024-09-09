package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTermOpr extends AspSyntax {
    TokenKind tk;

    AspTermOpr(int n) {
        super(n);
    }

    static AspTermOpr parse(Scanner s) {
        enterParser("term opr");
        test(s, minusToken, plusToken);

        AspTermOpr ato = new AspTermOpr(s.curLineNum());

        if (s.isTermOpr()) {
            ato.tk = s.curToken().kind;
        }
        skip(s, ato.tk);

        leaveParser("term opr");
        return ato;
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
