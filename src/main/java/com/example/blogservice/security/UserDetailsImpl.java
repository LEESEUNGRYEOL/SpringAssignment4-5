package com.example.blogservice.security;

import com.example.blogservice.entity.User;
import com.example.blogservice.entity.UserRoleEnum;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


@Getter
public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final String username;

    // 1. 생성자
    public UserDetailsImpl(User user, String username) {
        this.user = user;
        this.username = username;
    }

    /* 인증이 완료된 사용자 추가 및 사용자의 권환 GrantedAuthority 로 추상화 및 변환*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 1. 유저의 권한을 가져온다.
        UserRoleEnum role = user.getRole();
        // 2. 권한값을 String 으로 변환
        String authority = role.getAuthority();
        // 3. 유저의 권한을 추상화 해서 사용.
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }
    public User getUser() {
        return user;
    }
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}