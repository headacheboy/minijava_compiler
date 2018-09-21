package visitor;
import symbol.*;
import syntaxtree.*;
import java.util.Vector;
import java.util.HashMap;

public class TypeCheckVisitor2 extends GJDepthFirst<MType, MType> {
    // 存储ClassList，因为之后的argu会变
    private MClassList allClassList;
    private boolean debug = false;
    // 存放参数类型列表
    private Vector<String> methodParam = new Vector<String>();
    // argu: allClassList，此时在class identifier()处
    public MType visit(ClassDeclaration n, MType argu) {
        if (debug) {
            System.out.println("ClassDeclaration");
        }
        MType _ret = null;
        n.f0.accept(this, argu);
        MType classType = n.f1.accept(this, argu);
        // 得到的classType: MIdentifier，类型为类名，名字为实例名
        MClass hereClass = ((MClassList)argu).getClass(((MIdentifier)classType).getType());
        n.f2.accept(this, argu);
        n.f3.accept(this, hereClass);
        n.f4.accept(this, hereClass);
        n.f5.accept(this, argu);
        return _ret;
    }
    public MType visit(ClassExtendsDeclaration n, MType argu) {
        if (debug) {
            System.out.println("ClassExtendsDeclaration");
        }
        MType _ret = null;
        n.f0.accept(this, argu);
        MType classType = n.f1.accept(this, argu);
        MClass hereClass = ((MClassList)argu).getClass(((MIdentifier)classType).getType());
        n.f2.accept(this, argu);
        MType extsClassType = n.f3.accept(this, argu);
        MClass extsClass = ((MClassList)argu).getClass(((MIdentifier)extsClassType).getType());
        if (extsClass == null) {
            errorPrint("extends class doesn't exist");
        }
        // 检查是否有函数重载
        checkOverload(hereClass, extsClass);
        n.f4.accept(this, argu);
        n.f5.accept(this, hereClass);
        n.f6.accept(this, hereClass);
        n.f7.accept(this, argu);
        return _ret;
    }
    public MType visit(MainClass n, MType argu) {
        // MainClass中的"public"等字符串不再遍历
        MType _ret = null;
        if (debug) {
            System.out.println("MainClass");
        }
        MType classType = n.f1.accept(this, argu);
        MClass hereClass = allClassList.getClass(((MIdentifier)classType).getType());
        MMethod hereMethod = hereClass.getMethod("main");
        n.f11.accept(this, hereMethod);
        n.f14.accept(this, hereMethod);
        n.f15.accept(this, hereMethod);
        return _ret;
    }
    // 写这个只是为了存下allClassList
    public MType visit(Goal n, MType argu) {
        if (debug) {
            System.out.println("Goal");
        }
        allClassList = (MClassList)argu;
        // 建立符号表后，更新类的父类，以便getVarType上溯
        allClassList.updateParentClass();
        if (debug) {
            allClassList.print();
        }
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return new MType("root");
    }
    public MType visit(MethodDeclaration n, MType argu) {
        if (debug) {
            System.out.println("MethodDeclaration");
        }
        MType _ret = null;
        n.f0.accept(this, argu);

        MType type = n.f1.accept(this, (MType)allClassList);
        // 检查返回的类型是否存在
        checkTypeDeclared(type, argu);

        MIdentifier ret2 = (MIdentifier)n.f2.accept(this, argu);
        MMethod hereMethod = ((MClass)argu).getMethod(ret2.getName());
        if (debug) {
            System.out.println("**********************      "+hereMethod.getName());
            System.out.println("**********************      "+type.getType());
        }
        // 设置函数的返回类型，似乎是多余的，在建立符号表的时候应该就好了
        hereMethod.setType(type.getType());

        n.f3.accept(this, hereMethod);
        n.f4.accept(this, hereMethod);
        n.f5.accept(this, hereMethod);
        n.f6.accept(this, hereMethod);
        n.f7.accept(this, hereMethod);
        n.f8.accept(this, hereMethod);
        n.f9.accept(this, hereMethod);

        MType retExpType = n.f10.accept(this, hereMethod);
        // 检查返回值的类型是否与函数声明一致
        checkRetType(retExpType, type, "Return expression doesn't match return type");

        n.f11.accept(this, hereMethod);
        n.f12.accept(this, hereMethod);

        return _ret;
    }

    // 遍历此处，设置变量的类型，似乎是多余的，理由同函数的返回类型
    // 只是确保
    public MType visit(VarDeclaration n, MType argu) {
        if (debug) {
            System.out.println("VarDeclaration");
        }
        MType _ret = null;
        MType ltype = n.f0.accept(this, (MType)allClassList);
        MType rvar = n.f1.accept(this, null);
        if (debug) {
            System.out.println(ltype.getType());
            System.out.println(((MIdentifier)rvar).getName());
            System.out.println(((MIdentifier)rvar).getType());
        }
        MType rtype = null;
        if (argu instanceof MMethod) {
            // 建立符号表的时候name为类名，保持一致
            rtype = ((MMethod)argu).getVarType(((MIdentifier)rvar).getName());
        } else if (argu instanceof MClass) {
            rtype = ((MClass)argu).getVarType(((MIdentifier)rvar).getName());
        }
        rtype.setType(ltype.getType());
        return _ret;
    }
    // 得到类型，只有int,boolean,array,自定义类四种
    public MType visit(Type n, MType argu) {
        return n.f0.accept(this, argu);
    }
    public MType visit(ArrayType n, MType argu) {
        // array为int[]
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return new MType("array");
    }
    public MType visit(BooleanType n, MType argu) {
        return new MType("boolean");
    }
    public MType visit(IntegerType n, MType argu) {
        return new MType("int");
    }

    public MType visit(ExpressionList n, MType argu) {
        MType _ret = null;
        // 得到这个函数调用时的参数类型
        MType hereParam = n.f0.accept(this, argu);
        methodParam.add(hereParam.getType());
        n.f1.accept(this, argu);
        return _ret;
    }
    public MType visit(ExpressionRest n, MType argu) {
        MType _ret = null;
        n.f0.accept(this, argu);
        MType hereParam = n.f1.accept(this, argu);
        methodParam.add(hereParam.getType());
        return _ret;
    }
    public MType visit(Expression n, MType argu) {
        return n.f0.accept(this, argu);
    }
    public MType visit(AndExpression n, MType argu) {
        MType lhs = n.f0.accept(this, argu);
        if (!lhs.getType().equals("boolean")) {
            errorPrint("should be boolean && boolean");
        }
        n.f1.accept(this, argu);
        MType rhs = n.f2.accept(this, argu);
        if (!rhs.getType().equals("boolean")) {
            errorPrint("should be boolean && boolean");
        }
        return new MType("boolean");
    }
    public MType visit(CompareExpression n, MType argu) {
        MType lhs = n.f0.accept(this, argu);
        if (!lhs.getType().equals("int")) {
            errorPrint("should be int < int");
        }
        n.f1.accept(this, argu);
        MType rhs = n.f2.accept(this, argu);
        if (!rhs.getType().equals("int")) {
            errorPrint("should be int < int");
        }
        return new MType("boolean");
    }
    public MType visit(PlusExpression n, MType argu) {
        MType lhs = n.f0.accept(this, argu);
        if (!lhs.getType().equals("int")) {
            errorPrint("should be int + int");
        }
        n.f1.accept(this, argu);
        MType rhs = n.f2.accept(this, argu);
        if (!rhs.getType().equals("int")) {
            errorPrint("should be int + int");
        }
        return new MType("int");
    }
    public MType visit(MinusExpression n, MType argu) {
        MType lhs = n.f0.accept(this, argu);
        if (!lhs.getType().equals("int")) {
            errorPrint("should be int - int");
        }
        n.f1.accept(this, argu);
        MType rhs = n.f2.accept(this, argu);
        if (!rhs.getType().equals("int")) {
            errorPrint("should be int - int");
        }
        return new MType("int");
    }
    public MType visit(TimesExpression n, MType argu) {
        MType lhs = n.f0.accept(this, argu);
        if (!lhs.getType().equals("int")) {
            errorPrint("should be int * int");
        }
        n.f1.accept(this, argu);
        MType rhs = n.f2.accept(this, argu);
        if (!rhs.getType().equals("int")) {
            errorPrint("should be int * int");
        }
        return new MType("int");
    }
    public MType visit(ArrayLookup n, MType argu) {
        MType lhs = n.f0.accept(this, argu);
        if (!lhs.getType().equals("array")) {
            errorPrint("expect array, " + "but get " + ((MIdentifier)lhs).getName() + " : " + lhs.getType());
        }
        n.f1.accept(this, argu);
        MType index = n.f2.accept(this, argu);
        if (!index.getType().equals("int")) {
            errorPrint("array[should be int], but get " + index.getType());
        }
        n.f3.accept(this, argu);
        String trimType = "int";
        if (debug) {
            System.out.println("debug: arraylookup " + trimType);
        }
        return new MType(trimType);
    }
    public MType visit(ArrayLength n, MType argu) {
        MType arrType = n.f0.accept(this, argu);
        if (!arrType.getType().equals("array")) {
            errorPrint("array.length");
        }
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return new MType("int");
    }
    public MType visit(MessageSend n, MType argu) {
        // 此处为函数调用
        if (debug) {
            System.out.println("MessageSend");
        }
        MType classType = n.f0.accept(this, argu);
        if (debug) {
            System.out.println(((MIdentifier)classType).getName());
        }
        MClass hereClass = allClassList.getClass(((MIdentifier)classType).getType());
        if (!hereClass) {
            errorPrint("no class");
        }
        if (debug) {
            System.out.println(((MIdentifier)classType).getName());
            System.out.println(((MIdentifier)classType).getType());
        }
        n.f1.accept(this, argu);
        MType methodType = n.f2.accept(this, (MType)hereClass);
        if (!hereClass.hasMethod(((MIdentifier)methodType).getName())) {
            errorPrint("no method: " + ((MIdentifier)methodType).getName());
        }
        MMethod hereMethod = hereClass.getMethod(((MIdentifier)methodType).getName());
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        if (debug) {
            System.out.println("method params-------------");
            for (int i = 0; i < methodParam.size(); i++){
                System.out.println(methodParam.get(i) + " ");
            }
            System.out.println("method params end---------");
        }
        Vector<MType> factParam = hereMethod.getOrderPara();
        if (methodParam.size() != factParam.size()) {
            errorPrint("wrong param nums");
        }
        methodParam.clear();
        // 检查调用参数
        for(int i = 0; i < methodParam.size(); i++){
            if (!methodParam.get(i).equals(factParam.get(i).getType())) {
                errorPrint("wrong param type: " + i + "; expect " + factParam.get(i).getType() + " but get " + methodParam.get(i));
            }
        }
        n.f5.accept(this, argu);
        if (debug) {
            System.out.println(hereMethod.getType());
        }
        // 函数调用可能返回一个类的实例，没有名字
        return new MIdentifier(0, 0, hereMethod.getType(), null);
    }
    public MType visit(PrimaryExpression n, MType argu) {
        if (debug) {
            System.out.println("PrimaryExpression");
        }
        MType primary = n.f0.accept(this, argu);
        if (primary instanceof MIdentifier) {
            if (debug) {
                System.out.println(((MIdentifier)primary).getName());
                System.out.println(primary.getType());
            }
            if (argu instanceof MMethod) {
                if (debug) {
                    System.out.println("Primary MMethod");
                }
                // 此时为新的类实例
                if (((MIdentifier)primary).getName() == null) {
                    return primary;
                }
                // 正常函数变量
                if (((MMethod)argu).hasVar(((MIdentifier)primary).getName())) {

                    return ((MMethod)argu).getVarType(((MIdentifier)primary).getName());
                } else {
                    errorPrint("id:no " + ((MIdentifier)primary).getName());
                    return null;
                }
            } else if (argu instanceof MClassList) {
                if (debug) {
                    System.out.println("Primary MClassList");
                }
                return primary;
            } else {
                if (debug) {
                    System.out.println("Primary Unknown");
                }
                return new MType("Primary unknown type");
            }
        } else {
            return primary;
        }
    }
    public MType visit(IntegerLiteral n, MType argu) {
        n.f0.accept(this, argu);
        return new MType("int");
    }
    public MType visit(TrueLiteral n, MType argu) {
        n.f0.accept(this, argu);
        return new MType("boolean");
    }
    public MType visit(FalseLiteral n, MType argu) {
        n.f0.accept(this, argu);
        return new MType("boolean");
    }
    public MType visit(Identifier n, MType argu) {
        String token = n.f0.toString();
        if (debug) {
            System.out.println("debug: token " + token);
        }
        String type = "token";
        if (argu instanceof MMethod) {
            MMethod hereMethod = (MMethod)argu;
            if (hereMethod.hasVar(token)) {
                type = hereMethod.getVarType(token).getType();
            } else {
                errorPrint("no "+token + " in method: " + hereMethod.getName());
            }
            if (debug) {
                System.out.println(type);
            }
        } else if (argu instanceof MClassList) {
            // 此时token为一个类的名字，遍历处在最外
            if (allClassList.getClass(token) == null) {
                errorPrint("no this class: "+token);
            }
            type = token; // 类名，类型
            token = null; // 没有
        }
        return new MIdentifier(n.f0.beginLine, n.f0.beginColumn, type, token);
    }
    public MType visit(ThisExpression n, MType argu) {
        if (debug) {
            System.out.println("ThisExpression");
            if (argu instanceof MMethod) {
                System.out.println("ThisExpression MMethod");
            }
        }
        n.f0.accept(this, argu);
        String ownerType = ((MMethod)argu).getOwner().getType();
        // 符号表此处Name存的为类名
        String ownerName = ((MMethod)argu).getOwner().getName();
        return new MIdentifier(n.f0.beginLine, n.f0.beginColumn, ownerName, null);
    }
    public MType visit(ArrayAllocationExpression n, MType argu) {
        if (debug) {
            System.out.println("ArrayAllocationExpression");
        }
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        MType allocNum = n.f3.accept(this, argu);
        if (!allocNum.getType().equals("int")) {
            errorPrint("new int[should be int], " + "but get " + allocNum.getType());
        }
        n.f4.accept(this, argu);
        return new MType("array");
    }
    public MType visit(AllocationExpression n, MType argu) {
        // new 类()
        if (debug) {
            System.out.println("AllocationExpression");
        }
        n.f0.accept(this, argu);
        MType _ret = n.f1.accept(this, (MType)allClassList);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        if (debug) {
            System.out.println(((MIdentifier)_ret).getType());
        }
        return new MIdentifier(0, 0, ((MIdentifier)_ret).getType(), null);
    }
    public MType visit(NotExpression n, MType argu) {
        if (debug) {
            System.out.println("NotExpression");
        }
        n.f0.accept(this, argu);
        MType rhs = n.f1.accept(this, argu);
        if (!rhs.getType().equals("boolean")) {
            errorPrint("! should be boolean, but get "+rhs.getType());
        }
        return new MType("boolean");
    }
    public MType visit(BracketExpression n, MType argu) {
        if (debug) {
            System.out.println("BracketExpression");
        }
        n.f0.accept(this, argu);
        MType _ret = n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
    }

    // 函数参数列表，确保变量类型，似乎不用
    public MType visit(FormalParameterList n, MType argu) {
        if (debug) {
            System.out.println("FormalParameterList");
        }
        MType _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }
    public MType visit(FormalParameter n, MType argu) {
        if (debug) {
            System.out.println("FormalParameter");
        }
        MType _ret = null;
        MType ltype = n.f0.accept(this, (MType)allClassList);
        MType rvar = n.f1.accept(this, null);
        if (debug) {
            System.out.println(ltype.getType());
            System.out.println(((MIdentifier)rvar).getName());
            System.out.println(((MIdentifier)rvar).getType());
        }
        MType rtype = null;
        if (argu instanceof MMethod) {
            rtype = ((MMethod)argu).getVarType(((MIdentifier)rvar).getName());
        } else if (argu instanceof MClass) {
            rtype = ((MClass)argu).getVarType(((MIdentifier)rvar).getName());
        }
        rtype.setType(ltype.getType());
        return _ret;
    }
    public MType visit(FormalParameterRest n, MType argu) {
        if (debug) {
            System.out.println("FormalParameterRest");
        }
        n.f1.accept(this, argu);
        MType _ret = null;
        return _ret;
    }

    public MType visit(PrintStatement n, MType argu) {
        if (debug) {
            System.out.println("PrintStatement");
        }
        MType _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        _ret = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        if (!_ret.getType().equals("int")) {
            errorPrint("print(should be int), but get " + _ret.getType());
        }
        return _ret;
    }
    public MType visit(IfStatement n, MType argu) {
        if (debug) {
            System.out.println("IfStatement");
        }
        MType _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        _ret = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        if (!_ret.getType().equals("boolean")) {
            errorPrint("if(should be boolean)");
        }
        return _ret;
    }
    public MType visit(WhileStatement n, MType argu) {
        if (debug) {
            System.out.println("WhileStatement");
        }
        MType _ret = null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        _ret = n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        if (!_ret.getType().equals("boolean")) {
            errorPrint("if(should be boolean)");
        }
        return _ret;
    }

    public MType visit(AssignmentStatement n, MType argu) {
        if (debug) {
            System.out.println("AssignmentStatement");
        }
        MType lhs = n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        MType rhs = n.f2.accept(this, argu);
        // 检查赋值的类型
        if (allClassList.getClass(lhs.getType())!=null && allClassList.getClass(rhs.getType())!=null) {
            if (!lhs.getType().equals(rhs.getType())) {
                // 是否为父子类
                MClass leftClass = allClassList.getClass(lhs.getType());
                MClass rightClass = allClassList.getClass(rhs.getType());
                if (debug) {
                    System.out.println(leftClass.getType());
                    if (rightClass.getParentClass()!=null) {
                        System.out.println(rightClass.getParentClass().getType());
                    }
                }
                if (rightClass.getParentClass() == null || !rightClass.getParentClass().getType().equals(leftClass.getType())) {
                    errorPrint(((MIdentifier)lhs).getName() + " : " + lhs.getType() + " = " + rhs.getType());
                }
            }
        }
        else if (!lhs.getType().equals(rhs.getType())) {
            errorPrint(((MIdentifier)lhs).getName() + " : " + lhs.getType() + " = " + rhs.getType());
        }
        n.f3.accept(this, argu);
        return new MType("assign_expr");
    }

    // 检查返回值的类型是否与函数声明一致
    public void checkRetType(MType retExpType, MType ret, String error) {
        if (!retExpType.getType().equals(ret.getType())) {
            errorPrint(error + "; expect " + ret.getType() + " but get " + retExpType.getType());
        }
    }

    // 检查返回的类型是否存在
    public void checkTypeDeclared(MType type, MType argu) {
        String typeName;
        if (type instanceof MIdentifier) {
            typeName = ((MIdentifier)type).getType();
            if (allClassList.containClass(typeName)) {
                return;
            }
        } else {
            typeName = type.getType();
            if (typeName.equals("int") || typeName.equals("boolean") || typeName.equals("array")) {
                return;
            }
        }
        errorPrint("Undefined type: " + typeName);
    }
    // 检查是否有函数重载
    public void checkOverload(MClass hereClass, MClass extsClass) {
        HashMap<String, MMethod> hereMethods = hereClass.getMethodMap();
        HashMap<String, MMethod> extsMethods = extsClass.getMethodMap();
        for (String hkey:hereMethods.keySet()) {
            for (String ekey:extsMethods.keySet()) {
                if (hkey.equals(ekey)) {
                    MMethod hereMethod = hereClass.getMethod(hkey);
                    MMethod extsMethod = extsClass.getMethod(ekey);
                    Vector<MType> hereParams = hereMethod.getOrderPara();
                    Vector<MType> extsParams = extsMethod.getOrderPara();
                    if (hereParams.size() != extsParams.size()) {
                        errorPrint("overload " + hkey);
                    }
                    for(int i = 0; i < hereParams.size(); i++){
                        if (!hereParams.get(i).getType().equals(extsParams.get(i).getType())) {
                            errorPrint("overload " + hkey);
                        }
                    }
                    break;
                }
            }
        }
    }
    public void errorPrint(String error) {
        System.out.println("error => " + error);
        System.exit(10086);
    }
}
