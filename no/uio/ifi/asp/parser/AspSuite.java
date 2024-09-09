package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSuite extends AspSyntax {
    AspSmallStmtList assl;
    ArrayList<AspStmt> asList = new ArrayList<>();

    AspSuite(int n) {
        super(n);
    }

    static AspSuite parse(Scanner s) {
        enterParser("suite");

        AspSuite as = new AspSuite(s.curLineNum());

        if (s.curToken().kind == newLineToken) { // går den nederste veien i jernbanediagram hvis den finner
                                                 // newLineToken
            skip(s, newLineToken);
            skip(s, indentToken);
            as.asList.add(AspStmt.parse(s));
            while (s.curToken().kind != dedentToken) { // parser eventuelt flere 'stmt'
                as.asList.add(AspStmt.parse(s));
            }
            skip(s, dedentToken);
        } else { // ...ellers forsøke å parse 'small stmt list'
            as.assl = AspSmallStmtList.parse(s);
        }

        leaveParser("suite");
        return as;
    }

    @Override
    void prettyPrint() {
        if (assl != null) {
            assl.prettyPrint();
        }
        if (asList.size() > 0) {
            prettyWriteLn();
            prettyIndent();
            for (AspStmt s : asList) {
                s.prettyPrint();
            }
            prettyDedent();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if(assl == null){
            for(AspStmt as : asList){
                as.eval(curScope);
            }
        } else{
            assl.eval(curScope);
        }
        return null;
    }

}
