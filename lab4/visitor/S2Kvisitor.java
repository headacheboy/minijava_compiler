package visitor;
import syntaxtree.*;
import symbol.*;
import java.util.*;
import java.util.Vector;

public class S2Kvisitor extends GJDepthFirst<Object, Object> {
    public void println(String str) {
        System.out.println(str);
    }
    public void print(String str) {
        System.out.print(str);
    }
    public String getReg(int tmpNum) {
        return "t0";
    }
    public int storeS07(int stackpos) {
        for (int i=0; i<=7; ++i) {
            println("ASTORE s" + i + " (SPILLEDARG " + stackpos + ")");
            stackpos = stackpos + 1;
        }
        return stackpos;
    }
    public int loadS07(int stackpos) {
        for (int i=7; i>=0; --i) {
            println("ALOAD s" + i + " (SPILLEDARG " + stackpos + ")");
            stackpos = stackpos - 1;
        }
        return stackpos;
    }
    public Object visit(NodeList n, Object argu) {
        Object _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
        }
        return _ret;
    }

    public Object visit(NodeListOptional n, Object argu) {
        if ( n.present() ) {
            Vector<String> _ret= new Vector<String>();
            int _count=0;
            for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                String reg = (String)e.nextElement().accept(this,argu);
                _ret.add(reg);
                _count++;
            }
            return _ret;
        }
        else
            return null;
    }

    public Object visit(NodeOptional n, Object argu) {
        if ( n.present() ) {
            Object _ret = n.node.accept(this,argu);
            print(_ret + " "); // (Label)?
            return _ret;
        }
        else
            return null;
    }

    public Object visit(NodeSequence n, Object argu) {
        Object _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
        }
        return _ret;
    }

    public Object visit(NodeToken n, Object argu) { return null; }

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
    public Object visit(Goal n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        println("MAIN [][][]");
        n.f1.accept(this, argu);
        //n.f2.accept(this, argu);
        println("END");
        n.f3.accept(this, argu);
        //n.f4.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> ( ( Label() )? Stmt() )*
     */
    public Object visit(StmtList n, Object argu) {
        Object _ret=null;
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
    public Object visit(Procedure n, Object argu) {
        Object _ret=null;
        String pname = (String)n.f0.accept(this, argu);
        //n.f1.accept(this, argu);
        int paranum = Integer.parseInt((String)n.f2.accept(this, argu));
        println(pname + " [" + paranum + "][" + "][" + "]");
        for (int i=0; i<=3 && i < paranum; ++i) {
            println("MOVE s" + i + " a" + i);
        }
        if (paranum > 4) {
            // TODO
        }
        //n.f3.accept(this, argu);
        String reg = (String)n.f4.accept(this, argu);
        println("MOVE v0 " + reg);
        println("END");
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
    public Object visit(Stmt n, Object argu) {
        Object _ret=null;
        n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "NOOP"
     */
    public Object visit(NoOpStmt n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        println("NOOP");
        return _ret;
    }

    /**
     * f0 -> "ERROR"
     */
    public Object visit(ErrorStmt n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        println("ERROR");
        return _ret;
    }

    /**
     * f0 -> "CJUMP"
     * f1 -> Temp()
     * f2 -> Label()
     */
    public Object visit(CJumpStmt n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        String reg = (String)n.f1.accept(this, argu);
        String label = (String)n.f2.accept(this, argu);
        println("CJUMP " + reg + " " + label);
        return _ret;
    }

    /**
     * f0 -> "JUMP"
     * f1 -> Label()
     */
    public Object visit(JumpStmt n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        String label = (String)n.f1.accept(this, argu);
        println("JUMP " + label);
        return _ret;
    }

    /**
     * f0 -> "HSTORE"
     * f1 -> Temp()
     * f2 -> IntegerLiteral()
     * f3 -> Temp()
     */
    public Object visit(HStoreStmt n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        String reg1 = (String)n.f1.accept(this, argu);
        int bias = Integer.parseInt((String)n.f2.accept(this, argu));
        String reg2 = (String)n.f3.accept(this, argu);
        println("HSTORE " + reg1 + " " + bias + " " + reg2);
        return _ret;
    }

    /**
     * f0 -> "HLOAD"
     * f1 -> Temp()
     * f2 -> Temp()
     * f3 -> IntegerLiteral()
     */
    public Object visit(HLoadStmt n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        String reg1 = (String)n.f1.accept(this, argu);
        String reg2 = (String)n.f2.accept(this, argu);
        int bias = Integer.parseInt((String)n.f3.accept(this, argu));
        println("HLOAD " + reg1 + " " + reg2 + " " + bias);
        return _ret;
    }

    /**
     * f0 -> "MOVE"
     * f1 -> Temp()
     * f2 -> Exp()
     */
    public Object visit(MoveStmt n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        String reg1 = (String)n.f1.accept(this, argu);
        String reg2 = (String)n.f2.accept(this, argu);
        println("MOVE " + reg1 + " " + reg2);
        return _ret;
    }

    /**
     * f0 -> "PRINT"
     * f1 -> SimpleExp()
     */
    public Object visit(PrintStmt n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        String sexp = (String)n.f1.accept(this, argu);
        println("PRINT " + sexp);
        return _ret;
    }

    /**
     * f0 -> Call()
     *       | HAllocate()
     *       | BinOp()
     *       | SimpleExp()
     */
    public Object visit(Exp n, Object argu) {
        Object _ret=null;
        _ret = n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "BEGIN"
     * f1 -> StmtList()
     * f2 -> "RETURN"
     * f3 -> SimpleExp()
     * f4 -> "END"
     */
    public Object visit(StmtExp n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        //n.f2.accept(this, argu);
        String sexp = (String)n.f3.accept(this, argu);
        //n.f4.accept(this, argu);
        _ret = sexp;
        return _ret;
    }

    /**
     * f0 -> "CALL"
     * f1 -> SimpleExp()
     * f2 -> "("
     * f3 -> ( Temp() )*
     * f4 -> ")"
     */
    public Object visit(Call n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        String sexp = (String)n.f1.accept(this, argu);
        //n.f2.accept(this, argu);
        Vector<String> paras = (Vector<String>)n.f3.accept(this, argu);
        int size = paras.size();
        for (int i=0; i<size && i<=3; ++i) {
            println("MOVE a" + i + " " + paras.get(i));
        }
        if (size > 4) {
            for (int i=4; i<=size; ++i) {
                println("PASSARG " + (i-3) + " " + paras.get(i));
            }
        }
        //storeS07(1);
        //n.f4.accept(this, argu);
        println("CALL " + sexp);
        _ret = "v0";
        //loadS07(8);
        return _ret;
    }

    /**
     * f0 -> "HALLOCATE"
     * f1 -> SimpleExp()
     */
    public Object visit(HAllocate n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        String sexp = (String)n.f1.accept(this, argu);
        _ret = "HALLOCATE " + sexp;
        return _ret;
    }

    /**
     * f0 -> Operator()
     * f1 -> Temp()
     * f2 -> SimpleExp()
     */
    public Object visit(BinOp n, Object argu) {
        Object _ret=null;
        String op = (String)n.f0.accept(this, argu);
        String reg = (String)n.f1.accept(this, argu);
        String sexp = (String)n.f2.accept(this, argu);
        _ret = op + " " + reg + " " + sexp;
        return _ret;
    }

    /**
     * f0 -> "LT"
     *       | "PLUS"
     *       | "MINUS"
     *       | "TIMES"
     */
    public Object visit(Operator n, Object argu) {
        Object _ret=null;
        //n.f0.accept(this, argu);
        int choice = n.f0.which;
        if (choice == 0) {
            return "LT";
        } else if (choice == 1) {
            return "PLUS";
        } else if (choice == 2) {
            return "MINUS";
        } else if (choice == 3) {
            return "TIMES";
        }
        return _ret;
    }

    /**
     * f0 -> Temp()
     *       | IntegerLiteral()
     *       | Label()
     */
    public Object visit(SimpleExp n, Object argu) {
        Object _ret=null;
        _ret = n.f0.accept(this, argu);
        return _ret;
    }

    /**
     * f0 -> "TEMP"
     * f1 -> IntegerLiteral()
     */
    public Object visit(Temp n, Object argu) {
        Object _ret=null;
        n.f0.accept(this, argu);
        int tmpNum = Integer.parseInt((String)n.f1.accept(this, argu));
        _ret = getReg(tmpNum);
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public Object visit(IntegerLiteral n, Object argu) {
        Object _ret=null;
        _ret = n.f0.toString();
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public Object visit(Label n, Object argu) {
        Object _ret=null;
        _ret = n.f0.toString();
        return _ret;
    }

}
