package com.ganzithon.Hexfarming.domain.experience;

import com.ganzithon.Hexfarming.domain.user.User;
import com.ganzithon.Hexfarming.global.enumeration.Ability;
import com.ganzithon.Hexfarming.global.enumeration.Tier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY) // 처음부터 User 객체를 가져오는 게 아니라, User이 참조될 때 User 객체를 가져옴
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING) // index 값이 아니라 enum 값의 이름(문자열)으로 DB에 저장
    private Ability ability;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    private int experience;

    public void increase(int amount) {
        this.experience += amount;
        this.tier = Tier.caculateTier(this.experience);
    }
}
