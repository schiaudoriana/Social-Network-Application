package socialnetwork.domain;

import java.time.LocalDateTime;

public class UserFriendDto {
    private final String firstName;
    private final String lastName;
    private final LocalDateTime date;

    public UserFriendDto( String firstName, String lastName, LocalDateTime date ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getDate() {
        return date.minusSeconds(date.getSecond()).minusNanos(date.getNano());
    }
}
