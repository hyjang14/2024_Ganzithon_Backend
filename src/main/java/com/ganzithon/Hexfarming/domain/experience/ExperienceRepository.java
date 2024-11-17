package com.ganzithon.Hexfarming.domain.experience;

import com.ganzithon.Hexfarming.global.enumeration.Ability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    Optional<List<Experience>> findAllByUserId(int userId);
    Optional<Experience> findByUserIdAndAbility(int userId, Ability ability);
}
