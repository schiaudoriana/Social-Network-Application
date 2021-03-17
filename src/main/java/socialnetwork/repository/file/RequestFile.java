package socialnetwork.repository.file;

import socialnetwork.domain.Request;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class RequestFile extends AbstractFileRepository<Tuple<Long, Long>, Request> {
    public RequestFile( String fileName, Validator<Request> validator ) {
        super(fileName, validator);
    }

    @Override
    public Request extractEntity( List<String> attributes ) {
        Request r = new Request(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1)), attributes.get(2));
        r.setDate(LocalDateTime.parse(attributes.get(3)));
        return r;
    }

    @Override
    protected String createEntityAsString( Request entity ) {
        return entity.getId().getLeft() + ";" + entity.getId().getRight() + ";" + entity.getStatus() + ";" + entity.getDate();
    }
}
