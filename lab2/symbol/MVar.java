package symbol;

public class MVar extends MIdentifier
{
    public MType owner;    //所属类or方法
    public boolean init;           //是否初始化
    
    public MVar(int l, int c, String tName, String n, boolean ini, MType o)
    {
        super(l, c, tName, n);
        init = ini;
        owner = o;
    }
}