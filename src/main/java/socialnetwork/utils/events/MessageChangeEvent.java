package socialnetwork.utils.events;

import socialnetwork.controller.SendMessageType;
import socialnetwork.domain.MessageUserDto;

public class MessageChangeEvent implements Event {
    private MessageUserDto dto;
    private SendMessageType messType;

    public MessageChangeEvent( MessageUserDto dto, SendMessageType messType ) {
        this.dto = dto;
        this.messType = messType;
    }

    public MessageUserDto getDto() {
        return dto;
    }

    public SendMessageType getMessType() {
        return messType;
    }
}
