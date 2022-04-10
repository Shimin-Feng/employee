package com.example.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @Name HTTPStatusCodeErrorController
 * @Author $himin F
 * @Date 2022/4/9 12:56 周六
 * @Version 1.0
 * @description: 处理 HTTP status code
 */
@Controller
public class HTTPStatusCodeErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request, Principal user) {
        ModelAndView model = new ModelAndView();
        // Get HTTP status code
        if (request.getAttribute("javax.servlet.error.status_code") != null) {
            Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
            if (statusCode == 400) {
                if (user != null) {
                    model.addObject("msg", "Hi " + user.getName()
                            + ", 由于明显的客户端错误（例如，格式错误的请求语法，太大的大小，无效的请求消息或欺骗性路由请求），服务器不能或不会处理该请求。");
                } else {
                    model.addObject("msg", "由于明显的客户端错误（例如，格式错误的请求语法，太大的大小，无效的请求消息或欺骗性路由请求），服务器不能或不会处理该请求。");
                }
                model.setViewName("400");
            } else if (statusCode == 403) {
                if (user != null) {
                    model.addObject("msg", "Hi " + user.getName()
                            + ", you do not have permission to access this page.");
                } else {
                    model.addObject("msg", "You do not have permission to access this page.");
                }
                model.setViewName("403");
            } else if (statusCode == 404) {
                if (user != null) {
                    model.addObject("msg", "Hi " + user.getName()
                            + ", the requested resource could not be found but may be available in the future." +
                            " Subsequent requests by the client are permissible.");
                } else {
                    model.addObject("msg", "The requested resource could not be found but" +
                            " may be available in the future. Subsequent requests by the client are permissible.");
                }
                model.setViewName("404");
            } else if (statusCode == 440) {
                if (user != null) {
                    model.addObject("msg", "Hi " + user.getName()
                            + ", the client's session has expired and must log in again.");
                } else {
                    model.addObject("msg",
                            "The client's session has expired and must log in again.");
                }
                model.setViewName("440");
            } else {
                model.addObject("msg", statusCode);
                model.setViewName("unknownError");
            }
        }
        return model;
    }

}