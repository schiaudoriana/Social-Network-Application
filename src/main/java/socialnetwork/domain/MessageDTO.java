package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDTO extends Entity<Long> {
    private final Long from_id;
    private final List<Long> to_ids;
    private final String mesaj;
    private final LocalDateTime date;
    private final Long reply_id;

    public MessageDTO( Long from_id, List<Long> to_ids, String mesaj, LocalDateTime date, Long reply_id ) {
        this.from_id = from_id;
        this.to_ids = to_ids;
        this.mesaj = mesaj;
        this.date = date;
        this.reply_id = reply_id;
    }

    public Long getFrom_id() {
        return from_id;
    }

    public List<Long> getTo_ids() {
        return to_ids;
    }

    public Long getReply_id() {
        return reply_id;
    }

    public String getMesaj() {
        return mesaj;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String toIdToString() {
        StringBuilder s = new StringBuilder();
        this.to_ids.forEach(x -> {
            s.append(x);
            s.append(",");
        });
        return s.toString();
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "from_id=" + from_id +
                ", to_ids=" + this.toIdToString() +
                ", reply_id=" + reply_id +
                ", mesaj='" + mesaj + '\'' +
                ", date=" + date +
                '}';
    }
}
