package visitor;
import symbol.*;
import syntaxtree.*;
import java.io.*;

public class BuildSymbolTableVisitor extends GJDepthFirst<MType,MType>    //A是附带的作用域
{
    //将user-generated method拷贝过来重写，删掉没有改过的

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
    //由于主类默认第一个一定是main函数，所以手动添加...主类没有类变量？
   public MType visit(MainClass n, MType argu) {
      //System.out.println("in main Class");
      MType _ret=null;
      boolean flag;
      MIdentifier mIdentifier, varIdentifier;
      MClass mainClass;
      MMethod mainMethod;
      MVar mVar;
      n.f0.accept(this, argu);
      mIdentifier = (MIdentifier)n.f1.accept(this, argu);
      mainClass = new MClass(mIdentifier.getLine(), mIdentifier.getColumn(), mIdentifier.getName(), "Object");
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      varIdentifier = (MIdentifier)n.f11.accept(this, argu);
      mainMethod = new MMethod(mIdentifier.getLine(), mIdentifier.getColumn(), "void", "main", mainClass);
      mVar = new MVar(varIdentifier.getLine(), varIdentifier.getColumn(), "StringArray", varIdentifier.getName(), true, mainMethod);
      mainMethod.insertVar(mVar);
      n.f12.accept(this, argu);
      n.f13.accept(this, argu);
      n.f14.accept(this, mainMethod);
      n.f15.accept(this, mainMethod);
      n.f16.accept(this, argu);
      n.f17.accept(this, argu);
      mainClass.insertMethod(mainMethod);
      flag = ((MClassList)argu).insertClass(mainClass);
      if (!flag)
      {
         System.out.println("重复定义类"+" "+mainClass.getLine()+"行"+mainClass.getColumn()+"列");
      }
      return _ret;
   }


   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   //由于禁止内部类，这里的argu只能是classList
   public MType visit(ClassDeclaration n, MType argu) {
      //System.out.println("in class declaration");
      boolean flag;
      MType _ret=null;
      MIdentifier mIdentifier;
      MClass mClass;
      n.f0.accept(this, argu);
      mIdentifier = (MIdentifier)n.f1.accept(this, argu);
      mClass = new MClass(mIdentifier.getLine(), mIdentifier.getColumn(), mIdentifier.getName(), "Object");
      flag = ((MClassList)argu).insertClass(mClass);
      if (!flag)
      {
         System.out.println("重复定义类"+" "+mClass.getLine()+"行"+mClass.getColumn()+"列");
         System.exit(10086);
         return _ret;
      }
      n.f2.accept(this, argu);
      n.f3.accept(this, mClass);    //修改作用域
      n.f4.accept(this, mClass);
      n.f5.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
    //由于禁止内部类，这里的argu只能是classList
   public MType visit(ClassExtendsDeclaration n, MType argu) {
      //System.out.println("in class extend declaration");
      MType _ret=null;
      boolean flag;
      String parentClassName;
      MClass mClass, parentClass;
      MIdentifier mIdentifier, parentIdentifier;
      n.f0.accept(this, argu);
      mIdentifier = (MIdentifier)n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      parentIdentifier = (MIdentifier)n.f3.accept(this, argu);
      parentClassName = parentIdentifier.getName();
      mClass = new MClass(mIdentifier.getLine(), mIdentifier.getColumn(), mIdentifier.getName(), parentClassName);
      flag = ((MClassList)argu).insertClass(mClass);
      if (!flag)
      {
         System.out.println("重复定义类");
         System.exit(10086);
         return _ret;
      }
      n.f4.accept(this, argu);  
      n.f5.accept(this, mClass);  //修改argu为当前class
      n.f6.accept(this, mClass);  //修改argu为当前class
      n.f7.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    //这一层执行插入表操作，将定义的变量插入method/class中
   public MType visit(VarDeclaration n, MType argu) {
      //System.out.println("in variable declaration");
      boolean flag = true;
      String typeName;
      MType _ret=null;
      MVar mVar;
      MIdentifier mIdentifier;
      typeName = ((MType)(n.f0.accept(this, argu))).getType();  //
      mIdentifier = (MIdentifier)(n.f1.accept(this, argu)); //这两行，接受底层回传的type和identifier
      mVar = new MVar(mIdentifier.getLine(), mIdentifier.getColumn(), typeName, mIdentifier.getName(), false, argu);   //未初始化
      flag = ((MIdentifier)(argu)).insertVar(mVar); //多态
      if (!flag)
      {
          System.out.println("重复定义变量/方法"+" "+mVar.getLine()+"行"+mVar.getColumn()+"列");
          System.exit(10086);
          return _ret;
      }
      return _ret;
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public MType visit(MethodDeclaration n, MType argu) {
      //System.out.println("in method declaration");
      MType _ret=null;
      boolean flag;
      MType type;
      MIdentifier mIdentifier;
      MMethod method;
      n.f0.accept(this, argu);
      type = (MType)n.f1.accept(this, argu);
      mIdentifier = (MIdentifier)n.f2.accept(this, argu);
      method = new MMethod(mIdentifier.getLine(), mIdentifier.getColumn(), type.getType(), mIdentifier.getName(), (MClass)argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, method);    //注意这里不是argu了，而是当前定义的method，因为是要将参数，插入到method中
      n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, method);  //同上，这里要将临时变量插入method中
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      flag = ((MClass)argu).insertMethod(method);
      if (!flag)
      {
         System.out.println("重复定义方法/变量"+" "+method.getLine()+"行"+method.getColumn()+"列");
         System.exit(10086);
         return _ret;
      }
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
    //函数参数插入到method中
   public MType visit(FormalParameter n, MType argu) {
      boolean flag = true;
      String typeName;
      MType _ret=null;
      MVar mVar;
      MIdentifier mIdentifier;
      typeName = ((MType)(n.f0.accept(this, argu))).getType();  //
      mIdentifier = (MIdentifier)(n.f1.accept(this, argu)); //这两行，接受底层回传的type和identifier
      mVar = new MVar(mIdentifier.getLine(), mIdentifier.getColumn(), typeName, mIdentifier.getName(), true, argu);   //函数参数默认初始化
      flag = ((MMethod)(argu)).insertPara(mVar); //插入
      if (!flag)
      {
          System.out.println("重复定义函数参数"+" "+mVar.getLine()+"行"+mVar.getColumn()+"列");
          System.exit(10086);
          return _ret;
      }
      return _ret;
   }


   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   //这一层接着往上传
   public MType visit(Type n, MType argu) {
      MType _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }
   //下述三个类型：array，boolean和int，分别作成MType传回去
   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public MType visit(ArrayType n, MType argu) {
      MType _ret=null;
      _ret = new MType("array");
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "boolean"
    */
   public MType visit(BooleanType n, MType argu) {
      MType _ret=null;
      _ret = new MType("boolean");
      return _ret;
   }

   /**
    * f0 -> "int"
    */
   public MType visit(IntegerType n, MType argu) {
      MType _ret=null;
      _ret = new MType("int");
      return _ret;
   }


   /**
    * f0 -> <IDENTIFIER>
    */
   public MType visit(Identifier n, MType argu) {
      String name = n.f0.toString();
      MType _ret=null;
      _ret = new MIdentifier(n.f0.beginLine, n.f0.beginColumn, name, name);
      //将identifier的名字以及行列回传
      return _ret;
   }
}
