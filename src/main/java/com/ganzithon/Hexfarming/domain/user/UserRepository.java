package com.ganzithon.Hexfarming.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// User 객체를 저장하는 DB를 쉽게 다룰 수 있는 JPA Repository
public interface UserRepository extends JpaRepository<User, Integer> { // <User, id타입>
//    User findById(int id);
    User findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
}
