package com.junt.appchat.service;

import com.junt.appchat.model.Room;
import com.junt.appchat.model.User;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    List<Room> getAllRoomByUser(User user);
    Room getRoomById(UUID id);
    Room createRoom(Room room);
    Room getRoomChatByUser1AND2(User user1,User user2);
}
