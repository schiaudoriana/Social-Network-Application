package socialnetwork.domain;


import java.time.LocalDateTime;


public class Prietenie extends Entity<Tuple<Long, Long>> {

    LocalDateTime date;

    public Prietenie( Long s, Long d ) {
        this.setId(new Tuple<>(s, d));

    }

    @Override
    public String toString() {
        return "Prietenie{" +
                this.getId().getLeft() + ";" +
                this.getId().getRight() + ";" +
                "date=" + date +
                '}';
    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate( LocalDateTime d ) {
        this.date = d;
    }
}
