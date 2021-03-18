package com.vincent.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateRolePermissionsForm {
    @NotNull
    @Min(1)
    private Integer id;
    @NotBlank
    private String permissions;
}