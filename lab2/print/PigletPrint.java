package print;

import symbol.MClass;
import visitor.Minijava2PigletVisitor;

public class PigletPrint {
    private static int tabNum = 0;
    private static boolean newLine = false;

    public static void print(String s)
    {
        for (int i = 0; i < tabNum; ++i)
            System.out.print("  ");
        System.out.print(s);
    }

    public static void println(String s)
    {
        print(s);
        System.out.println();
    }

    public static void printBegin()
    {
        println("BEGIN");
        tabNum++;
    }

    public static void printEnd()
    {
        tabNum--;
        println("END");
    }

    public static void printReturn()
    {
        print("RETURN ");
    }

    public static void printMain()
    {
        println("MAIN");
        tabNum++;
    }

    public static void printMethod(String className, String methodName, int paraNum)
    {
        println(className+"_"+methodName+"["+paraNum+"]");
    }
}