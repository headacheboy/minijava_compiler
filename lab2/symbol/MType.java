package symbol;

//表示类型的类

public class MType
{
    protected String typeName;
    
    public MType(String n)
    {
        typeName = n;
    }
    
    
    public String getType()
    {
        return typeName;
    }
    public void setType(String ty)
    {
        typeName = ty;
    }
}
