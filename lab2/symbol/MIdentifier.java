package symbol;
import java.io.*;

public class MIdentifier extends MType
{
    protected int line, column;
    protected String name;
    public MIdentifier(int l, int c, String tName, String n)
    {
        super(tName);
        line = l;
        column = c;
        name = n;
    }
    public int getLine()
    {
        return line;
    }
    public int getColumn()
    {
        return column;
    }
    public String getName()
    {
        return name;
    }
    
    public void setLine(int a)
    {
        line = a;
    }
    public void setColumn(int a)
    {
        column = a;
    }
    
    public boolean insertVar(MVar mVar)
    {
        System.out.println("this should not appear!");
        return true;
    }
}
