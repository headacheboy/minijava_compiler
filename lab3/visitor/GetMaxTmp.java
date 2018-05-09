package visitor;
import symbol.*;
import syntaxtree.*;
import java.util.*;

public class GetMaxTmp extends GJDepthFirst<Allsp, Allsp> {
    int maxTmpNum = -1;
    /**
     * f0 -> "MAIN"
     * f1 -> StmtList()
     * f2 -> "END"
     * f3 -> ( Procedure() )*
     * f4 -> <EOF>
     */
    public Allsp visit(Goal n, Allsp argu) {
        Allsp _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        _ret = new SpigletResult(Integer.toString(maxTmpNum), true);
        return _ret;
    }
    /**
     * f0 -> "TEMP"
     * f1 -> IntegerLiteral()
     */
    public Allsp visit(Temp n, Allsp argu) {
        Allsp _ret=null;
        int hereNum = Integer.parseInt(n.f1.f0.toString());
        if (hereNum > maxTmpNum) {
            maxTmpNum = hereNum;
        }
        return _ret;
    }
}
