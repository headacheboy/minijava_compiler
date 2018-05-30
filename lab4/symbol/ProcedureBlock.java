package symbol;

import java.util.HashMap;
import java.util.Vector;

public class ProcedureBlock {
    public String pname;
    public int paranum;
    public int useStack = 0;
    public int inCall = 0;

    // TEMP to Registers
    public HashMap<Integer, String> regCandi = new HashMap<Integer, String>();
    // TEMP unused but appears, so skip in MOVEstmt
    public Vector<Integer> regSkip = new Vector<Integer>();
    // Reg used in this procedure, so store them and reload them then
    public Vector<String> regSave = new Vector<String>();
    // TEMP that store in SPILLEDARG
    public HashMap<Integer, String> regStack = new HashMap<Integer, String>();
    // used to register allocation
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
