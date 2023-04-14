package com.shiminfxcvii.other;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.function.Supplier;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * @author ShiminFXCVII
 * @since 2022/5/26 2:46 周四
 */

@FunctionalInterface
public interface MessageBuilder<T> {
    T buildMessage();
}

class Demo02LoggerLambda {
    private static void log(int level, MessageBuilder<String> builder) {
        if (level == 1) {
            System.out.println(builder.buildMessage());// 实际上利用内部类 延迟的原理,代码不相关 无需进入到启动代理执行
        }
    }

    public static void main(String[] args) {

        String msgA = "Hello";
        String msgB = "World";
        String msgC = "Java";
        log(1, () -> {
            System.out.println("lambda1 是否执行了");
            return msgA + msgB + msgC;
        });

        log(2, () -> {
            System.out.println("lambda2 是否执行了");
            return msgA + msgB + msgC;
        });


        out.println(Arrays.toString(args));
        Object o = new Object();
        Assert.notNull(o, () -> {
            out.println("lambda ------------------");
            return msgA + msgB + msgC;
        });

        Object o1 = checkNonNull(null, () -> {
            out.println(22);
            return msgA + msgB;
        });
        out.println(o1);
    }

    public static <T> T checkNonNull(T t, Supplier<String> msg) {
        if (t == null)
            err.println(msg.get());
        return t;
    }
}

class TestRunnable {
    private static void startThread(Runnable task) {
        new Thread(task).start();
    }

    public static void main(String[] args) {
        startThread(() -> out.println("线程任务执行！"));
    }
}