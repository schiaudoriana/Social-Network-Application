package socialnetwork.utils.events;

import socialnetwork.domain.UserFriendDto;

public class StatusEvent implements Event {
    private StatusEventType type;
    private UserFriendDto dto;

    public StatusEvent( StatusEventType type, UserFriendDto dto ) {
        this.type = type;
        this.dto = dto;
    }

    public StatusEventType getType() {
        return type;
    }

    public UserFriendDto getDto() {
        return dto;
    }

    public void setType( StatusEventType type ) {
        this.type = type;
    }

    public void setDto( UserFriendDto dto ) {
        this.dto = dto;
    }
}
