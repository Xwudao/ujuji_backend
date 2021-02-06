package com.ujuji.navigation.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private String username;
    private String AccessToken;//token
    private String email;
}
