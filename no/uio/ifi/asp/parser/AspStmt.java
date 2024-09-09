// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

abstract class AspStmt extends AspSyntax {

    AspStmt(int n) {
        super(n);
    }

    static AspStmt parse(Scanner s) {
        enterParser("stmt");

        AspStmt as = null;

        switch (s.curToken().kind) {
            case forToken:
            case ifToken:
            case whileToken:
            case defToken:
                as = AspCompoundStmt.parse(s); // parser 'compund atmt'
                break;
            default:
                as = AspSmallStmtList.parse(s); // hvis ikke 'compound stmt' forsøker den å parse 'small stmt'
        }
        leaveParser("stmt");
        return as;
    }
}
