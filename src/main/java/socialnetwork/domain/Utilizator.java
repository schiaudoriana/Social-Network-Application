package socialnetwork.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final List<Utilizator> friends = new ArrayList<>();
    private String friendsString;

    public Utilizator( String firstName, String lastName ) {
        this.firstName = firstName;
        this.lastName = lastName;


    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName( String firstName ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName ) {
        this.lastName = lastName;
    }

    public List<Utilizator> getFriends() {
        return friends;
    }

    public String friendsToString() {
        StringBuilder s = new StringBuilder();
        this.friends.forEach(x -> {
            s.append(x.getFirstName());
            s.append(" ");
            s.append(x.getLastName());
            s.append(",");
        });
        return s.toString();
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ",friends='" + friendsToString() + '\'' +
                '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    public void addPrieten( Utilizator u ) {
        this.friends.add(u);
    }

    public void unfriend( Utilizator u ) {
        this.friends.remove(u);
    }

    public String getFriendsString() {
        return friendsToString();
    }
}