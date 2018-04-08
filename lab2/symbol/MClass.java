package symbol;
import java.util.HashMap;
import java.util.Vector;

public class MClass extends MIdentifier
{
    private HashMap<String, MVar> mVarMap = new HashMap<String, MVar>();
    public Vector<MVar> mVarVec = new Vector<MVar>();
    private HashMap<String, MMethod> mMethodMap = new HashMap<String, MMethod>();
    public Vector<MMethod> mMethodVec = new Vector<MMethod>();
    private String parent;  //父类，可以为NULL，即默认的Object父类
    private MClass parentClass;
    
    //此外，继承的MIdentifier的typeName，默认是Class
    public MClass(int l, int c, String n, String p)
    {
        super(l, c, "class", n);
        parent = p;
    }
    
    public boolean insertVar(MVar mVar)
    {
        String tmpName = mVar.getName();        
        //System.out.println(tmpName);
        if (mVarMap.containsKey(tmpName))
        {
            return false;
        }
        else
        {
            mVarVec.add(mVar);
            mVarMap.put(tmpName, mVar);
            return true;
        }
    }
    
    public boolean insertMethod(MMethod mMethod)
    {
        String tmpName = mMethod.getName();
        //System.out.println(tmpName);
        if (mMethodMap.containsKey(tmpName))
        {
            return false;
        }
        else
        {
            mMethodVec.add(mMethod);
            mMethodMap.put(tmpName, mMethod);
            return true;
        }
    }

    public boolean hasMethod(String name)
    {
        if (mMethodMap.containsKey(name)) {
            return true;
        }
        if (parentClass != null) {
            return parentClass.hasMethod(name);
        }
        return false;
    }

    /*
    public MMethod getMethod(String name)
    {
        if (mMethodMap.containsKey(name)) {
            return mMethodMap.get(name);
        }
        if (parentClass != null) {
            return parentClass.getMethod(name);
        }
        return null;
    }*/

    public MMethod getMethod(String name)
    {
        //不考虑父类的方法的情况暂时
        return mMethodMap.get(name);
    }

    public MMethod getMethod(int i)
    {
        //不考虑父类的方法的情况暂时
        //提供了用下标查找的方法
        return mMethodVec.get(i);
    }

    public int getMethodClassPos(MClassList mClassList, String name, MClass whichClass)
    {
        MClass tmpClass = this;
        int ret = 0;
        while (true)
        {
            if (tmpClass.getMethod(name) != null)
            {
                whichClass = mClassList.getClass(tmpClass.getName());
                return ret * 4;
            }
            ret += tmpClass.mVarVec.size()+1;
            tmpClass = tmpClass.getParentClass();
        }
    }

    public int getMethodPos(String name)
    {
        //这里只要判断自己的method，上面一个函数判断了在哪个class里
        int len = mMethodVec.size();
        for (int i = 0; i < len; ++i)
        {
            if (name.equals(mMethodVec.get(i).getName()))
            {
                return 4*i;
            }
        }
        return -1;
    }

    public int getVarPos(MClassList mClassList, String name)
    {
        //获取name变量在class的哪个位置，注意如果是父类的变量，则需要往上寻找
        //类实例表的结构为：先dtable，再属性，若有父类，则父类按照同样的规则接在当前vtable后
        MClass tmpClass=this;
        int sum = 0;
        int tmp;
        while (true)
        {
            tmp = tmpClass.getVarIdx(name);
            if (tmp != -1)
                break;
            sum += 1+tmpClass.mVarVec.size();
            tmpClass = tmpClass.getParentClass();
        }
        return (1+sum+tmp)*4;
    }

    public int getVarIdx(String name)
    {
        int len = mVarVec.size();
        for (int i = 0; i < len; ++i)
        {
            if (name.equals(mVarVec.get(i).getName()))
                return i;
        }
        return -1;
    }

    public String getParent()
    {
        return parent;
    }
    public MClass getParentClass()
    {
        return parentClass;
    }
    public void setParentClass(MClass mClass)
    {
        parentClass = mClass;
    }

    public MType getVarType(String var) {
        if (mVarMap.containsKey(var)) {
            return mVarMap.get(var);
        }
        if (parentClass != null) {
            return parentClass.getVarType(var);
        }
        return null;
    }

    public boolean hasVar(String var) {
        if (mVarMap.containsKey(var)) {
            return true;
        }
        if (parentClass != null) {
            return parentClass.hasVar(var);
        }
        return false;
    }

    //测试用，输出var和method名称
    public void print(int i)
    {
        for (String s:mVarMap.keySet())
        {
            for (int j = 0; j < i; ++j)
                System.out.print("  ");
            System.out.println(s+"(var, "+mVarMap.get(s).getType()+" "+mVarMap.get(s).init+")"+"----"+((MClass)mVarMap.get(s).owner).getName());
        }
        for (String s:mMethodMap.keySet())
        {
            for (int j = 0; j < i; ++j)
                System.out.print("  ");
            System.out.println(s+"(method, "+mMethodMap.get(s).getType()+")"+"----"+((MClass)mMethodMap.get(s).owner).getName());
            MMethod tmpMethod = mMethodMap.get(s);
            tmpMethod.print(i+1);
        }
    }

    public HashMap<String, MMethod> getMethodMap()
    {
        return mMethodMap;
    }
}
