package com.vincent.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@ApiModel
@Data
public class UpdateUnreadMessageForm {

    @Autowired
    private String id;
}
