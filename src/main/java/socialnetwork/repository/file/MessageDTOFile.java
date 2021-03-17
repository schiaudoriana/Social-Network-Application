package socialnetwork.repository.file;

import socialnetwork.domain.MessageDTO;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageDTOFile extends AbstractFileRepository<Long, MessageDTO> {
    public MessageDTOFile( String fileName, Validator<MessageDTO> validator ) {
        super(fileName, validator);
    }

    @Override
    public MessageDTO extractEntity( List<String> attributes ) {
        List<String> users = Arrays.asList(attributes.get(2).split(","));
        List<Long> to = users.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
        Long id = Long.parseLong(attributes.get(0));
        Long fromid = Long.parseLong(attributes.get(1));
        String msg = attributes.get(3);
        LocalDateTime date = LocalDateTime.parse(attributes.get(4));
        MessageDTO m;
        if (attributes.size() == 5) {
            m = new MessageDTO(fromid, to, msg, date, null);
        } else {
            Long replyid = Long.parseLong(attributes.get(5));
            m = new MessageDTO(fromid, to, msg, date, replyid);
        }
        m.setId(id);
        return m;

    }

    protected String toListToString( List<Long> l ) {
        StringBuilder s = new StringBuilder();
        l.forEach(x -> s.append(x).append(","));
        s.deleteCharAt(s.length() - 1);
        return s.toString();
    }

    @Override
    protected String createEntityAsString( MessageDTO entity ) {
        if (entity.getReply_id() == null)
            return entity.getId() + ";" + entity.getFrom_id() + ";" + toListToString(entity.getTo_ids()) + ";" + entity.getMesaj() + ";" + entity.getDate();
        else
            return entity.getId() + ";" + entity.getFrom_id() + ";" + toListToString(entity.getTo_ids()) + ";" + entity.getMesaj() + ";" + entity.getDate() + ";" + entity.getReply_id();
    }


}
