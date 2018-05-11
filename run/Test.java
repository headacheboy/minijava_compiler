//class Test {
//	public static void main(String [] args) {
//		A a;
//        a = new A();
//		System.out.println(a.foo());
//	}
//}
//class A {
//	int A;
//	int[] foo;
//	public int foo() {
//		int A;
//		int[] foo;
//        return A;
//	}
//}
//
//class B extends A {
//	boolean A;
//	boolean foo;
//}


class Test {
	public static void main(String [] args) {
		A a;
        a = new A();
		System.out.println(a.foo(2018));
	}
}
class A {
	public int foo(int n) {
		int i;
        int n;
        i = 0;
		while (i < 10) {
            n = 10;
			i = i + 1;
		}
        return i;
	}
}

