import java.io.*;
import symbol.*;
import visitor.*;
import syntaxtree.*;
import java.util.*;

class S2K {
    public static void main(String []args) {
        try {
            HashMap<String, FlowGraph> flowGraphHashMap = new HashMap<String, FlowGraph>();
            InputStream in = new FileInputStream(args[0]);
            Node root = new SpigletParser(in).Goal();
            root.accept(new BuildNode(flowGraphHashMap));
            root.accept(new BuildNode2(flowGraphHashMap));
            Liveness liveness = new Liveness();
            liveness.analysis(flowGraphHashMap);
            RegAlloc regalloc = new RegAlloc(flowGraphHashMap);
            regalloc.alloc();
            root.accept(new S2Kvisitor(), flowGraphHashMap);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
