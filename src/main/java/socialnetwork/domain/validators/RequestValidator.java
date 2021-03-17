package socialnetwork.domain.validators;

import socialnetwork.domain.Request;

public class RequestValidator implements Validator<Request> {
    @Override
    public void validate( Request entity ) throws ValidationException {
        String msgs = "";
        if (entity.getId().getLeft() <= 0)
            msgs += "Id from invalid ";
        if (entity.getId().getRight() <= 0)
            msgs += "Id to invalid ";
        if (!(entity.getStatus().equals("pending") || entity.getStatus().equals("approved") || entity.getStatus().equals("rejected")))
            msgs += "Status invalid ";
        if (!msgs.equals(""))
            throw new ValidationException(msgs);
    }
}
