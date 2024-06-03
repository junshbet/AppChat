package com.junt.appchat.service;

import com.junt.appchat.model.Chat;
import com.junt.appchat.model.Room;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    List<Chat> getAllChatByRoom(Room room);
    List<Chat> getAllChatByUser(UUID user);
    Chat saveChat(Chat chat);
    void deleteChat(Chat chat);
    Chat sendMessage(UUID roomId, UUID senderId, String message);
}
