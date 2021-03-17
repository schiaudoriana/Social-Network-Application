package socialnetwork.domain;

import java.time.LocalDateTime;

public class RequestUserDto {
    private final String firstName;
    private final String lastName;
    private final String status;
    private final LocalDateTime date;

    public RequestUserDto( String firstName, String lastName, String status, LocalDateTime date ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date.minusSeconds(date.getSecond()).minusNanos(date.getNano());
    }
}
