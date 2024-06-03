package com.junt.appchat.repository;

import com.junt.appchat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatRepesitory extends JpaRepository<Chat, UUID> {
}
