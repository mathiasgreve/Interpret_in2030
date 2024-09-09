// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExpr extends AspSyntax {
    ArrayList<AspAndTest> andTests = new ArrayList<>();

    AspExpr(int n) {
        super(n);
    }

    public static AspExpr parse(Scanner s) {
        enterParser("expr");

        AspExpr ae = new AspExpr(s.curLineNum());
        while (true) { // parse 'and test' helt til neste token ikke er orToken
            ae.andTests.add(AspAndTest.parse(s));
            if (s.curToken().kind != orToken)
                break;
            skip(s, orToken);
        }

        leaveParser("expr");
        return ae;
    }

    @Override
    public void prettyPrint() {
        int n = 0;
        for (AspAndTest at : andTests) {
            if (n > 0) {
                prettyWrite(" or ");
            }
            at.prettyPrint();
            n++;
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = andTests.get(0).eval(curScope); // henter første andTest
        for (int i = 1; i < andTests.size(); i++) { // looper over lista med andTester HVIS den inneholder mer enn én
                                                    // andTest
            if (v.getBoolValue("or operand", this)) {
                return v; // returnerer den første andTesten som er True
            }
            v = andTests.get(i).eval(curScope);
        }
        return v; // hvis det bare er én andTest returneres denne sin .eval()

    }
}
