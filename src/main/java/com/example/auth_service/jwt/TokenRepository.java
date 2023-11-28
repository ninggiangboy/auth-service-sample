package com.example.auth_service.jwt;

import com.example.auth_service.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String token);

    @Query("SELECT t FROM Token t WHERE t.revoked = false AND t.user = :user")
    List<Token> findAllValidTokenByUser(User user);
}
