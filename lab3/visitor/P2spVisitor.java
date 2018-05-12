package visitor;
import symbol.*;
import syntaxtree.*;
import java.util.*;

public class P2spVisitor extends GJDepthFirst<Allsp, Allsp> {
    int tmpNum = 1000;
    int getTmpNum() {
        // return tmp tmpNum unused
        tmpNum = tmpNum + 1;
        return tmpNum;
    }

    public Allsp visit(NodeList n, Allsp argu) {
        Allsp _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
        }
        return _ret;
    }

    public Allsp visit(NodeListOptional n, Allsp argu) {
        SpigletResultList _ret = new SpigletResultList();
        if ( n.present() ) {
            for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                SpigletResult res = (SpigletResult)e.nextElement().accept(this, new Place("notCALL", true));
                if (((Place)argu).stmt.equals("CALL")) {
                    // store result only in CALL to return parameters
                    if (!res.isTemp()) {
                        int newTemp = getTmpNum();
                        System.out.println("MOVE TEMP " + newTemp + " " + res);
                        res = new SpigletResult("TEMP " + newTemp, true);
                    }
                    _ret.addResult(res);
                }
            }
        }
        return _ret;
    }

    public Allsp visit(NodeOptional n, Allsp argu) {
        if ( n.present() ) {
            Allsp _ret = n.node.accept(this,argu);
            System.out.print(_ret + " "); // (Label)?
            return _ret;
        }
        else
            return null;
    }

    public Allsp visit(NodeSequence n, Allsp argu) {
        Allsp _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
        }
        return _ret;
    }

    public Allsp visit(NodeToken n, Allsp argu) { return null; }

    //
    // User-generated visitor methods below
    //

    /**
     * f0 -> "MAIN"
     * f1 -> StmtList()
     * f2 -> "END"
     * f3 -> ( Procedure() )*
     * f4 -> <EOF>
     */
    public Allsp visit(Goal n, Allsp argu) {
        Allsp _ret=null;
        // get the max tmp number in pg file
        SpigletResult maxNumStr = (SpigletResult)n.accept(new GetMaxTmp(), null);
        tmpNum = Integer.parseInt(maxNumStr.toString()) + 1;
        n.f0.accept(this, argu);
        System.out.println("MAIN");
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        System.out.println("END");
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ( ( Label() )? Stmt() )*
     */
    public Allsp visit(StmtList n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> Label()
     * f1 -> "["
     * f2 -> IntegerLiteral()
     * f3 -> "]"
     * f4 -> StmtExp()
     */
    public Allsp visit(Procedure n, Allsp argu) {
        Allsp _ret=null;
        SpigletResult name = (SpigletResult)n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        SpigletResult paras = (SpigletResult)n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        System.out.println(name + " [ " + paras + " ]");
        // StmtExp only appears in Procedure, so print BEGIN ... END here
        System.out.println("BEGIN");
        SpigletResult res = (SpigletResult)n.f4.accept(this, argu);
        System.out.println("RETURN " + res);
        System.out.println("END");
        return _ret;
    }

    /**
     * f0 -> NoOpStmt()
     *       | ErrorStmt()
     *       | CJumpStmt()
     *       | JumpStmt()
     *       | HStoreStmt()
     *       | HLoadStmt()
     *       | MoveStmt()
     *       | PrintStmt()
     */
    public Allsp visit(Stmt n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "NOOP"
     */
    public Allsp visit(NoOpStmt n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        System.out.println("NOOP");
        return _ret;
    }

    /**
     * f0 -> "ERROR"
     */
    public Allsp visit(ErrorStmt n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        System.out.println("ERROR");
        return _ret;
    }

    /**
     * f0 -> "CJUMP"
     * f1 -> Exp()
     * f2 -> Label()
     */
    public Allsp visit(CJumpStmt n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        SpigletResult exp = (SpigletResult)n.f1.accept(this, argu);
        if (!exp.isTemp()) {
            // needs temp number
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp);
            exp = new SpigletResult("TEMP " + newTemp, true);
        }
        SpigletResult label = (SpigletResult)n.f2.accept(this, argu);
        System.out.println("CJUMP " + exp + " " + label);
        return _ret;
    }

    /**
     * f0 -> "JUMP"
     * f1 -> Label()
     */
    public Allsp visit(JumpStmt n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        SpigletResult label = (SpigletResult)n.f1.accept(this, argu);
        System.out.println("JUMP " + label);
        return _ret;
    }

    /**
     * f0 -> "HSTORE"
     * f1 -> Exp()
     * f2 -> IntegerLiteral()
     * f3 -> Exp()
     */
    public Allsp visit(HStoreStmt n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        SpigletResult exp1 = (SpigletResult)n.f1.accept(this, argu);
        if (!exp1.isTemp()) {
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp1);
            exp1 = new SpigletResult("TEMP " + newTemp, true);
        }
        n.f2.accept(this, argu);
        SpigletResult exp2 = (SpigletResult)n.f3.accept(this, argu);
        if (!exp2.isTemp()) {
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp2);
            exp2 = new SpigletResult("TEMP " + newTemp, true);
        }
        System.out.println("HSTORE " + exp1 + " " + n.f2.f0 + " " + exp2);
        return _ret;
    }

    /**
     * f0 -> "HLOAD"
     * f1 -> Temp()
     * f2 -> Exp()
     * f3 -> IntegerLiteral()
     */
    public Allsp visit(HLoadStmt n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        SpigletResult tmp = (SpigletResult)n.f1.accept(this, argu);
        SpigletResult exp = (SpigletResult)n.f2.accept(this, argu);
        if (!exp.isTemp()) {
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp);
            exp = new SpigletResult("TEMP " + newTemp, true);
        }
        n.f3.accept(this, argu);
        System.out.println("HLOAD " + tmp + " " + exp + " " + n.f3.f0);
        return _ret;
    }

    /**
     * f0 -> "MOVE"
     * f1 -> Temp()
     * f2 -> Exp()
     */
    public Allsp visit(MoveStmt n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        SpigletResult tmp = (SpigletResult)n.f1.accept(this, argu);
        SpigletResult exp = (SpigletResult)n.f2.accept(this, argu);
        System.out.println("MOVE " + tmp + " " + exp);
        return _ret;
    }

    /**
     * f0 -> "PRINT"
     * f1 -> Exp()
     */
    public Allsp visit(PrintStmt n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        SpigletResult exp = (SpigletResult)n.f1.accept(this, argu);
        if (!exp.isSimple()) {
            // needs simple exp
            // TEMP number is OK
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp);
            exp = new SpigletResult("TEMP " + newTemp, true);
        }
        System.out.println("PRINT " + exp);
        return _ret;
    }

    /**
     * f0 -> StmtExp()
     *       | Call()
     *       | HAllocate()
     *       | BinOp()
     *       | Temp()
     *       | IntegerLiteral()
     *       | Label()
     */
    public Allsp visit(Exp n, Allsp argu) {
        Allsp _ret=null;
        // return to UPPER to see whether it is simple or tmp
        _ret = n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "BEGIN"
     * f1 -> StmtList()
     * f2 -> "RETURN"
     * f3 -> Exp()
     * f4 -> "END"
     */
    public Allsp visit(StmtExp n, Allsp argu) {
        // BEGIN RETURN and END are printed in Procedure
        Allsp _ret=null;
        n.f0.accept(this, argu);
        SpigletResult stmtlist = (SpigletResult)n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        SpigletResult exp = (SpigletResult)n.f3.accept(this, argu);
        if (!exp.isSimple()) {
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp);
            exp = new SpigletResult("TEMP " + newTemp, true);
        }
        n.f4.accept(this, argu);
        _ret = new SpigletResult(exp.toString(), true);
        return _ret;
    }

    /**
     * f0 -> "CALL"
     * f1 -> Exp()
     * f2 -> "("
     * f3 -> ( Exp() )*
     * f4 -> ")"
     */
    public Allsp visit(Call n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        SpigletResult exp1 = (SpigletResult)n.f1.accept(this, argu);
        if (!exp1.isSimple()) {
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp1);
            exp1 = new SpigletResult("TEMP " + newTemp, true);
        }
        n.f2.accept(this, argu);
        // get TEMP list
        SpigletResultList exp2LO = (SpigletResultList)n.f3.accept(this, new Place("CALL", true));
        n.f4.accept(this, argu);
        _ret = new SpigletResult("CALL " + exp1 + " ( " + exp2LO + ")", false);
        return _ret;
    }

    /**
     * f0 -> "HALLOCATE"
     * f1 -> Exp()
     */
    public Allsp visit(HAllocate n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        SpigletResult exp = (SpigletResult)n.f1.accept(this, argu);
        if (!exp.isSimple()) {
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp);
            exp = new SpigletResult("TEMP " + newTemp, true);
        }
        _ret = new SpigletResult("HALLOCATE " + exp, false);
        return _ret;
    }

    /**
     * f0 -> Operator()
     * f1 -> Exp()
     * f2 -> Exp()
     */
    public Allsp visit(BinOp n, Allsp argu) {
        Allsp _ret=null;
        SpigletResult op = (SpigletResult)n.f0.accept(this, argu);
        SpigletResult exp1 = (SpigletResult)n.f1.accept(this, argu);
        if (!exp1.isTemp()) {
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp1);
            exp1 = new SpigletResult("TEMP " + newTemp, true);
        }
        SpigletResult exp2 = (SpigletResult)n.f2.accept(this, argu);
        if (!exp2.isSimple()) {
            int newTemp = getTmpNum();
            System.out.println("MOVE TEMP " + newTemp + " " + exp2);
            exp2 = new SpigletResult("TEMP " + newTemp, true);
        }
        _ret = new SpigletResult(op + " " + exp1 + " " + exp2, false);
        return _ret;
    }

    /**
     * f0 -> "LT"
     *       | "PLUS"
     *       | "MINUS"
     *       | "TIMES"
     */
    public Allsp visit(Operator n, Allsp argu) {
        // return Operator
        Allsp _ret=null;
        int which = n.f0.which;
        String op;
        if (which == 0) {
            op = "LT";
        } else if (which == 1) {
            op = "PLUS";
        } else if (which == 2) {
            op = "MINUS";
        } else {
            op = "TIMES";
        }
        _ret = new SpigletResult(op, false);
        return _ret;
    }

    /**
     * f0 -> "TEMP"
     * f1 -> IntegerLiteral()
     */
    public Allsp visit(Temp n, Allsp argu) {
        // return TEMP number as it is
        Allsp _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        _ret = new SpigletResult("TEMP " + n.f1.f0, true);
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public Allsp visit(IntegerLiteral n, Allsp argu) {
        // return int
        Allsp _ret=null;
        n.f0.accept(this, argu);
        _ret = new SpigletResult(n.f0.toString(), true);
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public Allsp visit(Label n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        _ret = new SpigletResult(n.f0.toString(), true);
        return _ret;
    }

}
