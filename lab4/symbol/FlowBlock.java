package symbol;
import java.util.*;

public class FlowBlock
{
    public HashSet<FlowBlock> pre = new HashSet<FlowBlock>();
    public HashSet<FlowBlock> next = new HashSet<FlowBlock>();
    public int No;
    public HashSet<Integer> left = new HashSet<>();
    public HashSet<Integer> right = new HashSet<>();
    public HashSet<Integer> In = new HashSet<>();
    public HashSet<Integer> Out = new HashSet<>();

    public FlowBlock(int n)
    {
        No = n;
    }

}