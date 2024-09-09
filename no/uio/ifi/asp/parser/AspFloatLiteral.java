package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFloatLiteral extends AspAtom {
    Token t;
    double d;

    AspFloatLiteral(int n) {
        super(n);
    }

    static AspFloatLiteral parse(Scanner s) {
        enterParser("float literal");

        AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());

        afl.t = s.curToken();
        afl.d = afl.t.floatLit;

        skip(s, floatToken);

        leaveParser("float literal");
        return afl;
    }

    @Override
    void prettyPrint() {
        prettyWrite(t.floatLit + "");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeFloatValue(d);
    }

}
