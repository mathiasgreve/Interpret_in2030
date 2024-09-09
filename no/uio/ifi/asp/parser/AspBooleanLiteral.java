package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspBooleanLiteral extends AspAtom {
    TokenKind tk;
    boolean bool;

    AspBooleanLiteral(int n) {
        super(n);
    }

    static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");
        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());

        AspBooleanLiteral.test(s, trueToken, falseToken);

        abl.tk = s.curToken().kind;

        if (abl.tk.toString() == "True")
            abl.bool = true;
        else
            abl.bool = false;

        s.readNextToken();

        leaveParser("boolean literal");
        return abl;
    }

    @Override
    void prettyPrint() {
        prettyWrite(tk.toString());
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(bool);
    }
}
