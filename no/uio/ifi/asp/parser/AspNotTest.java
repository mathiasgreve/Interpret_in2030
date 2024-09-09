package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNotTest extends AspSyntax {
    AspComparison ac;
    boolean hasNot = false;

    AspNotTest(int n) {
        super(n);
    }

    static AspNotTest parse(Scanner s) {
        enterParser("not test");

        AspNotTest ant = new AspNotTest(s.curLineNum());

        if (s.curToken().kind == notToken) { // skip notToken hvis det finnes
            skip(s, notToken);
            ant.hasNot = true;
        }

        ant.ac = AspComparison.parse(s);

        leaveParser("not test");
        return ant;
    }

    @Override
    void prettyPrint() {
        if (hasNot) {
            prettyWrite("not ");
        }
        ac.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue { // kode hentet fra PPT Uke 41 2023 Dag Langmyhr
        RuntimeValue v = ac.eval(curScope);
        if (hasNot) {
            v = v.evalNot(this);
        }
        return v;
    }
}
