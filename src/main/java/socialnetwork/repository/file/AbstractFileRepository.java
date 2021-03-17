package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

import java.io.*;

import java.util.Arrays;
import java.util.List;


public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;

    public AbstractFileRepository( String fileName, Validator<E> validator ) {
        super(validator);
        this.fileName = fileName;
        loadData();

    }

    /**
     * loads data from file into the memory
     * when an AbstractFileRepository object is created
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                List<String> attr = Arrays.asList(linie.split(";"));
                E e = extractEntity(attr);
                super.save(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //using lambda to load data from the file
//        Path path = Paths.get(fileName);
//        try {
//            List<String> lines = Files.readAllLines(path);
//            lines.forEach(linie -> {
//                E entity=extractEntity(Arrays.asList(linie.split(";")));
//                super.save(entity);
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes-list of strings
     * @return an entity of type E
     */
    public abstract E extractEntity( List<String> attributes );

    /**
     * create entity as a string
     *
     * @param entity:entity to be converted to string
     * @return String that contains entity's attributes
     */
    protected abstract String createEntityAsString( E entity );

    @Override
    public E save( E entity ) {
        E e = super.save(entity);
        if (e == null) {
            writeToFile(entity);
        }
        return e;

    }

    /**
     * writes an entity to file using the createEntityAsString method
     *
     * @param entity: entity to be written to the file
     */
    protected void writeToFile( E entity ) {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName, true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * writes all entities from memory to file
     */
    protected void writeAllToFile() {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName))) {
            for (E e1 : findAll()) {
                bW.write(createEntityAsString(e1));
                bW.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public E delete( ID id ) {
        E e = super.delete(id);
        if (e != null) {
            try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName))) {
                for (E e1 : findAll()) {
                    bW.write(createEntityAsString(e1));
                    bW.newLine();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return e;
    }

    @Override
    public E update( E entity ) {
        E e = super.update(entity);
        writeAllToFile();
        return e;
    }
}

