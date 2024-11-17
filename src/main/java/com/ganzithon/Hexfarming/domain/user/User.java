package com.ganzithon.Hexfarming.domain.user;

import com.ganzithon.Hexfarming.domain.experience.Experience;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder // 객체 생성을 쉽게 만들어주는 애너테이션
@AllArgsConstructor // 생성자 자동 생성
@NoArgsConstructor // 생성자 자동 생성
@Entity // Model 객체라는 것을 알리는 애너테이션 -> 지역변수들을 자동으로 DB에 매핑시켜줌
public class User {
    @Id // id값이라는 걸 알려줌
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동으로 1, 2, 3, ... 1씩 증가해서 넣어줌
    private int id;

    private String email;

    private String password;

    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // Experience의 'user' 필드를 통해 User에서 역참조 가능
    private List<Experience> experiences = new ArrayList<>();

    public void setPassword(String password) {
        this.password = password;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
