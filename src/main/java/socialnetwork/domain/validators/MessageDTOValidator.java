package socialnetwork.domain.validators;

import socialnetwork.domain.MessageDTO;

public class MessageDTOValidator implements Validator<MessageDTO> {
    @Override
    public void validate( MessageDTO entity ) throws ValidationException {
        StringBuilder msgs = new StringBuilder();
        if (entity.getFrom_id() <= 0)
            msgs.append("Id FROM invalid ");
        for (Long to_id : entity.getTo_ids()) {
            if (to_id <= 0)
                msgs.append("Id TO invalid ");
        }
        if (entity.getMesaj().equals(""))
            msgs.append("Mesaj gol ");
        if (!msgs.toString().equals(""))
            throw new ValidationException(msgs.toString());
    }
}
