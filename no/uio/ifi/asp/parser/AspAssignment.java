
package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;

public class AspAssignment extends AspSmallStmt {
    AspName an;
    ArrayList<AspSubscription> subscriptions = new ArrayList<>();
    AspExpr expr;

    AspAssignment(int n) {
        super(n);
    }

    static AspAssignment parse(Scanner s) {
        enterParser("assignment");

        AspAssignment aa = new AspAssignment(s.curLineNum());
        aa.an = AspName.parse(s);

        while (s.curToken().kind == leftBracketToken) { // subscription må begynne med left bracket. fordi det kan være
                                                        // flere subscriptions brukes en while-loop
            aa.subscriptions.add(AspSubscription.parse(s));
        }
        skip(s, equalToken);
        aa.expr = AspExpr.parse(s);
        leaveParser("assignment");
        return aa;
    }

    @Override
    void prettyPrint() {
        an.prettyPrint();
        for (AspSubscription as : subscriptions) {
            as.prettyPrint();
        }
        prettyWrite(" = ");
        expr.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if (subscriptions.isEmpty()) {
            RuntimeValue v = expr.eval(curScope);
            trace(an.t.name + " = " + v.showInfo());
            curScope.assign(an.t.name, v);
        } else if (subscriptions.size() == 1) {
            RuntimeValue a = an.eval(curScope);
            RuntimeValue indexExpr = subscriptions.get(0).eval(curScope);
            RuntimeValue valueExpr = expr.eval(curScope);
            trace(an.t.name + "[" + indexExpr + "] = " + valueExpr.showInfo());
            a.evalAssignElem(indexExpr, valueExpr, this);
        } else if (subscriptions.size() > 1) {
            RuntimeValue a = an.eval(curScope);

            int i = 0;
            RuntimeValue indexExpr = null;
            RuntimeValue valueExpr = expr.eval(curScope);
            while (i < subscriptions.size() - 1) {
                indexExpr = subscriptions.get(i).eval(curScope);
                a.evalSubscription(indexExpr, this);
                i++;
            }
            trace(an.t.name + subscriptions + " = " + valueExpr.showInfo());
            a.evalAssignElem(subscriptions.get(i).eval(curScope), valueExpr, this);
        }
        return null;

    }
}
