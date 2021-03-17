package socialnetwork.utils.events;

import socialnetwork.domain.Request;
import socialnetwork.domain.RequestUserDto;

public class RequestChangeEvent implements Event {
    private ChangeEventType type;
    private RequestUserDto data;
    private RequestUserDto oldData;

    public RequestChangeEvent( ChangeEventType type, RequestUserDto data ) {
        this.type = type;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public RequestUserDto getData() {
        return data;
    }

    public RequestUserDto getOldData() {
        return oldData;
    }
}
