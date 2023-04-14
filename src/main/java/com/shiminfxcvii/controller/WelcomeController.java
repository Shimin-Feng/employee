package com.shiminfxcvii.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 接受登录和主页的请求
 *
 * @author ShiminFXCVII
 * @since 2022/5/10 15:40
 */
@Controller
public final class WelcomeController {

    /**
     * 在登录之前访问任何资源都将跳转到自定义登录界面
     *
     * @return "login" login 页面
     * @author ShiminFXCVII
     * @since 2022/4/29 16:46
     */
    @GetMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 接受请求跳转到 index 页面
     *
     * @return "index" index 页面
     * @author ShiminFXCVII
     * @since 2022/4/29 16:33
     */
    @GetMapping("index")
    public String index() {
        return "index";
    }

    // TODO: <input> 如何解决在使用中文输入时的错误？
    // TODO: 统计图
    // TODO: 数据库根据时间自动调整年龄
    // TODO: 实现使用拼音也能搜索
    // TODO: ExampleMatcher 匹配 SearchRecord 搜索（考虑）
    // TODO: 后续可以添加用户管理界面，管理请假界面
    // TODO: 迁移数据库之后 employee_management employee 编码为 utf8mb4，所以某些查询会出现问题
    // TODO: 如此频繁地查询数据库是否真的有必要？
    // TODO: 原生 js forEach 的用法
    // TODO: 根据本项目目前的情况，js 中修改员工信息弹窗事件委托暂时先放在 tbody
    // TODO: js 加分号，换成 var
    // TODO: @NotNull 换成自己判断
    // TODO: 再次尝试将 js 查找员工的两个方法写进一个方法里
    // TODO: 设置页面最小不可变 400px
    // TODO: js 将获取标签统一写在开头
    // TODO: 精简页面需要更新的数据
    // TODO: 将在 js 中的验证转到 entity 上字段上验证

    /*@RequestMapping("logout")
    public String logout() {
        return "logout";
    }*/

    /*@RequestMapping("login-failed")
    public String loginFailed() {
        return "login-failed";
    }*/

    // TODO: 为什么点退出会来的 timeout 页面？
    /*@RequestMapping("timeout")
    public String timeout() {
        return "timeout";
    }*/

}