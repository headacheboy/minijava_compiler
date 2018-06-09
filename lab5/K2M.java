import java.io.*;
import visitor.*;
import syntaxtree.*;
import java.util.*;

public class K2M {
    public static void main(String []args) {
        try {
            InputStream in = new FileInputStream(args[0]);
            Node root = new KangaParser(in).Goal();
            root.accept(new K2Mvisitor());
            util_func();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    static void util_func() {
        System.out.println("        .text");
        System.out.println("        .globl _halloc");
        System.out.println("_halloc:");
        System.out.println("        li $v0, 9");
        System.out.println("        syscall");
        System.out.println("        j $ra");
        System.out.println("");
        System.out.println("        .text");
        System.out.println("        .globl _print");
        System.out.println("_print:");
        System.out.println("        li $v0, 1");
        System.out.println("        syscall");
        System.out.println("        la $a0, newl");
        System.out.println("        li $v0, 4");
        System.out.println("        syscall");
        System.out.println("        j $ra");
        System.out.println("");
        System.out.println("        .data");
        System.out.println("        .align   0");
        System.out.println("newl:   .asciiz \"\\n\"");
        System.out.println("        .data");
        System.out.println("        .align   0");
        System.out.println("str_er: .asciiz \" ERROR: abnormal termination\\n\"");
    }
}
