package com.shiminfxcvii.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @class HTTPStatusCodeErrorController
 * @created 2022/4/9 12:56 周六
 * @description 处理 HTTP status code
 */
@Controller
public class HTTPStatusCodeErrorController implements ErrorController {

    /**
     * 接受 http 错误状态码设置并返回内容
     *
     * @param request HttpServletRequest 请求
     * @param user    Principal 包含登录用户信息
     * @return modelAndView 根据错误状态码设置并返回内容
     * @method handleError
     * @author shiminfxcvii
     * @created 2022/5/1 15:25
     * @see org.springframework.web.servlet.ModelAndView
     * @see javax.servlet.http.HttpServletRequest
     * @see java.security.Principal
     */
    @RequestMapping("/error")
    public ModelAndView handleError(@NotNull HttpServletRequest request, Principal user) {
        ModelAndView modelAndView = new ModelAndView();
        // Get HTTP status code
        if (null != request.getAttribute("javax.servlet.error.status_code")) {
            Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
            switch (statusCode) {
                case 400 -> {
                    if (null != user) {
                        modelAndView.addObject("msg", "Hi " + user.getName()
                                + ", 由于明显的客户端错误（例如，格式错误的请求语法，太大的大小，无效的请求消息或欺骗性路由请求），服务器不能或不会处理该请求。");
                    } else {
                        modelAndView.addObject("msg", "由于明显的客户端错误（例如，格式错误的请求语法，太大的大小，无效的请求消息或欺骗性路由请求），服务器不能或不会处理该请求。");
                    }
                    modelAndView.setViewName("400");
                }
                case 403 -> {
                    if (null != user) {
                        modelAndView.addObject("msg", "Hi " + user.getName()
                                + ", you do not have permission to access this page.");
                    } else {
                        modelAndView.addObject("msg", "You do not have permission to access this page.");
                    }
                    modelAndView.setViewName("403");
                }
                case 404 -> {
                    if (null != user) {
                        modelAndView.addObject("msg", "Hi " + user.getName()
                                + ", the requested resource could not be found but may be available in the future." +
                                " Subsequent requests by the client are permissible.");
                    } else {
                        modelAndView.addObject("msg", "The requested resource could not be found but" +
                                " may be available in the future. Subsequent requests by the client are permissible.");
                    }
                    modelAndView.setViewName("404");
                }
                case 440 -> {
                    if (null != user) {
                        modelAndView.addObject("msg", "Hi " + user.getName()
                                + ", the client's session has expired and must log in again.");
                    } else {
                        modelAndView.addObject("msg",
                                "The client's session has expired and must log in again.");
                    }
                    modelAndView.setViewName("440");
                }
                default -> {
                    modelAndView.addObject("msg", statusCode);
                    modelAndView.setViewName("unknownError");
                }
            }
        }
        return modelAndView;
    }

}