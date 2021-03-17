package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.RequestChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestService implements Observable<RequestChangeEvent> {
    private final Repository<Tuple<Long, Long>, Request> repoRequest;
    private final Repository<Long, Utilizator> repoUser;
    private final Repository<Tuple<Long, Long>, Prietenie> repoFriendship;


    public RequestService( Repository<Tuple<Long, Long>, Request> repoRequest, Repository<Long, Utilizator> repoUser, Repository<Tuple<Long, Long>, Prietenie> repoFriendship ) {
        this.repoRequest = repoRequest;
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;
    }

    /**
     * adds a friend request from a user to another
     *
     * @param id1:Long-id of the user that sends the friend request
     * @param id2:Long-id of the user that the request is sent to
     */
    public void sendRequest( Long id1, Long id2 ) {
        repoUser.findOne(id1);
        repoUser.findOne(id2);
        Request r = new Request(id1, id2, "pending");
        r.setDate(LocalDateTime.now());
        repoRequest.save(r);

    }

    /**
     * responds to a friend request-accept/decline
     *
     * @param id1:Long-id            of the user that sent the request
     * @param id2:Long-id            of the user that replies to the request
     * @param response:String-user's reply to the request
     */
    public void respondRequest( Long id1, Long id2, String response ) {
        Utilizator u1 = repoUser.findOne(id1);
        Utilizator u2 = repoUser.findOne(id2);
        Request req = repoRequest.findOne(new Tuple<>(id1, id2));
        if (req.getStatus().equals("pending")) {
            Request r = new Request(id1, id2, response);
            r.setDate(LocalDateTime.now());
            repoRequest.update(r);
            RequestUserDto dto = new RequestUserDto(u2.getFirstName(), u2.getLastName(), r.getStatus(), r.getDate());
            notifyObserver(new RequestChangeEvent(ChangeEventType.ADD, dto));
            if (response.equals("approved")) {
                Prietenie p = new Prietenie(id1, id2);
                p.setDate(LocalDateTime.now());
                if (repoFriendship.save(p) != null) {
                    u1.addPrieten(u2);
                    u2.addPrieten(u1);
                }
            }
        }
    }

    /**
     * gets the friend requests list of a user given by id
     *
     * @param id:Long-user's id
     * @return list of RequestUserDto-user's friend requests
     */
    public List<RequestUserDto> getUserRequests( Long id ) {
        return StreamSupport.stream(repoRequest.findAll().spliterator(), false)
                .filter(r -> r.getId().getRight().equals(id))
                .map(r -> {
                    Utilizator us = repoUser.findOne(r.getId().getLeft());
                    return new RequestUserDto(us.getFirstName(), us.getLastName(), r.getStatus(), r.getDate());
                })
                .collect(Collectors.toList());
    }


    private final List<Observer<RequestChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver( Observer<RequestChangeEvent> e ) {
        observers.add(e);
    }

    @Override
    public void removeObserver( Observer<RequestChangeEvent> e ) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver( RequestChangeEvent t ) {
        observers.forEach(x -> x.update(t));
    }

    /**
     * cancels a sent friend request
     *
     * @param id1:Long-id of the user that sent the friend requests and wants to cancel it
     * @param id2:Long-id of the user that the request was sent to
     */
    public void cancelRequest( Long id1, Long id2 ) {
        repoUser.findOne(id1);
        Utilizator u2 = repoUser.findOne(id2);
        Tuple<Long, Long> t = new Tuple<>(id1, id2);
        Request req = repoRequest.findOne(t);
        if (req.getStatus().equals("pending")) {
            req = repoRequest.delete(t);
            RequestUserDto dto = new RequestUserDto(u2.getFirstName(), u2.getLastName(), req.getStatus(), req.getDate());
            notifyObserver(new RequestChangeEvent(ChangeEventType.DELETE, dto));
        }

    }

    /**
     * finds the requests sent by a user given by id
     *
     * @param id:Long-id of the user
     * @return List of RequestUserDto:sent requests
     */
    public List<RequestUserDto> getUserSentRequests( Long id ) {
        return StreamSupport.stream(repoRequest.findAll().spliterator(), false)
                .filter(r -> r.getId().getLeft().equals(id))
                .map(r -> {
                    Utilizator us = repoUser.findOne(r.getId().getRight());
                    return new RequestUserDto(us.getFirstName(), us.getLastName(), r.getStatus(), r.getDate());
                })
                .collect(Collectors.toList());
    }
}
