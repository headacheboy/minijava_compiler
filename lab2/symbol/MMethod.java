package symbol;
import java.util.HashMap;
import java.util.Vector;

public class MMethod extends MIdentifier
{
    public MClass owner;   //所在类
    private HashMap<String, MVar> mPara = new HashMap<String, MVar>();
    private Vector<MVar> orderPara = new Vector<MVar>();  //记录顺序
    private HashMap<String, MVar> tmpVar = new HashMap<String, MVar>();
    private Vector<MVar> orderVar = new Vector<MVar>();
    
    public MMethod(int l, int c, String retType, String n, MClass o)
    {
        super(l, c, retType, n);
        owner = o;
    }

    public int getParaIdx(String varName)
    {
        int len = orderPara.size();
        for (int i = 0; i < len; ++i)
        {
            MVar tmpVar = orderPara.get(i);
            if (tmpVar.getName().equals(varName))
            {
                return i;
            }
        }
        System.exit(10086);
        return 0;
    }

    public int getParaNum()
    {
        return orderPara.size();
    }
    
    public boolean insertPara(MVar mVar)
    {
        //如果有重名的变量，则返回false，否则插入成功返回true
        String name = mVar.getName();
        if (tmpVar.containsKey(name) || mPara.containsKey(name))
        {
            return false;
        }
        else
        {
            mPara.put(name, mVar);
            orderPara.add(mVar);
        }
        return true;
    }
    
    public boolean insertVar(MVar mVar)
    {
        //如果有重名的临时变量，则返回false，否则插入成功返回true
        String name = mVar.getName();
        if (tmpVar.containsKey(name) || mPara.containsKey(name))
        {
            return false;
        }
        else
        {
            tmpVar.put(name, mVar);
            orderVar.add(mVar);
        }
        return true;
    }

    public MType getVarType(String name) {
        if (mPara.containsKey(name)) {
            return mPara.get(name);
        }
        if (tmpVar.containsKey(name)) {
            return tmpVar.get(name);
        }
        return owner.getVarType(name);
    }

    public boolean hasPara(String name)
    {
        if (mPara.containsKey(name))
            return true;
        return false;
    }

    public boolean hasVar(String name)
    {
        if (tmpVar.containsKey(name))
            return true;
        return false;
    }

    public boolean checkPara(Vector<MVar> tmpPara)
    {
        int len = orderPara.size();
        if (tmpPara.size() != len)
            return false;
        for (int i = 0; i < len; ++i)
        {
            if (!orderPara.get(i).getType().equals(tmpPara.get(i).getType()))
                return false;
        }
        return true;
    }

    public Vector<MVar> getOrderPara()
    {
        return orderPara;
    }
    public Vector<MVar> getOrderVar()
    {
        return orderVar;
    }
    public MClass getOwner() {
        return owner;
    }

    //测试用
    public void print(int i)
    {
        for (MType m: orderPara)
        {
            System.out.println(getName()+"___"+m.getType()+"_____");
        }
        for (String s:mPara.keySet())
        {
            for (int j = 0; j < i; ++j)
                System.out.print("  ");
            System.out.println(s+"(para, "+mPara.get(s).getType()+" "+
                    mPara.get(s).init+")"+"----"+((MMethod)mPara.get(s).owner).getName());
        }
        for (String s:tmpVar.keySet())
        {
            for (int j = 0; j < i; ++j)
                System.out.print("  ");
            System.out.println(s + "(var, " + tmpVar.get(s).getType() + " " +tmpVar .get(s).init+ ")" + "----" + ((MMethod) (tmpVar.get(s).owner)).getName());
        }
    }
}
