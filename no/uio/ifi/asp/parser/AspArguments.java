package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspArguments extends AspPrimarySuffix {
    ArrayList<AspExpr> ae = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    static AspArguments parse(Scanner s) {
        enterParser("arguments");
        AspArguments aa = new AspArguments(s.curLineNum());

        skip(s, leftParToken);
        if (s.curToken().kind != rightParToken) {
            while (true) { // parse flere 'expr'
                aa.ae.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) // bryt ut av loopen hvis det ikke finner et komma
                    break;
                skip(s, commaToken);
            }
        }
        skip(s, rightParToken);
        leaveParser("arguments");
        return aa;
    }

    @Override
    void prettyPrint() {
        prettyWrite("(");
        int n = 0;
        for (AspExpr e : ae) {
            if (n > 0) {
                prettyWrite(", ");
            }
            e.prettyPrint();
            n++;
        }
        prettyWrite(")");

    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> arrList = new ArrayList<>();

        for (AspExpr expr : ae) {
            arrList.add(expr.eval(curScope));
        }
        RuntimeListValue list = new RuntimeListValue(arrList);
        return list;
    }

}
