package com.vincent.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel
public class RegisterCodeForm {

    @NotBlank
    @Pattern(regexp = "^[0-9]{4}$",message = "注册码必须是4位数字")
    private String registerCode;
}
