package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private final Utilizator from;
    private final List<Utilizator> to;
    private final String message;
    private final LocalDateTime data;
    private final Long reply;


    public Message( Utilizator from, List<Utilizator> to, String message, LocalDateTime data, Long reply ) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.reply = reply;
    }

    public Utilizator getFrom() {
        return from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Long getReply() {
        return reply;
    }


    public String toUsersToString() {
        StringBuilder s = new StringBuilder();
        for (Utilizator x : this.to) {
            s.append(x.getId()).append(" ");
        }
        return s.toString();
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + toUsersToString() +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", reply=" + reply +
                '}';
    }
}


