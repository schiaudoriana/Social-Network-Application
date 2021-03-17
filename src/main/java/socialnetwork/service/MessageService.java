package socialnetwork.service;

import socialnetwork.controller.SendMessageType;
import socialnetwork.domain.Message;
import socialnetwork.domain.MessageDTO;
import socialnetwork.domain.MessageUserDto;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.Repository;
import socialnetwork.utils.events.MessageChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MessageService implements Observable<MessageChangeEvent> {
    private final Repository<Long, Utilizator> repoUser;
    private final Repository<Long, MessageDTO> repoMessage;

    public MessageService( Repository<Long, Utilizator> repoUser, Repository<Long, MessageDTO> repoMessage ) {
        this.repoUser = repoUser;
        this.repoMessage = repoMessage;
    }

    /**
     * finds the message with the given id
     *
     * @param messId: Long
     * @return MessageDto
     */
    public MessageDTO findOne( Long messId ) {
        return repoMessage.findOne(messId);
    }

    /**
     * gets all the messages
     *
     * @return Iterable of Message entities
     */
    public Iterable<Message> getAll() {
        List<Message> lista = new ArrayList<>();
        repoMessage.findAll().forEach(x -> {
            Utilizator from = repoUser.findOne(x.getFrom_id());
            List<Utilizator> to = new ArrayList<>();
            x.getTo_ids().forEach(y -> {
                Utilizator to_user = repoUser.findOne(y);
                to.add(to_user);
            });
            Message m = new Message(from, to, x.getMesaj(), x.getDate(), x.getReply_id());
            m.setId(x.getId());
            lista.add(m);
        });

        return lista;
    }

    /**
     * replies to a message
     *
     * @param idFrom:Long-id         of the user that replies
     * @param idTo:Long-id           of the user that is replied to
     * @param mesaj:String-the       message itself
     * @param date:LocalDateTime-the date and time when the reply is sent
     * @param reply:Long-the         id of the message that is replied to
     * @return MessageDTO
     */
    public MessageDTO replyToOne( Long idFrom, Long idTo, String mesaj, LocalDateTime date, Long reply ) {
        MessageDTO mes = repoMessage.findOne(reply);
        repoUser.findOne(idFrom);
        repoUser.findOne(idTo);
        if (!mes.getTo_ids().contains(idFrom) || !(mes.getTo_ids().contains(idTo) || mes.getFrom_id().equals(idTo))) {
            return null;
        } else {
            Long id = (long) ((List<Message>) this.getAll()).size() + 1;
            List<Long> toList = new ArrayList<>();
            toList.add(idTo);
            MessageDTO m = new MessageDTO(idFrom, toList, mesaj, date, reply);
            m.setId(id);
            repoMessage.save(m);

            notifyObserver(new MessageChangeEvent(null, SendMessageType.REPLY));
            return m;
        }
    }

    /**
     * replies to all users that received a specific message
     *
     * @param idFrom:Long-            id of the user that replies
     * @param mesaj:String-the        message itself
     * @param date:LocalDateTime-date and time when the reply is sent
     * @param reply:                  Long-the id of the message that is replied to
     */
    public void replyToAll( Long idFrom, String mesaj, LocalDateTime date, Long reply ) {
        MessageDTO m = repoMessage.findOne(reply);
        repoUser.findOne(idFrom);
        List<Long> to = m.getTo_ids();
        to.add(m.getFrom_id());
        to.remove(idFrom);
        Long id = (long) ((List<Message>) this.getAll()).size() + 1;
        MessageDTO mes = new MessageDTO(idFrom, to, mesaj, date, reply);
        mes.setId(id);
        repoMessage.save(mes);

        //MessageUserDto dto=new MessageUserDto(idFrom,us.getFirstName()+" "+us.getLastName(),mesaj, date,reply);

        notifyObserver(new MessageChangeEvent(null, SendMessageType.REPLY_ALL));

    }

    /**
     * shows the conversation between two users given by id
     *
     * @param id1:Long-id user1
     * @param id2:Long-id user2
     * @return list of Message entities
     */
    public List<Message> showConversation( Long id1, Long id2 ) {
        Utilizator fromUser = repoUser.findOne(id1);
        Utilizator toUser = repoUser.findOne(id2);
        List<Message> list = new ArrayList<>();
        this.getAll().forEach(x -> {
            if (x.getFrom().getId().equals(id1) && x.getTo().contains(toUser))
                list.add(x);
            else if (x.getFrom().getId().equals(id2) && x.getTo().contains(fromUser))
                list.add(x);
        });
        return list.stream()
                .sorted(Comparator.comparing(Message::getData))
                .collect(Collectors.toList());

    }

    /**
     * starts a new conversation
     *
     * @param id1:Long-        id of the user that starts the conversation
     * @param idTo:            list of Long-ids of the user that receive the message
     * @param mesaj:String-the message itself
     */
    public void startConversation( Long id1, List<Long> idTo, String mesaj ) {
        repoUser.findOne(id1);
        idTo.forEach(repoUser::findOne);
        MessageDTO m = new MessageDTO(id1, idTo, mesaj, LocalDateTime.now(), null);
        m.setId((long) ((List<Message>) this.getAll()).size() + 1);
        repoMessage.save(m);

        notifyObserver(new MessageChangeEvent(null, SendMessageType.START_CONVERSATION));

    }

    /**
     * shows the list of messages a user given by id received
     *
     * @param id:Long-id of the user
     * @return list of MessageUserDto
     */
    public List<MessageUserDto> showUserMessages( Long id ) {
        Utilizator user = repoUser.findOne(id);
        return StreamSupport.stream(this.getAll().spliterator(), false)
                .filter(x -> x.getTo().contains(user) && !x.getFrom().getId().equals(id))
                .map(x -> {
                    String name = x.getFrom().getFirstName() + " " + x.getFrom().getLastName();
                    return new MessageUserDto(x.getId(), name, x.getMessage(), x.getData(), x.getReply());
                })
                .collect(Collectors.toList());
    }

    private final List<Observer<MessageChangeEvent>> observers = new ArrayList<>();


    @Override
    public void addObserver( Observer<MessageChangeEvent> e ) {
        observers.add(e);
    }

    @Override
    public void removeObserver( Observer<MessageChangeEvent> e ) {
        observers.remove(e);
    }

    @Override
    public void notifyObserver( MessageChangeEvent t ) {
        observers.forEach(x -> x.update(t));
    }
}