package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.UserFriendDto;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.UserChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PrietenieService implements Observable<UserChangeEvent> {
    private final Repository<Tuple<Long, Long>, Prietenie> repoFriendship;
    private final Repository<Long, Utilizator> repoUser;

    public PrietenieService( Repository<Tuple<Long, Long>, Prietenie> repoFriendship, Repository<Long, Utilizator> repoUser ) {
        this.repoFriendship = repoFriendship;
        this.repoUser = repoUser;
    }

    /**
     * adds new friendship
     *
     * @param id1 Long-id of the first user
     * @param id2 Long-id of the second user
     */
    public void addPrietenie( Long id1, Long id2 ) {
        Utilizator u1 = repoUser.findOne(id1);
        Utilizator u2 = repoUser.findOne(id2);
        Prietenie pr = new Prietenie(id1, id2);
        pr.setDate(LocalDateTime.now());
        Prietenie fr = repoFriendship.save(pr);
        if (fr != null) {
            u1.addPrieten(u2);
            u2.addPrieten(u1);
        }
    }

    /**
     * gets a list of all friendships
     *
     * @return Iterable Prietenie
     */
    public Iterable<Prietenie> getAll() {
        return repoFriendship.findAll();
    }

    /**
     * removes a friendship
     *
     * @param st Long st-id of the first user
     * @param dr Long dr-id of the second user
     */
    public void removePrietenie( Long st, Long dr ) {
        Iterable<Prietenie> lista = getAll();
        Tuple<Long, Long> t = new Tuple<>((long) 0, (long) 0);
        for (Prietenie p : lista) {
            if (p.getId().getLeft().equals(st) && p.getId().getRight().equals(dr)) {
                t.setLeft(st);
                t.setRight(dr);
                break;
            }
        }
        Utilizator u1 = repoUser.findOne(st);
        Utilizator u2 = repoUser.findOne(dr);
        u1.unfriend(u2);
        u2.unfriend(u1);
        Prietenie p = repoFriendship.delete(t);
        if (p != null) {
            UserFriendDto dto = new UserFriendDto(u2.getFirstName(), u2.getLastName(), p.getDate());
            notifyObserver(new UserChangeEvent(ChangeEventType.DELETE, dto));
        }
    }

    /**
     * finds the friends of a given user
     *
     * @param u:Utilizator
     * @return list of Strings-the user's friends' names
     */
    public List<String> getFriends( Utilizator u ) {
        List<Prietenie> lista = StreamSupport
                .stream(repoFriendship.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return lista.stream()
                .filter(x -> x.getId().getLeft().equals(u.getId()) || x.getId().getRight().equals(u.getId()))
                .map(x -> {
                    Utilizator us = repoUser.findOne(x.getId().getLeft());
                    if (x.getId().getLeft().equals(u.getId()))
                        us = repoUser.findOne(x.getId().getRight());
                    return us.getFirstName() + "|" + us.getLastName() + "|" + x.getDate();
                })
                .collect(Collectors.toList());

    }

    /**
     * finds the friends of a given user that were added in a given month of the year
     *
     * @param u:Utilizator
     * @param month:String
     * @return list of strings-list of user's friends' names
     */
    public List<String> getFriendsMonth( Utilizator u, String month ) {
        List<Prietenie> lista = StreamSupport
                .stream(repoFriendship.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return lista.stream()
                .filter(x -> x.getId().getLeft().equals(u.getId()) || x.getId().getRight().equals(u.getId()))
                .filter(x -> x.getDate().getMonth().toString().equals(month))
                .map(x -> {
                    Utilizator ut = repoUser.findOne(x.getId().getLeft());
                    if (x.getId().getLeft().equals(u.getId()))
                        ut = repoUser.findOne(x.getId().getRight());
                    return ut.getFirstName() + "|" + ut.getLastName() + "|" + x.getDate();

                })
                .collect(Collectors.toList());
    }

    /**
     * gets a list of dtos for a given user (dto contains first and last name of a friend and the date they became friends)
     *
     * @param u:Utilizator
     * @return list of UserFriendDto
     */
    public List<UserFriendDto> getDtoUser( Utilizator u ) {
        return StreamSupport.stream(repoFriendship.findAll().spliterator(), false)
                .filter(x -> x.getId().getLeft().equals(u.getId()) || x.getId().getRight().equals(u.getId()))
                .map(x -> {
                    Utilizator us = repoUser.findOne(x.getId().getLeft());
                    if (x.getId().getLeft().equals(u.getId()))
                        us = repoUser.findOne(x.getId().getRight());
                    return new UserFriendDto(us.getFirstName(), us.getLastName(), x.getDate());
                })
                .collect(Collectors.toList());
    }

    private final List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver( Observer<UserChangeEvent> e ) {
        observers.add(e);
    }

    @Override
    public void removeObserver( Observer<UserChangeEvent> e ) {
        //observers.remove(e);
    }

    @Override
    public void notifyObserver( UserChangeEvent t ) {
        observers.forEach(x -> x.update(t));
    }
}
