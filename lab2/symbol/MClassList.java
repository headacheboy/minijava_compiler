package symbol;
import java.util.HashMap;
import java.util.HashSet;

//MClassList内存放了所有的类，以供查询

public class MClassList extends MType
{
    private HashMap<String, MClass> mClasses = new HashMap<String, MClass>();

    public MClassList()
    {
        super("null");
    }

    //将类放入hashmap中，如果存在重复类，则返回false，否则插入成功返回true
    public boolean insertClass(MClass mClass)
    {
        String className = mClass.getName();
        if (mClasses.containsKey(className))
        {
            return false;
        }
        mClasses.put(className, mClass);
        return true;
    }

    //根据名字查找类
    public MClass getClass(String name)
    {
        if (!mClasses.containsKey(name))
        {
            return null;
        }
        else
        {
            return mClasses.get(name);
        }
    }

    // 是否定义名为name的类
    public boolean containClass(String name) {
        return mClasses.containsKey(name);
    }

    //查看是否有重复，循环继承类
    public boolean check()
    {
        boolean debugMode = true;
        if (debugMode)
        {
            print();
        }
        HashSet<String> hashSet = new HashSet<String>();    //已经检查过的类，放在这里
        for (String s:mClasses.keySet())
        {
            if (!hashSet.contains(s))
            {
                hashSet.add(s);
                MClass tmpClass = mClasses.get(s);
                String nextClass = tmpClass.getParent();
                while (!nextClass.equals("Object"))
                {
                    if (!mClasses.containsKey(nextClass))
                    {
                        System.out.println("父类不存在"+" "+tmpClass.getLine()+"行"+tmpClass.getColumn()+"列");
                        return false;
                    }
                    if (mClasses.get(nextClass).getName().equals(s))
                    {
                        System.out.println("循环继承"+" "+tmpClass.getLine()+"行"+tmpClass.getColumn()+"列");
                        return false;
                    }
                    hashSet.add(nextClass);
                    tmpClass = mClasses.get(nextClass);
                    nextClass = tmpClass.getParent();
                }
            }
        }
        for (String s:mClasses.keySet())
        {
            MClass tmpClass = mClasses.get(s);
            for (MMethod mMethod:tmpClass.getMethodMap().values())
            {
                if (!checkMethod(tmpClass, mMethod))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkMethod(MClass tmpClass, MMethod mMethod)
    {
        String parentName = tmpClass.getParent();
        while (!parentName.equals("Object"))
        {
            tmpClass = mClasses.get(parentName);
            if (tmpClass.hasMethod(mMethod.getName()))
            {
                MMethod tmpMethod = tmpClass.getMethod(mMethod.getName());
                if (mMethod.checkPara(tmpMethod.getOrderPara()) && mMethod.getType().equals(tmpMethod.getType()))
                {
                    //同名且参数类型、顺序相同
                    return true;
                }
                else
                {
                    //同名但参数不同，是重载
                    System.out.println("父类子类不能重载，类型参数、返回值要相同");
                    return false;
                }
            }
            else
            {
                parentName = tmpClass.getParent();
            }
        }
        // 到这里表示父类、祖父类等没有同名变量
        return true;
    }

    public void updateParentClass() {
        for (String s:mClasses.keySet()) {
            String parentName = mClasses.get(s).getParent();
            if (parentName != null) {
                mClasses.get(s).setParentClass(mClasses.get(parentName));
            } else {
                mClasses.get(s).setParentClass(null);
            }
        }
    }

    //测试用，输出class，method和variable
    public boolean print()
    {
        HashSet<String> hashset = new HashSet<String>();
        for (String s:mClasses.keySet())
        {
            System.out.print(s);
            MClass tmpClass = mClasses.get(s);
            if (!tmpClass.getParent().equals("Object"))
                System.out.println(" extends from "+tmpClass.getParent());
            else
            {
                System.out.println();
            }
            tmpClass.print(1);
        }
        return true;
    }
}   
