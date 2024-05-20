package com.cafehub.cafehub.security.jwt;

import com.cafehub.cafehub.common.dto.Timestamped;
import com.cafehub.cafehub.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends Timestamped {

    @Id
    private String email;

    private String token;

    public RefreshToken(Member member, String token) {
        this.email = member.getEmail();
        this.token = token;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}
