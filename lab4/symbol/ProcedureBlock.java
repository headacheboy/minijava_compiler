package symbol;

import java.util.HashMap;
import java.util.Vector;

public class ProcedureBlock {
    public String pname;
    public int paranum;
    public int useStack = 0;
    public int inCall = 0;

    public HashMap<Integer, String> regCandi = new HashMap<Integer, String>();
    public Vector<Integer> regSkip = new Vector<Integer>();
    public HashMap<Integer, String> regStack = new HashMap<Integer, String>();
    public HashMap<Integer, Liveinterval> tmpMap = new HashMap<Integer, Liveinterval>();
    public ProcedureBlock() {

    }
    public void lazyInit(String pname_, int paranum_) {
        pname = pname_;
        paranum = paranum_;
    }
    public ProcedureBlock (String pname_, int paranum_) {
        pname = pname_;
        paranum = paranum_;
    }
}
