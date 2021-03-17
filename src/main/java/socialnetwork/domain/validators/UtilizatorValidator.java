package socialnetwork.domain.validators;

import socialnetwork.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate( Utilizator entity ) throws ValidationException {
        String msgs = "";
        if (entity.getId() <= 0)
            msgs += "Id invalid ";
        if (entity.getFirstName() == null || "".equals(entity.getFirstName()))
            msgs += "Nume invalid ";
        if (entity.getLastName() == null || "".equals(entity.getLastName()))
            msgs += "Prenume invalid ";
        if (!msgs.equals(""))
            throw new ValidationException(msgs);
    }
}
