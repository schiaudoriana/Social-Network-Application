package socialnetwork.domain;

import java.time.LocalDateTime;

public class MessageUserDto {
    private final Long id;
    private final String from;
    private final String message;
    private final LocalDateTime date;
    private final Long replyId;

    public MessageUserDto( Long id, String from, String message, LocalDateTime date, Long replyId ) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.date = date;
        this.replyId = replyId;
    }

    public Long getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date.minusSeconds(date.getSecond()).minusNanos(date.getNano());
    }

    public Long getReplyId() {
        return replyId;
    }
}
