import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.*;

import symbol.*;
import visitor.*;
import syntaxtree.*;

class Main
{
    public static void main(String []args)
    {
        HashMap<String, FlowGraph> flowGraphHashMap = new HashMap<String, FlowGraph>();
        FlowGraph currentFlowGraph;
        try
        {
            InputStream in = new FileInputStream(args[0]);
            Node root = new SpigletParser(in).Goal();
            root.accept(new BuildNode(flowGraphHashMap));
            System.out.println(flowGraphHashMap.get("MAIN").No);
            root.accept(new BuildNode2(flowGraphHashMap));
            Liveness liveness = new Liveness();
            liveness.analysis(flowGraphHashMap);
            currentFlowGraph = flowGraphHashMap.get("MAIN");
            for (Entry<Integer, FlowBlock> entry: currentFlowGraph.mBlock.entrySet())
            {
                HashSet<Integer> left = entry.getValue().left, right = entry.getValue().right, IN = entry.getValue().In,
                OUT = entry.getValue().Out;
                System.out.print(entry.getKey()+" in: ");
                for (Integer integer: IN)
                {
                    System.out.print(integer+" ");
                }
                System.out.print("out: ");
                for (Integer integer: OUT)
                {
                    System.out.print(integer+" ");
                }
                System.out.print(" left: ");
                for (Integer integer: left)
                {
                    System.out.print(integer+" ");
                }
                System.out.print("right: ");
                for (Integer integer: right)
                {
                    System.out.print(integer+" ");
                }
                System.out.println("");
                System.out.print("pre: ");
                HashSet<FlowBlock> hashSet = entry.getValue().pre;
                HashSet<FlowBlock> hashSet2 = entry.getValue().next;
                for (FlowBlock flowBlock:hashSet)
                {
                    System.out.print(flowBlock.No+" ");
                }
                System.out.print("next: ");
                for (FlowBlock flowBlock:hashSet2)
                {
                    System.out.print(flowBlock.No+" ");
                }
                System.out.println("");
            }
            for (Entry<String, Integer> entry: currentFlowGraph.mLabel.entrySet())
            {
                System.out.println(entry.getKey()+" "+entry.getValue());
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
