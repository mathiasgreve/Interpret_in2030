package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspGlobalStmt extends AspSmallStmt {
    ArrayList<AspName> an = new ArrayList<>();

    AspGlobalStmt(int n) {
        super(n);
    }

    static AspGlobalStmt parse(Scanner s) {
        enterParser("global stmt");
        AspGlobalStmt ags = new AspGlobalStmt(s.curLineNum());
        skip(s, globalToken);
        while (true) { // parser navn, eventuelt flerenavn dersom de er separert av komma-token
            ags.an.add(AspName.parse(s));
            if (s.curToken().kind != commaToken)
                break;
            skip(s, commaToken);
        }
        leaveParser("global stmt");
        return ags;
    }

    @Override
    void prettyPrint() {
        int antall = 0;
        prettyWrite("global ");
        for (AspName n : an) {
            if (antall++ > 0) {
                prettyWrite(", ");
            }
            n.prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        // trace("global name assignment: "+an.toString());
        for (AspName name : an) {
            curScope.registerGlobalName(name.t.name);
        }
        return null;
    }

}
