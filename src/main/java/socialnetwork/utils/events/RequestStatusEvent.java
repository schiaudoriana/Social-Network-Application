package socialnetwork.utils.events;

import socialnetwork.domain.RequestUserDto;

public class RequestStatusEvent {
    private StatusEventType type;
    private RequestUserDto dto;

    public RequestStatusEvent( StatusEventType type, RequestUserDto dto ) {
        this.type = type;
        this.dto = dto;
    }

    public StatusEventType getType() {
        return type;
    }

    public RequestUserDto getDto() {
        return dto;
    }

    public void setType( StatusEventType type ) {
        this.type = type;
    }

    public void setDto( RequestUserDto dto ) {
        this.dto = dto;
    }
}
