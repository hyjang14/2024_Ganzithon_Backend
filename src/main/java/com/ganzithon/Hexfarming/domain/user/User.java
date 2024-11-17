package com.ganzithon.Hexfarming.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder // 객체 생성을 쉽게 만들어주는 애너테이션
@AllArgsConstructor // 생성자 자동 생성
@NoArgsConstructor // 생성자 자동 생성
@Entity // Model 객체라는 것을 알리는 애너테이션 -> 지역변수들을 자동으로 DB에 매핑시켜줌
public class User {
    @Id // id값이라는 걸 알려줌
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 1, 2, 3, ... 1씩 증가해서 넣어줌
    private int id;

    private String username;

    private String password;

    private String nickname;

    private String tier;
}
