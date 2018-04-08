import java.io.*;
import visitor.*;
import syntaxtree.*;
import symbol.*;
public class Main
{
    public static void main(String []args)
    {
        try
        {
            InputStream in = new FileInputStream(args[0]);
            Node root = new MiniJavaParser(in).Goal();
            MClassList allClassList = new MClassList();
            root.accept(new BuildSymbolTableVisitor(), allClassList); //把符号表插入到allClassList中
            allClassList.updateParentClass();
            root.accept(new Minijava2PigletVisitor(), allClassList);  //
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        /*
        try
        {
            InputStream in = new FileInputStream(args[0]);
            Node root = new MiniJavaParser(in).Goal();
            root.accept(new BuildSymbolTableVisitor());
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }*/
    }
}
/*
class MyDepthVisitor extends DepthFirstVisitor
{
    public void visit(VarDeclaration n) {
        System.out.println(n.f1.getClass().getName());
        Identifier id = (Identifier)n.f1;
        System.out.println("VarName: "+id.f0.toString());
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
    }
}*/



/* for testing
public class main
{
    public static void main(String []args)
    {
        try
        {
            InputStream in = new FileInputStream(args[0]);
            Node root = new MiniJavaParser(in).Goal();
            MType allClassList = new MClassList();
            root.accept(new BuildSymbolTableVisitor(), allClassList); //把符号表插入到allClassList中
            root.accept(new TypeCheckVisitor(), allClassList);  //检查错误
            if (ErrorPrinter.getSize() == 0)
            {
                System.out.println("Program type checked successfully");
            }
            else
            {
                System.out.println("Type error");
            }
            ErrorPrinter.printAll();
        }
    }
}
*/
