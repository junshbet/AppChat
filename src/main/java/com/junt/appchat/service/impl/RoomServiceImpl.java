package com.junt.appchat.service.impl;

import com.junt.appchat.model.Room;
import com.junt.appchat.model.User;
import com.junt.appchat.repository.RoomRepository;
import com.junt.appchat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    @Override
    public List<Room> getAllRoomByUser(User user) {
        return List.of();
    }

    @Override
    public Room getRoomById(UUID id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomChatByUser1AND2(User user1,User user2) {
        return roomRepository.findByUser1AndUser2OrUser1AndUser2(user1,user2,user2,user1).orElse(null);
    }
}
