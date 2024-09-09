package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;

abstract class AspAtom extends AspSyntax {
    TokenKind tk;

    AspAtom(int n) {
        super(n);
    }

    // kode hentet fra PPT høst 2022 uke37 - Dag Langmyhr & Ragnhild Runde
    static AspAtom parse(Scanner s) {
        enterParser("atom");
        AspAtom aa = null;
        TokenKind t = s.curToken().kind;
        switch (t) {
            case falseToken:
            case trueToken:
                aa = AspBooleanLiteral.parse(s);
                aa.tk = t;
                break;
            case floatToken:
                aa = AspFloatLiteral.parse(s);
                aa.tk = t;
                break;
            case integerToken:
                aa = AspIntegerLiteral.parse(s);
                aa.tk = t;
                break;
            case leftBraceToken:
                aa = AspDictDisplay.parse(s);
                aa.tk = t;
                break;
            case leftBracketToken:
                aa = AspListDisplay.parse(s);
                aa.tk = t;
                break;
            case leftParToken:
                aa = AspInnerExpr.parse(s);
                aa.tk = t;
                break;
            case nameToken:
                aa = AspName.parse(s);
                aa.tk = t;
                break;
            case noneToken:
                aa = AspNoneLiteral.parse(s);
                aa.tk = t;
                break;
            case stringToken:
                aa = AspStringLiteral.parse(s);
                aa.tk = t;
                break;
            default:
                parserError("Expected an expression atom but found a " +
                        s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("atom");
        return aa;
    }
}