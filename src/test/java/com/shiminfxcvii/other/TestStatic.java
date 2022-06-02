package com.shiminfxcvii.other;

/**
 * @author $himin F
 * @version 1.0
 * @description
 * @class TestStatic
 * @see
 * @since 2022/5/2 12:19 周一
 */
public class TestStatic {


    public static String ENCODE = "UTF-8";

    public static String STATUS;
}

class Father {
    public static final int me = 334;
    public static int m = 33;

    static {
        m = 55;
        System.out.println(m);
        System.out.println(3);
    }
}

class Child {
    public static Child child = new Child();

    static {
        System.out.println("子类被初始化");
    }
}

class StaticTest {
    public static void main(String[] args) {
        Child child = new Child();
    }
}

class Other {
    public static final Other o1 = new Other();
    public static final Other o2 = new Other();

    static {
        System.out.println("静态块");
    }

    {
        System.out.println("构造块");
    }

    public static void main(String[] args) {
        Other other = new Other();
    }
}