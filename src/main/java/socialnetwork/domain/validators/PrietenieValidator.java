package socialnetwork.domain.validators;

import socialnetwork.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {
    @Override
    public void validate( Prietenie entity ) throws ValidationException {
        String msgs = "";
        if (entity.getId().getLeft() <= 0)
            msgs += "Id stang invalid ";
        if (entity.getId().getRight() <= 0)
            msgs += "Id drept invalid";
        if (!msgs.equals(""))
            throw new ValidationException(msgs);
    }
}
