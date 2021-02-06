package com.ujuji.navigation.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("users")
public class UserEntity extends BaseEntity implements UserDetails {
    @NotBlank(message = "用户名不能为空")
    private String username;


    @NotBlank(message = "密码不能为空")
    @Length(min = 6, message = "密码长度最少6位")
    // @JsonIgnore
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}", message = "邮箱格式不准确")
    private String email;

    private Integer enable;

    private String AccessToken;//token

    @Null(message = "请勿传入非法参数")
    private String authority;

    @TableField(exist = false)
    private List<BoxEntity> boxes;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(authority));
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable == 1;
    }

}
