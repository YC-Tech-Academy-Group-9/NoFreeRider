package com.teamnine.noFreeRider.member.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @Column(name = "member_id", updatable = false)
    private UUID id;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_email", nullable = false, updatable = true, unique = true)
    private String memberEmail;

    @Column(name = "member_studentId")
    private int memberStudentId;

    @Column(name = "member_major")
    private String memberMajor;



    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "member_temperature")
    private short memberTemperature;

    public Member(String memberName, String member_email, int member_studentId, String member_major) {
        this.memberName = memberName;
        this.memberEmail = member_email;
        this.memberStudentId = member_studentId;
        this.memberMajor = member_major;
        this.memberTemperature = 36;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberName;
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

    public boolean isFullySet() {
        return this.getMemberName() != null && this.getMemberEmail() != null && this.getMemberMajor() != null && this.getMemberStudentId() != 0;
    }

    public void updateMember(String memberName, String email, int memberStudentId, String major) {
        this.memberName = memberName;
        this.memberEmail = email;
        this.memberStudentId = memberStudentId;
        this.memberMajor = major;
    }
}
