package com.vincent.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel
public class RegisterForm {

    @NotBlank
    @Pattern(regexp = "^[0-9]{4}$",message = "注册码必须是4位数字")
    private String registerCode;

//    微信临时授权码
    @NotBlank(message = "授权码不能为空")
    private String code;

    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @NotBlank(message = "头像不能为空")
    private String photo;

}
