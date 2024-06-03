package com.junt.appchat.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRoomRequest {
    private String sender;
    private String receiver;
}
