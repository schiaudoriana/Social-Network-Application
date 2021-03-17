package socialnetwork.domain;

import java.time.LocalDateTime;

public class Request extends Entity<Tuple<Long, Long>> {
    private String status;
    private LocalDateTime date;

    public Request( Long id1, Long id2, String status ) {
        this.setId(new Tuple<>(id1, id2));
        this.status = status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDate( LocalDateTime date ) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Request{" +
                "from=" + this.getId().getLeft() +
                "to=" + this.getId().getRight() +
                "status='" + status + '\'' +
                '}';
    }

}
