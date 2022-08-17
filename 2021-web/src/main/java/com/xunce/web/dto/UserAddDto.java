package com.xunce.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserAddDto {
    @NotNull
    private Integer id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
