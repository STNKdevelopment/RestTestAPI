package ru.stnk.RestTestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stnk.RestTestAPI.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail (String email);

    /*@Query(value = "SELECT * FROM USERS WHERE EMAIL = ?1", nativeQuery = true)
    User findByUserEmail (String email);*/
}
