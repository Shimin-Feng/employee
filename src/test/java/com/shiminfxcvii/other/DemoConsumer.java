package com.shiminfxcvii.other;

import lombok.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import javax.persistence.Persistence;
import java.util.function.Consumer;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description
 * @class DemoConsumer
 * @see
 * @since 2022/5/27 21:50 周五
 */
public class DemoConsumer {
    /*
    定义一个方法包含两个参数
    参数1传递一个字符串
    参数2传递Consumer接口，泛型指定为String
    可以使用参数2传递的Consumer接口“消费”参数1传递的字符串
    */
    public static void consume(int i, String name, Consumer<String> con) {
        con.accept(name);
        System.out.println(i);
        System.out.println(name);
        System.out.println(con);
    }

    public static void main(String[] args) {
        //调用consume方法
        //因为Consumer接口是一个函数式接口。所以可以使用Lambda表达式
        consume(1, " jer ry o ", s -> {
            //自定义接口消费方式
            //对字符串进行反转输出
            StringBuffer reName = new StringBuffer(s).reverse();
            System.out.println(reName);
//            Assert.notNull(null, "Probe must not be null");
//            assert true : "Probe must not be null";
            System.err.println(reName);
        });

        consume(2, " jer ry o ", new Consumer<String>() {
            @Override
            public void accept(String s) {
                //自定义接口消费方式
                //对字符串进行反转输出
                StringBuffer reName = new StringBuffer(s).reverse();
                System.err.println(reName);
                System.out.println(reName);
            }
        });

        consume(3, " jer ry o ", s -> {
        });

        System.out.println(Persistence.getPersistenceUtil());
        loadUserByUsername("vv");
        loadUserByUsername(null);

    }

    public static void loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Assert.notNull(username, "The username must not be null.");
        System.out.println(username);
    }
}
