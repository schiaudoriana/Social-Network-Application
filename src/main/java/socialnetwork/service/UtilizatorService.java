package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class UtilizatorService {
    private final Repository<Long, Utilizator> repoUser;
    private final Repository<Tuple<Long, Long>, Prietenie> repoFriendship;

    public UtilizatorService( Repository<Long, Utilizator> repoUser, Repository<Tuple<Long, Long>, Prietenie> repoFriendship ) {
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;
        //initialise each user with his friends list
        Iterable<Prietenie> pr = this.repoFriendship.findAll();
        for (Prietenie prietenie : pr) {
            long id1 = prietenie.getId().getLeft();
            long id2 = prietenie.getId().getRight();
            Utilizator u1 = repoUser.findOne(id1);
            Utilizator u2 = repoUser.findOne(id2);
            u1.addPrieten(u2);
            u2.addPrieten(u1);
        }
    }

    /**
     * adds an user with the given id, first and last name
     *
     * @param id Long
     * @param nume String
     * @param prenume String
     */
    public void addUtilizator( Long id, String nume, String prenume ) {

        Utilizator user = new Utilizator(nume, prenume);
        user.setId(id);
        repoUser.save(user);
    }

    /**
     * finds all users
     *
     * @return Iterable Utilizator
     */
    public Iterable<Utilizator> getAll() {
        return repoUser.findAll();
    }


    /**
     * deletes an user given by id
     *
     * @param id Long- id ul pe care vrem sa il stergem
     */
    public void removeUtilizator( Long id ) {

        Utilizator user = repoUser.delete(id);
        List<Tuple<Long, Long>> lista = new ArrayList<>();
        for (Prietenie prietenie : repoFriendship.findAll()) {
            if (prietenie.getId().getLeft().equals(id)) {
                lista.add(new Tuple<>(id, prietenie.getId().getRight()));
                Utilizator us = repoUser.findOne(prietenie.getId().getRight());
                us.unfriend(user);
            } else if (prietenie.getId().getRight().equals(id)) {
                lista.add(new Tuple<>(prietenie.getId().getLeft(), id));
                Utilizator us = repoUser.findOne(prietenie.getId().getLeft());
                us.unfriend(user);
            }

        }
        for (Tuple<Long, Long> t : lista)
            repoFriendship.delete(t);

    }

    /**
     * finds the user with the given id
     *
     * @param id: Long-id of the user looked for
     * @return Utilizator
     * null-if there is no user with the given id
     */
    public Utilizator findOne( Long id ) {
        return repoUser.findOne(id);
    }

    /**
     * modifies an user
     *
     * @param id Long
     * @param nume String
     * @param prenume String
     */
    public void updateUtilizator( Long id, String nume, String prenume ) {
        Utilizator user = new Utilizator(nume, prenume);
        user.setId(id);
        List<Utilizator> friends = repoUser.findOne(id).getFriends();
        friends.forEach(user::addPrieten);
        repoUser.update(user);
        for (Utilizator u : repoUser.findAll()) {
            for (Utilizator p : u.getFriends()) {
                if (p.getId().equals(id)) {
                    p.setFirstName(nume);
                    p.setLastName(prenume);
                }
            }
        }

    }

    /**
     * gets the id of a user given by first and last name
     *
     * @param nume:String
     * @param prenume:String
     * @return Long: id of the user looked for
     */
    public Long getUserId( String nume, String prenume ) {
        Long id = null;
        for (Utilizator utilizator : getAll()) {
            if (utilizator.getLastName().equals(prenume) && utilizator.getFirstName().equals(nume))
                id = utilizator.getId();
        }
        return id;
    }

}
