package com.shiminfxcvii.model.cmd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * EmployeeCmd
 *
 * @author shiminfxcvii
 * @since 2022/10/3 15:57 周一
 */
@Getter
@Setter
public class EmployeeCmd {

    /**
     * 员工 id
     */
//    @Schema(example = "员工 id", type = "string")
    private String employeeId;
    /**
     * 员工姓名
     */
    @NotBlank(message = "员工姓名不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5\\w\\s•]{1,25}$", message = "姓名只支持由 1 - 25 个汉字、英文、数字、空格和•组成")
//    @Schema(example = "员工姓名", type = "string")
    private String employeeName;
    /**
     * 员工身份证号码
     */
    @Length(min = 15, max = 18, message = "请填写 15 或者 18 位身份证号码")
    @NotBlank(message = "员工身份证号码不能为空")
    @Pattern(regexp = "^\\d{15}|\\d{18}|(\\d{17}X|x)$", message = "身份证号码有误，请检查后重试")
//    @Schema(example = "员工身份证号码", type = "string")
    private String employeeIdCard;
    /**
     * 员工住址
     */
    @NotBlank(message = "员工住址不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5\\w\\s•,]{2,45}$", message = "住址只支持由最多 45 个汉字、英文、空格、英文逗号和•的组合")
//    @Schema(example = "员工住址", type = "string")
    private String employeeAddress;
    /**
     * 员工电话号码
     */
    @NotBlank(message = "员工电话号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话号码格式有误，请检查后重试")
//    @Schema(example = "员工电话号码", type = "string")
    private String employeePhoneNumber;

}
