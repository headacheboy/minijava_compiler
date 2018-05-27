package symbol;

import java.util.*;

public class FlowGraph
{
    public HashMap<Integer, FlowBlock> mBlock = new HashMap<Integer, FlowBlock>();
    public HashMap<String, Integer> mLabel = new HashMap<String, Integer>();
    public String name;
    public ProcedureBlock pBlock = new ProcedureBlock();
    public int No;

    public FlowGraph(String n){
        name = n;
    }

    public void addBlock(int No)
    {
        FlowBlock flowBlock = new FlowBlock(No);
        mBlock.put(No, flowBlock);
    }

    public void addEdge(int src, int dst)
    {
        FlowBlock flowBlock1, flowBlock2;
        flowBlock1 = mBlock.get(src);
        flowBlock2 = mBlock.get(dst);
        flowBlock1.next.add(flowBlock2);
        flowBlock2.pre.add(flowBlock1);
    }

    public FlowBlock getBlock(int No)
    {
        return mBlock.get(No);
    }

}
