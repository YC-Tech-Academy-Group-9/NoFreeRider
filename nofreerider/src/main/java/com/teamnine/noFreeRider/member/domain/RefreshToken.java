package com.teamnine.noFreeRider.member.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "memberId", unique = true)
    private Member member;

    public RefreshToken(String refreshToken, Member member) {
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public RefreshToken() {

    }
}