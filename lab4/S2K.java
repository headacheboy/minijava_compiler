import java.io.*;
import symbol.*;
import visitor.*;
import syntaxtree.*;

class S2K {
    public static void main(String []args) {
        try {
            InputStream in = new FileInputStream(args[0]);
            Node root = new SpigletParser(in).Goal();
            root.accept(new S2Kvisitor(), null);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
