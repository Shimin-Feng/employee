package com.shiminfxcvii.employee.controller;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * @author $himin F
 * @version 1.0
 * @description
 * @class OperationLogControllerTest
 * @see
 * @since 2022/5/2 10:33 周一
 */
class OperationLogControllerTest {

    @Test
    public void saveOperationLog() {
        Logger logger = Logger.getGlobal();
        logger.info("1111");
        logger.config("222");
        logger.fine("333");
        logger.finer("333");
        logger.finest("333");
        logger.warning("333");
        System.out.println(logger.getName());
        System.out.println(logger.getParent());
        System.out.println(logger.getFilter());
        System.out.println(Arrays.toString(logger.getHandlers()));
        System.out.println(logger.getClass());
        System.out.println(logger.getLevel());
    }
}