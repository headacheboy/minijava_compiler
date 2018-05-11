class Test{
    public static void main(String[] aa) {
        int ret_value;
        A a;
        B b;
        A c;
        ret_value = new Student().sayHello();
        a = new A();
        b = new B();
        c = b;
        ret_value = a.assignA(1);
        ret_value = b.assignB(2);
        ret_value = a.print();
        ret_value = b.print();
        ret_value = c.print();
        ret_value = a.printA();
        ret_value = b.printA();
        ret_value = c.printA();
    }
}

class Person {
    public int sayHello() {
        System.out.println(0);
        return 0;
    }
}

class Student extends Person {
    public int sayHello() {
        System.out.println(1);
        return 1;
    }
}

class A {
    int type;
    public int assignA(int init_type) {
        type = init_type;
        return type;
    }
    public int printA() {
        System.out.println(type);
        return type;
    }
    public int print() {
        System.out.println(type);
        return type;
    }
}
class B extends A {
    int type;
    public int assignB(int init_type) {
        type = init_type;
        return type;
    }
    public int printB() {
        System.out.println(type);
        return type;
    }
    public int print() {
        System.out.println(type);
        return type;
    }
}
