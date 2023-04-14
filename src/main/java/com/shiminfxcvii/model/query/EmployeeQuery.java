package com.shiminfxcvii.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * EmployeeQuery
 *
 * @author ShiminFXCVII
 * @since 2022/10/3 16:53 周一
 */
@Getter
@Setter
public class EmployeeQuery {

    /**
     * 员工姓名
     */
    @Schema(example = "员工姓名", type = "string")
    private String employeeName;
    /**
     * 员工性别
     */
    @Schema(example = "员工性别", type = "string")
    private String employeeSex;
    /**
     * 员工年龄
     */
    @Schema(example = "员工年龄", type = "integer")
    private Integer employeeAge;
    /**
     * 员工身份证号码
     */
    @Schema(example = "员工身份证号码", type = "string")
    private String employeeIdCard;
    /**
     * 员工住址
     */
    @Schema(example = "员工住址", type = "string")
    private String employeeAddress;
    /**
     * 员工电话号码
     */
    @Schema(example = "员工电话号码", type = "string")
    private String employeePhoneNumber;
    /**
     * 创建者
     */
    @Schema(example = "创建者", type = "string")
    private String createdBy;
    /**
     * 创建时间
     */
    @Schema(example = "创建时间", type = "string")
    private String createdDate;
    /**
     * 最后更新时间
     */
    @Schema(example = "最后更新时间", type = "string")
    private String lastModifiedDate;

}