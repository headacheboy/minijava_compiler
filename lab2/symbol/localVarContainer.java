package symbol;
import java.util.*;

public class localVarContainer extends MType
{
    public HashSet<String> allVar;
    public HashSet<String> initVar;

    public localVarContainer(MMethod m)
    {
        super(null);
        allVar = new HashSet<>();
        initVar = new HashSet<>();
        Vector<MVar> t = m.getOrderVar();
        int len = t.size();
        for (int i = 0; i < len; ++i)
        {
            allVar.add(t.get(i).getName());
        }
    }

    public localVarContainer(localVarContainer varContainer)
    {
        super(null);
        allVar = new HashSet<>();
        initVar = new HashSet<>();
        for (String s:varContainer.allVar)
        {
            allVar.add(s);
        }
        for (String s:varContainer.initVar)
        {
            initVar.add(s);
        }
    }

    public boolean checkInit(String name)
    {
        /*
        for (String s:allVar)
        {
            System.out.print(s+" ");
        }
        System.out.println("");
        for (String s:initVar)
        {
            System.out.print(s+" ");
        }*/
        if (allVar.contains(name))
        {
            if (initVar.contains(name))
                return true;
            return false;
        }
        return true;
    }

    public void clear()
    {
        allVar.clear();
        initVar.clear();
    }
}