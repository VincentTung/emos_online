package com.vincent.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@ApiModel
public class UpdateEmployeeStateForm {

    private int code;
    @Min(0) @Max(1)
    private int state;
}
