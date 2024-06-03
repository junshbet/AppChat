package com.junt.appchat.controller;

import com.junt.appchat.model.Chat;
import com.junt.appchat.model.ChatMessage;
import com.junt.appchat.model.Room;
import com.junt.appchat.model.User;
import com.junt.appchat.request.GetRoomRequest;
import com.junt.appchat.service.ChatService;
import com.junt.appchat.service.RoomService;
import com.junt.appchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    private final UserService userService;
    private final ChatService chatService;
    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;


    /**
     * Registers a user for chat.
     *
     * @param chatMessage    The chat message containing the sender's information.
     * @param headerAccessor The SimpMessageHeaderAccessor object used to access session attributes.
     * @return The registered chat message.
     */
    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    /**
     * Sends a chat message to all connected users in a specific channel.
     *
     * @param chatMessage The chat message to be sent.
     * @return The sent chat message.
     */
    @MessageMapping("/chat.send/{channel}")
    @SendTo("/topic/{channel}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String channel) {
        Chat chat = new Chat();
        chat.setMessage(chatMessage.getContent());
        User user = userService.getUserByUsername(chatMessage.getSender());
        chat.setSender(user);
        Room room = roomService.getRoomById(UUID.fromString(channel));
        chat.setRoom(room);
        chat.setSendTime(Timestamp.from(Instant.now()));
        chatService.saveChat(chat);
        return chatMessage;
    }


    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            user = userService.getUserByUsername(userDetails.getUsername());
        }
        model.addAttribute("user", user);
        List<User> users = userService.getUsers();
        User finalUser = user;
        users.removeIf(item -> item.getId().equals(finalUser.getId()));
        model.addAttribute("users", users);
        return "index";
    }

    @PostMapping("/getRoom")
    public ResponseEntity<?> getRoom(@ModelAttribute GetRoomRequest request) {
        User user1 = userService.getUserById(UUID.fromString(request.getSender()));
        User user2 = userService.getUserById(UUID.fromString(request.getReceiver()));

        Room room = roomService.getRoomChatByUser1AND2(user1, user2);
        if (room == null) {
            Room newRoom = new Room();
            newRoom.setUser1(user1);
            newRoom.setUser2(user2);
            System.out.println(newRoom.getUser1().getUsername());
            System.out.println(newRoom.getUser2().getUsername());
            room = roomService.createRoom(newRoom);
        }
        return ResponseEntity.ok(room.getId().toString());
    }

}
