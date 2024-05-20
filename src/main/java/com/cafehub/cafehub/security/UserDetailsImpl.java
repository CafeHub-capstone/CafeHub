package com.cafehub.cafehub.security;

import com.cafehub.cafehub.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * UserDtails : SpringSecurity에서 사용자 세부 정보를 나타내는데 사용
 */
@Getter
public class UserDetailsImpl implements UserDetails {

    private final Member member;

    public UserDetailsImpl(Member member) {
        this.member = member;
    }

    /**
     * 현재는 권한을 따로 부여하지 않았기에 빈 리스트로 반환
     * 하지만 추후에 관리자 전용 서버를 따로 만들 것이기에
     * 그때는 ROLE_USER, ROLE_MANAGER 권한을 정의할 예정
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return member.getEmail();
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
        return true;
    }
}
