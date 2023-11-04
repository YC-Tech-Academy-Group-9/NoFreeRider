package com.teamnine.noFreeRider.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Member implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "member_no", updatable = false)
    private UUID memberNo;

    @Column(name = "member_id", nullable = false, updatable = false, unique = true)
    private String memberId;

    @Column(name = "member_email", nullable = false, updatable = true, unique = true)
    private String memberEmail;

    @Column(name = "member_password", nullable = false)
    private String memberPassword;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "member_temperature")
    private short memberTemperature;

    public Member(String memberId, String member_email, String memberPassword) {
        this.memberId = memberId;
        this.memberEmail = member_email;
        this.memberPassword = memberPassword;
        this.memberTemperature = 36;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public String getPassword() {
        return memberPassword;
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
