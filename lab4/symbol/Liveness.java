package symbol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.*;

public class Liveness
{
    public Liveness(){}

    public void analysis(HashMap<String, FlowGraph> flowGraphHashMap)
    {
        for (Entry<String, FlowGraph> entry: flowGraphHashMap.entrySet())
        {
            FlowGraph flowGraph = entry.getValue();
            boolean flag = true;
            while (flag)
            {
                flag = false;
                for (int i = flowGraph.No-1; i > 0; --i)
                {
                    FlowBlock flowBlock = flowGraph.getBlock(i);
                    HashSet<Integer> newHashSet = new HashSet<>();
                    flowBlock.Out.clear();
                    for (FlowBlock tmpNext: flowBlock.next)
                    {
                        for (Integer integer: tmpNext.In)
                        {
                            flowBlock.Out.add(integer);
                        }
                    }
                    for (Integer integer:flowBlock.Out)
                    {
                        if (!flowBlock.left.contains(integer))
                        {
                            newHashSet.add(integer);
                        }
                    }
                    for (Integer integer:flowBlock.right)
                    {
                        newHashSet.add(integer);
                    }
                    if (!check(newHashSet, flowBlock.In))
                    {
                        flowBlock.In = newHashSet;
                        flag = true;
                    }
                }
            }
        }
    }

    public boolean check(HashSet<Integer> hashSet1, HashSet<Integer> hashSet2)
    {
        if (hashSet1.size() != hashSet2.size())
            return false;
        for (Integer i: hashSet1)
        {
            if (!hashSet2.contains(i))
                return false;
        }
        return true;
    }
}