package com.junt.appchat.service.impl;

import com.junt.appchat.model.Chat;
import com.junt.appchat.model.Room;
import com.junt.appchat.model.StatusMessage;
import com.junt.appchat.model.User;
import com.junt.appchat.repository.ChatRepesitory;
import com.junt.appchat.repository.RoomRepository;
import com.junt.appchat.repository.UserRepository;
import com.junt.appchat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepesitory chatRepesitory;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public List<Chat> getAllChatByRoom(Room room) {
        return List.of();
    }

    @Override
    public List<Chat> getAllChatByUser(UUID user) {
        return List.of();
    }

    @Override
    public Chat saveChat(Chat chat) {
        return null;
    }

    @Override
    public void deleteChat(Chat chat) {

    }

    @Override
    public Chat sendMessage(UUID roomId, UUID senderId, String message) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        User sender = userRepository.findById(senderId).orElseThrow();

        Chat chat = new Chat();
        chat.setRoom(room);
        chat.setSender(sender);
        chat.setMessage(message);
        chat.setSendTime(new Timestamp(System.currentTimeMillis()));
        chat.setStatus(StatusMessage.SENT);

        return chatRepesitory.save(chat);
    }
}
