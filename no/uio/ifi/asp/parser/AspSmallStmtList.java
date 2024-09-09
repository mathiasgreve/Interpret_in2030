package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> smallstmts = new ArrayList<>();
    ArrayList<TokenKind> semicolons = new ArrayList<>();

    AspSmallStmtList(int n) {
        super(n);
    }

    static AspSmallStmtList parse(Scanner s) {
        enterParser("small stmt list");

        AspSmallStmtList ssl = new AspSmallStmtList(s.curLineNum());
        ssl.smallstmts.add(AspSmallStmt.parse(s));

        while (s.curToken().kind == semicolonToken) { // hvis det er flere 'small stmt' på samme linje må det finnes
                                                      // semikolon
            skip(s, semicolonToken);
            ssl.semicolons.add(semicolonToken);
            if (s.curToken().kind == newLineToken)
                break; // bryter ut av loop hvis det er slutten på linja
            ssl.smallstmts.add(AspSmallStmt.parse(s)); // ... ellers parses nye 'small stmt'
        }
        if (s.curToken().kind != newLineToken)
            skip(s, semicolonToken); // dersom det er et semikolon på slutten av 'small stmt' må dette skippes
        skip(s, newLineToken);

        leaveParser("small stmt list");
        return ssl;
    }

    @Override
    void prettyPrint() {
        for (AspSmallStmt ass : smallstmts) {
            ass.prettyPrint();
            if (semicolons.size() != 0) {
                semicolons.remove(0);
                prettyWrite("; ");
            }

        }
        prettyWriteLn();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for(AspSmallStmt ass : smallstmts){
            ass.eval(curScope);
        }
        return null;

    }

}
