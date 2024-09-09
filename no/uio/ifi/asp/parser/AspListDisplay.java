package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspListDisplay extends AspAtom {
    ArrayList<AspExpr> ae = new ArrayList<>();

    AspListDisplay(int n) {
        super(n);
    }

    static AspListDisplay parse(Scanner s) {
        enterParser("list diplay");

        AspListDisplay ald = new AspListDisplay(s.curLineNum());

        skip(s, leftBracketToken);
        if (s.curToken().kind != rightBracketToken) {
            while (true) {
                ald.ae.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken)
                    break;
                skip(s, commaToken);
            }
        }

        skip(s, rightBracketToken);
        leaveParser("list display");
        return ald;
    }

    @Override
    void prettyPrint() {
        prettyWrite("[");
        int n = 0;
        for (AspExpr e : ae) {
            if (n > 0) {
                prettyWrite(", ");
            }
            e.prettyPrint();
            n++;
        }
        prettyWrite("]");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> arrList = new ArrayList<>();

        for (AspExpr e : ae) {
            arrList.add(e.eval(curScope));
        }
        RuntimeListValue list = new RuntimeListValue(arrList);
        return list;
    }

}
