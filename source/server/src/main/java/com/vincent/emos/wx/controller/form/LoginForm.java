package com.vincent.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class LoginForm {

    @NotBlank(message = "微信临时授权码不能为空")
    private String code;
}
