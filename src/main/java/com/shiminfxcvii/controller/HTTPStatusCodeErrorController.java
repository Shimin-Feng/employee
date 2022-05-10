package com.shiminfxcvii.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static com.shiminfxcvii.util.Constants.MODEL_AND_VIEW;

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
     * @return MODEL_AND_VIEW 根据错误状态码设置并返回内容
     * @method handleError
     * @author shiminfxcvii
     * @created 2022/5/1 15:25
     * @see ModelAndView
     * @see HttpServletRequest
     * @see Principal
     * TODO: 405, 415
     */
    @GetMapping("error")
    public ModelAndView handleError(@NotNull HttpServletRequest request, Principal user) {
        // Get HTTP status code
        if (null != request.getAttribute("javax.servlet.error.status_code")) {
            Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
            switch (statusCode) {
                case 400 -> {
                    if (null != user) {
                        MODEL_AND_VIEW.addObject("msg", "Hi " + user.getName()
                                + ", 由于明显的客户端错误（例如，格式错误的请求语法，太大的大小，无效的请求消息或欺骗性路由请求），服务器不能或不会处理该请求。");
                    } else {
                        MODEL_AND_VIEW.addObject("msg", "由于明显的客户端错误（例如，格式错误的请求语法，太大的大小，无效的请求消息或欺骗性路由请求），服务器不能或不会处理该请求。");
                    }
                    MODEL_AND_VIEW.setViewName("400");
                }
                case 403 -> {
                    if (null != user) {
                        MODEL_AND_VIEW.addObject("msg", "Hi " + user.getName()
                                + ", you do not have permission to access this page.");
                    } else {
                        MODEL_AND_VIEW.addObject("msg", "You do not have permission to access this page.");
                    }
                    MODEL_AND_VIEW.setViewName("403");
                }
                case 404 -> {
                    if (null != user) {
                        MODEL_AND_VIEW.addObject("msg", "Hi " + user.getName()
                                + ", the requested resource could not be found but may be available in the future." +
                                " Subsequent requests by the client are permissible.");
                    } else {
                        MODEL_AND_VIEW.addObject("msg", "The requested resource could not be found but" +
                                " may be available in the future. Subsequent requests by the client are permissible.");
                    }
                    MODEL_AND_VIEW.setViewName("404");
                }
                case 440 -> {
                    if (null != user) {
                        MODEL_AND_VIEW.addObject("msg", "Hi " + user.getName()
                                + ", the client's session has expired and must log in again.");
                    } else {
                        MODEL_AND_VIEW.addObject("msg",
                                "The client's session has expired and must log in again.");
                    }
                    MODEL_AND_VIEW.setViewName("440");
                }
                default -> {
                    MODEL_AND_VIEW.addObject("msg", statusCode);
                    MODEL_AND_VIEW.setViewName("unknown-error");
                }
            }
        }
        return MODEL_AND_VIEW;
    }

}