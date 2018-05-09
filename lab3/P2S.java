import java.io.*;
import symbol.*;
import visitor.*;
import syntaxtree.*;

public class P2S {
    public static void main(String []args) {
        try {
            InputStream in = new FileInputStream(args[0]);
            Node root = new PigletParser(in).Goal();
            Allsp argu = new Place("ALL", false);
            root.accept(new P2spVisitor(), argu);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
