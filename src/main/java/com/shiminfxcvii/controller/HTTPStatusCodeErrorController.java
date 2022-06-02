package com.shiminfxcvii.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.regex.Pattern;

/**
 * 处理 HTTP status code
 *
 * @author shiminfxcvii
 * @since 2022/4/9 12:56
 */
@Controller
public final class HTTPStatusCodeErrorController implements ErrorController {
    private static final ModelAndView MODEL_AND_VIEW = new ModelAndView();

    /**
     * 接受 http 错误状态码设置并返回内容
     *
     * @param response HttpServletResponse 响应的错误信息
     * @param user     Principal 包含登录用户信息
     * @return modelAndView 根据错误状态码设置并返回内容
     * @method handleError
     * @author shiminfxcvii
     * @see ModelAndView
     * @see HttpServletResponse
     * @see Principal
     * @since 2022/5/1 15:25
     */
    @GetMapping("error")
    public ModelAndView handleError(HttpServletResponse response, Principal user) {
        // Get HTTP status code
        int status = response.getStatus();
        if (Pattern.matches("^\\d+$", String.valueOf(status))) {
            MODEL_AND_VIEW.addObject("back", "Back to the index page.");
            MODEL_AND_VIEW.addObject("href", "/index");
            switch (status) {
                case 400 -> {
                    if (null != user)
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                "Hi " + user.getName() +
                                        """
                                                , the server cannot or will not process the request due to an apparent client error
                                                 (e.g., malformed request syntax, size too large, invalid request message framing, or deceptive request routing).<br>
                                                由于明显的客户端错误（例如，格式错误的请求语法、无效的请求消息或欺骗性路由请求），服务器不能或不会处理该请求。
                                                """
                        );
                    else
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                """
                                        The server cannot or will not process the request due to an apparent client error
                                         (e.g., malformed request syntax, size too large, invalid request message framing, or deceptive request routing).<br>
                                        由于明显的客户端错误（例如，格式错误的请求语法、无效的请求消息或欺骗性路由请求），服务器不能或不会处理该请求。
                                        """
                        );
                }
                case 403 -> {
                    if (null != user)
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                "Hi " + user.getName() +
                                        ", you do not have permission to access this page.<br>服务器已经理解请求，但是拒绝执行它。"
                        );
                    else
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                "You do not have permission to access this page.<br>服务器已经理解请求，但是拒绝执行它。"
                        );
                }
                case 404 -> {
                    if (null != user)
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                "Hi " + user.getName() +
                                        """
                                                , the requested resource could not be found but may be available in the future.
                                                 Subsequent requests by the client are permissible.<br>
                                                请求失败，请求所希望得到的资源未被在服务器上发现，但允许用户的后续请求。
                                                """
                        );
                    else
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                """
                                        The requested resource could not be found but may be available in the future.
                                         Subsequent requests by the client are permissible.<br>
                                        请求失败，请求所希望得到的资源未被在服务器上发现，但允许用户的后续请求。
                                        """
                        );
                }
                case 405 -> {
                    if (null != user)
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                "Hi " + user.getName() +
                                        """
                                                , a request method is not supported for the requested resource; for example,
                                                 a GET request on a form that requires data to be presented via POST, or a PUT request on a read-only resource.<br>
                                                请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个 Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
                                                例如，需要通过 POST 呈现数据的表单上的 GET 请求，或只读资源上的 PUT 请求。
                                                """
                        );
                    else
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                """
                                        A request method is not supported for the requested resource; for example,
                                         a GET request on a form that requires data to be presented via POST, or a PUT request on a read-only resource.<br>
                                        请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个 Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
                                        例如，需要通过 POST 呈现数据的表单上的 GET 请求，或只读资源上的 PUT 请求。
                                        """
                        );
                }
                case 415 -> {
                    if (null != user)
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                "Hi " + user.getName() +
                                        """
                                                , the requested resource could not be found but may be available in the future.
                                                 Subsequent requests by the client are permissible.<br>
                                                对于当前请求的方法和所请求的资源，请求中提交的互联网媒体类型并不是服务器中所支持的格式，因此请求被拒绝。
                                                例如，客户端将图像上传格式为 svg，但服务器要求图像使用上传格式为 jpg。
                                                """
                        );
                    else
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                """
                                        The requested resource could not be found but may be available in the future.
                                         Subsequent requests by the client are permissible.<br>
                                        对于当前请求的方法和所请求的资源，请求中提交的互联网媒体类型并不是服务器中所支持的格式，因此请求被拒绝。
                                        例如，客户端将图像上传格式为 svg，但服务器要求图像使用上传格式为 jpg。
                                        """
                        );
                }
                case 440 -> {
                    if (null != user)
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                "Hi " + user.getName() + ", the client's session has expired and must log in again.<br>会话已过期，请重新登录。"
                        );
                    else
                        MODEL_AND_VIEW.addObject(
                                "msg",
                                "The client's session has expired and must log in again.<br>会话已过期，请重新登录。"
                        );
                    // 将会覆盖之前的设置
                    MODEL_AND_VIEW.addObject("back", "Back to the login page.");
                    MODEL_AND_VIEW.addObject("href", "/login");
                }
                default -> MODEL_AND_VIEW.addObject("msg", HttpStatus.resolve(status));
            }

            // 使前台能通过 ${#request.getAttribute('org.springframework.web.servlet.View.responseStatus')} 获取到值，附带原因短语的错误提示
            MODEL_AND_VIEW.setStatus(HttpStatus.resolve(status));
            // 所有的错误状态码都将在 error-code 页面展示
            MODEL_AND_VIEW.setViewName("error-code");
        }

        return MODEL_AND_VIEW;
    }
}