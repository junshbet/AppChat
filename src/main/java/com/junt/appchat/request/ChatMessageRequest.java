package com.junt.appchat.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;
@Getter
@Setter
@ToString
public class ChatMessageRequest {
    private UUID roomId;
    private UUID senderId;
    private String message;
}
