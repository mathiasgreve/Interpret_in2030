package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

abstract class AspCompoundStmt extends AspStmt {
    AspCompoundStmt(int n) {
        super(n);
    }

    static AspCompoundStmt parse(Scanner s) {
        enterParser("compound stmt");
        AspCompoundStmt acs = null;

        switch (s.curToken().kind) {
            case forToken:
                acs = AspForStmt.parse(s);
                break;
            case ifToken:
                acs = AspIfStmt.parse(s);
                break;
            case whileToken:
                acs = AspWhileStmt.parse(s);
                break;
            case defToken:
                acs = AspFuncDef.parse(s);
                break;
            default:
                parserError("Expected a compund statement, but found a " + s.curToken().kind, s.curLineNum());
        }
        leaveParser("compound stmt");
        return acs;
    }

}