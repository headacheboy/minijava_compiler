package visitor;
import syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class K2Mvisitor extends GJNoArguDepthFirst<String> {
    //
    // Auto class visitors--probably don't need to be overridden.
    //
    void println(String str) {
        System.out.println(str);
    }
    void print(String str) {
        System.out.print(str);
    }
    int curArgs = 0;
    int curStack = 0;
    int curCall = 0;
    public String visit(NodeList n) {
        String _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
        }
        return _ret;
    }

    public String visit(NodeListOptional n) {
        if ( n.present() ) {
            String _ret=null;
            int _count=0;
            for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
                e.nextElement().accept(this);
                _count++;
            }
            return _ret;
        }
        else
            return null;
    }

    public String visit(NodeOptional n) {
        if ( n.present() ) {
            String label = n.node.accept(this);
            print(label + ": ");
            return n.node.accept(this);
        } else
            return null;
    }

    public String visit(NodeSequence n) {
        String _ret=null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
            _count++;
        }
        return _ret;
    }

    public String visit(NodeToken n) { return null; }

    //
    // User-generated visitor methods below
    //

    /**
     * f0 -> "MAIN"
     * f1 -> "["
     * f2 -> IntegerLiteral()
     * f3 -> "]"
     * f4 -> "["
     * f5 -> IntegerLiteral()
     * f6 -> "]"
     * f7 -> "["
     * f8 -> IntegerLiteral()
     * f9 -> "]"
     * f10 -> StmtList()
     * f11 -> "END"
     * f12 -> ( Procedure() )*
     * f13 -> <EOF>
     */
    public String visit(Goal n) {
        String _ret=null;
        println("        .text");
        println("        .globl main");
        println("main:");
        println("move $fp, $sp");
        curArgs = Integer.parseInt(n.f2.accept(this));
        int spill = Integer.parseInt(n.f5.accept(this));
        curStack = spill;
        int passargs = Integer.parseInt(n.f8.accept(this));
        curCall = passargs;
        int stacknum = 4;
        stacknum += (spill * 4);
        if (passargs > 4) {
            stacknum += ((passargs - 4) * 4);
        }
        println("subu $sp, $sp, " + stacknum);
        println("sw $ra, -4($fp)");
        n.f10.accept(this);
        println("lw $ra, -4($fp)");
        println("addu $sp, $sp, " + stacknum);
        println("j $ra\n");
        n.f12.accept(this);
        return _ret;
    }

    /**
     * f0 -> ( ( Label() )? Stmt() )*
     */
    public String visit(StmtList n) {
        String _ret=null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> Label()
     * f1 -> "["
     * f2 -> IntegerLiteral()
     * f3 -> "]"
     * f4 -> "["
     * f5 -> IntegerLiteral()
     * f6 -> "]"
     * f7 -> "["
     * f8 -> IntegerLiteral()
     * f9 -> "]"
     * f10 -> StmtList()
     * f11 -> "END"
     */
    public String visit(Procedure n) {
        String _ret=null;
        String pname = n.f0.accept(this);
        println("        .text");
        println("        .globl " + pname);
        println(pname + ":");
        println("sw $fp, -8($sp)");
        println("move $fp, $sp");
        curArgs = Integer.parseInt(n.f2.accept(this));
        int stacknum = 8;
        int spill = Integer.parseInt(n.f5.accept(this));
        curStack = spill;
        int passargs = Integer.parseInt(n.f8.accept(this));
        curCall = passargs;
        stacknum += (spill * 4);
        if (passargs > 4) {
            stacknum += ((passargs - 4) * 4);
        }
        println("subu $sp, $sp, " + stacknum);
        println("sw $ra, -4($fp)");
        n.f10.accept(this);
        println("lw $ra, -4($fp)");
        println("lw $fp, " + (stacknum - 8) + "($sp)");
        println("addu $sp, $sp, " + stacknum);
        println("j $ra\n");
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
     *       | ALoadStmt()
     *       | AStoreStmt()
     *       | PassArgStmt()
     *       | CallStmt()
     */
    public String visit(Stmt n) {
        String _ret=null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "NOOP"
     */
    public String visit(NoOpStmt n) {
        String _ret=null;
        println("nop");
        return _ret;
    }

    /**
     * f0 -> "ERROR"
     */
    public String visit(ErrorStmt n) {
        String _ret=null;
        println("li $v0, 4");
        println("la $a0, str_er");
        println("syscall");
        println("li $v0, 10");
        println("syscall");
        return _ret;
    }

    /**
     * f0 -> "CJUMP"
     * f1 -> Reg()
     * f2 -> Label()
     */
    public String visit(CJumpStmt n) {
        String _ret=null;
        String reg = n.f1.accept(this);
        String label = n.f2.accept(this);
        println("beqz " + reg + " " + label);
        return _ret;
    }

    /**
     * f0 -> "JUMP"
     * f1 -> Label()
     */
    public String visit(JumpStmt n) {
        String _ret=null;
        String label = n.f1.accept(this);
        println("b " + label);
        return _ret;
    }

    /**
     * f0 -> "HSTORE"
     * f1 -> Reg()
     * f2 -> IntegerLiteral()
     * f3 -> Reg()
     */
    public String visit(HStoreStmt n) {
        String _ret=null;
        String reg1 = n.f1.accept(this);
        String bias = n.f2.accept(this);
        String reg2 = n.f3.accept(this);
        println("sw " + reg2 + ", " + bias + "(" + reg1 + ")");
        return _ret;
    }

    /**
     * f0 -> "HLOAD"
     * f1 -> Reg()
     * f2 -> Reg()
     * f3 -> IntegerLiteral()
     */
    public String visit(HLoadStmt n) {
        String _ret=null;
        String reg1 = n.f1.accept(this);
        String reg2 = n.f2.accept(this);
        String bias = n.f3.accept(this);
        println("lw " + reg1 + ", " + bias + "(" + reg2 + ")");
        return _ret;
    }

    /**
     * f0 -> "MOVE"
     * f1 -> Reg()
     * f2 -> Exp()
     */
    public String visit(MoveStmt n) {
        String _ret=null;
        String reg = n.f1.accept(this);
        String exp = n.f2.accept(this);
        int which = n.f2.f0.which;
        if (which == 0) { // HAllocate
            println("move " + reg + ", $v0");
        } else if (which == 1) { // BinOp
            String []moveexp = exp.split(",");
            println(moveexp[0] + " " + reg + ", " + moveexp[1] + ", " + moveexp[2]);
        } else { // SimpleExp
            String []sexps = exp.split(",");
            int sexpwhich = Integer.parseInt(sexps[1]);
            if (sexpwhich == 0) { // reg
                println("move " + reg + ", " + sexps[0]);
            } else if (sexpwhich == 1) { // int
                println("li " + reg + ", " + sexps[0]);
            } else { // label
                println("la " + reg + ", " + sexps[0]);
            }
        }
        return _ret;
    }

    /**
     * f0 -> "PRINT"
     * f1 -> SimpleExp()
     */
    public String visit(PrintStmt n) {
        String _ret = null;
        String sexp = n.f1.accept(this);
        String []sexps = sexp.split(",");
        int which = Integer.parseInt(sexps[1]);
        if (which == 0) { // reg
            println("move $a0, " + sexps[0]);
        } else if (which == 1) { // int
            println("li $a0, " + sexps[0]);
        } else { // label
            // TODO
            println("print label");
        }
        println("jal _print");
        return _ret;
    }

    /**
     * f0 -> "ALOAD"
     * f1 -> Reg()
     * f2 -> SpilledArg()
     */
    public String visit(ALoadStmt n) {
        String _ret=null;
        String reg = n.f1.accept(this);
        int spill = Integer.parseInt(n.f2.accept(this));
        int spill4 = 4 * spill;
        if (curArgs > 4 && (spill < (curArgs - 4))) {
            println("lw " + reg + ", " + spill4 + "($fp)");
            return _ret;
        }
        int startpos = spill4;
        if (curCall > 4) {
            startpos += (4 * (curCall - 4));
        }
        println("lw " + reg + ", " + startpos + "($sp)");
        return _ret;
    }

    /**
     * f0 -> "ASTORE"
     * f1 -> SpilledArg()
     * f2 -> Reg()
     */
    public String visit(AStoreStmt n) {
        String _ret=null;
        int spill = Integer.parseInt(n.f1.accept(this));
        String reg = n.f2.accept(this);
        int spill4 = 4 * spill;
        if (curArgs > 4 && (spill < (curArgs - 4))) {
            println("sw " + reg + ", " + spill4 + "($fp)");
            return _ret;
        }
        int startpos = spill4;
        if (curCall > 4) {
            startpos += (4 * (curCall - 4));
        }
        println("sw " + reg + ", " + startpos + "($sp)");
        return _ret;
    }

    /**
     * f0 -> "PASSARG"
     * f1 -> IntegerLiteral()
     * f2 -> Reg()
     */
    public String visit(PassArgStmt n) {
        String _ret=null;
        int passpos = Integer.parseInt(n.f1.accept(this));
        String reg = n.f2.accept(this);
        println("sw " + reg + ", " + (4*passpos-4) + "($sp)");
        return _ret;
    }

    /**
     * f0 -> "CALL"
     * f1 -> SimpleExp()
     */
    public String visit(CallStmt n) {
        String _ret=null;
        String sexp = n.f1.accept(this);
        String []sexps = sexp.split(",");
        //int which = Integer.parseInt(sexps[1]);
        //if (which == 0) { // reg
        //    println("move $a0, " + sexps[0]);
        //} else if (which == 1) { // int
        //    println("li $a0, " + sexps[0]);
        //} else { // label
        //    // TODO
        //    println("print label");
        //}
        println("jalr " + sexps[0]);
        return _ret;
    }

    /**
     * f0 -> HAllocate()
     *       | BinOp()
     *       | SimpleExp()
     */
    public String visit(Exp n) {
        String _ret=null;
        _ret = n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "HALLOCATE"
     * f1 -> SimpleExp()
     */
    public String visit(HAllocate n) {
        String _ret=null;
        String sexp = n.f1.accept(this);
        String []sexps = sexp.split(",");
        int which = Integer.parseInt(sexps[1]);
        if (which == 0) { // reg
            println("move $a0, " + sexps[0]);
        } else if (which == 1) { // int
            println("li $a0, " + sexps[0]);
        } else { // label
            // TODO
            println("print label");
        }
        println("jal _halloc");
        return _ret;
    }

    /**
     * f0 -> Operator()
     * f1 -> Reg()
     * f2 -> SimpleExp()
     */
    public String visit(BinOp n) {
        String _ret=null;
        String []ops = {"slt", "add", "sub", "mul"};
        String reg = n.f1.accept(this);
        String exp = n.f2.accept(this);
        int which = n.f0.f0.which;
        _ret = ops[which] + "," + reg + "," + exp;
        return _ret;
    }

    /**
     * f0 -> "LT"
     *       | "PLUS"
     *       | "MINUS"
     *       | "TIMES"
     */
    public String visit(Operator n) {
        String _ret=null;
        n.f0.accept(this);
        return _ret;
    }

    /**
     * f0 -> "SPILLEDARG"
     * f1 -> IntegerLiteral()
     */
    public String visit(SpilledArg n) {
        String _ret=null;
        _ret = n.f1.accept(this);
        return _ret;
    }

    /**
     * f0 -> Reg()
     *       | IntegerLiteral()
     *       | Label()
     */
    public String visit(SimpleExp n) {
        String _ret=null;
        int which = n.f0.which;
        _ret = n.f0.accept(this) + "," + which;
        return _ret;
    }

    /**
     * f0 -> "a0"
     *       | "a1"
     *       | "a2"
     *       | "a3"
     *       | "t0"
     *       | "t1"
     *       | "t2"
     *       | "t3"
     *       | "t4"
     *       | "t5"
     *       | "t6"
     *       | "t7"
     *       | "s0"
     *       | "s1"
     *       | "s2"
     *       | "s3"
     *       | "s4"
     *       | "s5"
     *       | "s6"
     *       | "s7"
     *       | "t8"
     *       | "t9"
     *       | "v0"
     *       | "v1"
     */
    public String visit(Reg n) {
        String _ret=null;
        int which = n.f0.which;
        String[] regs = {"a0", "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8", "t9", "v0", "v1"};
        _ret = "$" + regs[which];
        return _ret;
    }

    /**
     * f0 -> <INTEGER_LITERAL>
     */
    public String visit(IntegerLiteral n) {
        String _ret=null;
        _ret = n.f0.toString();
        return _ret;
    }

    /**
     * f0 -> <IDENTIFIER>
     */
    public String visit(Label n) {
        String _ret=n.f0.toString();
        return _ret;
    }

}

//       |     PassArgStmt
