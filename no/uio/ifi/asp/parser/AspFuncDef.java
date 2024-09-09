package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFuncDef extends AspCompoundStmt {
    public ArrayList<AspName> formalParams = new ArrayList<>(); // bytte fra 'an' til 'formalParams'???
    AspName funcName;
    public AspSuite as;

    AspFuncDef(int n) {
        super(n);
    }

    static AspFuncDef parse(Scanner s) {
        enterParser("func def");

        AspFuncDef afs = new AspFuncDef(s.curLineNum());

        skip(s, defToken);
        afs.funcName = AspName.parse(s);
        skip(s, leftParToken);
        while (s.curToken().kind != rightParToken) { // hvis det er parametre til funksjonen
            afs.formalParams.add(AspName.parse(s));
            if (s.curToken().kind == rightParToken)
                break; // hopp ut hvis det ikke er flere paramatre
            skip(s, commaToken); // ...ellers les komma og fortsett loop
        }
        skip(s, rightParToken);
        skip(s, colonToken);
        afs.as = AspSuite.parse(s);
        leaveParser("func def");
        return afs;

    }

    @Override
    void prettyPrint() {
        prettyWrite("def ");
        funcName.prettyPrint();
        prettyWrite("(");
        int n = 0;
        for (AspName name : formalParams) {
            if (n > 0) {
                prettyWrite(", ");
                n++;
            }
            name.prettyPrint();
        }
        prettyWrite(") :");
        as.prettyPrint();
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("Def " + funcName.t.name);
        RuntimeFunc func = new RuntimeFunc(this, curScope, funcName.t.name);
        curScope.assign(funcName.t.name, func);
        return func;
    }

    public int antParams() {
        return formalParams.size();
    }
}
