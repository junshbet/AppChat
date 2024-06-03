package com.junt.appchat.repository;

import com.junt.appchat.model.Room;
import com.junt.appchat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    Optional<Room> findByUser1AndUser2OrUser1AndUser2(User user12, User user21, User user2, User user1);
    List<Room> findByUser1OrUser2(User user1,User user);
}
