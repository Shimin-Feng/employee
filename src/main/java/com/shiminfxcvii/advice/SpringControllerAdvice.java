package com.shiminfxcvii.advice;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author shiminfxcvii
 * @version 1.0
 * @description 给 Controller 控制器添加统一的操作或处理。
 * @class SpringControllerAdvice
 * @created 2022/5/12 12:54 周四
 */
@ControllerAdvice
public final class SpringControllerAdvice {

    private static final ModelAndView MODEL_AND_VIEW = new ModelAndView();

    /**
     * 全局异常捕捉处理
     *
     * @param e Exception
     * @return MODEL_AND_VIEW
     * @method errorHandler
     * @author shiminfxcvii
     * @created 2022/5/12 15:46
     * @see java.io.IOException
     * @see java.lang.IllegalAccessException
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ModelAndView exceptionHandler(Exception e) {
        if (e instanceof IllegalAccessException)
            MODEL_AND_VIEW.addObject("msg", "非法访问反射对象属性: " + e.getMessage());
        else if (e instanceof NullPointerException)
            MODEL_AND_VIEW.addObject("msg", "目标对象或值为空: " + e.getMessage());
        MODEL_AND_VIEW.setStatus(INTERNAL_SERVER_ERROR);
        MODEL_AND_VIEW.setViewName("error-code");
        return MODEL_AND_VIEW;
    }

    /**
     * 应用到所有被 @RequestMapping 注解的方法，在其执行之前初始化数据绑定器
     * 该注解的主要作用是绑定一些自定义的参数。一般情况下我们使用的参数通过 @RequestParam，@RequestBody 或者 @ModelAttribute 等注解就可以进行绑定了，
     * 但对于一些特殊类型参数，比如 Date，它们的绑定 Spring 是没有提供直接的支持的，我们只能为其声明一个转换器，
     * 将 request 中字符串类型的参数通过转换器转换为 Date 类型的参数，从而供给 @RequestMapping 标注的方法使用。
     *
     * @param binder WebDataBinder
     * @method initBinder
     * @author shiminfxcvii
     * @created 2022/5/12 15:52
     * @see org.springframework.web.bind.WebDataBinder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        /*
         * 创建一个字符串微调编辑器
         * 参数 {boolean emptyAsNull}: 是否把空字符串 ("") 视为 null
         */
        StringTrimmerEditor trimmerEditor = new StringTrimmerEditor(true);

        /*
         * 注册自定义编辑器
         * 接受两个参数 {Class<?> requiredType, PropertyEditor propertyEditor}
         * requiredType: 所需处理的类型
         * propertyEditor: 属性编辑器，StringTrimmerEditor 就是 propertyEditor 的一个子类
         */
        binder.registerCustomEditor(String.class, trimmerEditor);

        binder.registerCustomEditor(
                LocalDateTime.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), false)
        );
    }

    /**
     * 把值绑定到 Model 中，使全局 @RequestMapping 都可以获取到该值
     * \@ModelAttribute 标注的方法的执行是在所有的拦截器的 preHandle() 方法执行之后才会执行。
     *
     * @param model Model
     * @method addAttributes
     * @author shiminfxcvii
     * @created 2022/5/12 15:49
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("test", "Hello world.");
    }
}
