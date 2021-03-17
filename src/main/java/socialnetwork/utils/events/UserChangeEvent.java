package socialnetwork.utils.events;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.UserFriendDto;

public class UserChangeEvent implements Event {
    private ChangeEventType type;
    private UserFriendDto data;
    private UserFriendDto oldData;

    public UserChangeEvent( ChangeEventType type, UserFriendDto data ) {
        this.type = type;
        this.data = data;
    }

    public UserChangeEvent( ChangeEventType type, UserFriendDto data, UserFriendDto oldData ) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public UserFriendDto getData() {
        return data;
    }

    public UserFriendDto getOldData() {
        return oldData;
    }
}
