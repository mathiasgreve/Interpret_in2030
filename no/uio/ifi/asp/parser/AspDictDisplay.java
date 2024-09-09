package no.uio.ifi.asp.parser;

import java.util.ArrayList;
import java.util.HashMap;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspDictDisplay extends AspAtom {
    ArrayList<AspStringLiteral> asl = new ArrayList<>();
    ArrayList<AspExpr> ae = new ArrayList<>();

    AspDictDisplay(int n) {
        super(n);
    }

    static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");

        AspDictDisplay addi = new AspDictDisplay(s.curLineNum());

        skip(s, leftBraceToken);
        if (s.curToken().kind != rightBraceToken) {
            while (true) { // hvis det er innhold i 'dict display' parses dette
                addi.asl.add(AspStringLiteral.parse(s));
                skip(s, colonToken);
                addi.ae.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) // hopp ut hvis det ikke er et komma - som indikerer at det er
                                                     // flere ting som skal parses
                    break;
                skip(s, commaToken);
            }
        }
        skip(s, rightBraceToken);

        leaveParser("dict display");
        return addi;
    }

    @Override
    void prettyPrint() {
        prettyWrite("{");
        int n = 0;
        for (AspStringLiteral sl : asl) {
            if (n > 0) {
                prettyWrite(", ");
            }
            sl.prettyPrint();
            prettyWrite(":");
            ae.get(n).prettyPrint();
            n++;
        }
        prettyWrite("}");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        HashMap<String, RuntimeValue> hash = new HashMap<>();

        for (int i = 0; i < asl.size(); i++) {
            hash.put(asl.get(i).eval(curScope).toString(), ae.get(i).eval(curScope));
        }
        RuntimeValue v = new RuntimeDictValue(hash);
        return v;

    }
}
