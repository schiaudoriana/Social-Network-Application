package socialnetwork.repository.file;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.util.List;

public class PrietenieFile extends AbstractFileRepository<Tuple<Long, Long>, Prietenie> {

    public PrietenieFile( String fileName, Validator<Prietenie> validator ) {
        super(fileName, validator);
    }

    @Override
    public Prietenie extractEntity( List<String> attributes ) {
        Prietenie pr = new Prietenie(Long.parseLong(attributes.get(0)), Long.parseLong(attributes.get(1)));
        if (attributes.get(2) == null)
            pr.setDate(null);
        else pr.setDate(LocalDateTime.parse(attributes.get(2)));
        return pr;
    }

    @Override
    protected String createEntityAsString( Prietenie entity ) {
        return entity.getId().getLeft() + ";" + entity.getId().getRight() + ";" + entity.getDate();
    }

}
