import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PetDAO {

    public List<Pet> findNamedPetsBySpecies(Session session, String species) {
//        session.beginTransaction();
//        Criteria cr = session.createCriteria(Pet.class);
//        cr.list();
//        Restrictions
        final List<Pet> petList = (List<Pet>) session.createQuery("FROM Pet").list();
        return petList.stream()
                .filter(el -> el.species.equals(species))
                .collect(Collectors.toList());

    }

    @Entity(name = "Pet")
    @Table
    public static class Pet {
        @Id
        public Integer id;
        @Column
        public String name;
        @Column
        public String species;

        public Pet() {
        }

        public Pet(Integer id, String name, String species) {
            this.id = id;
            this.name = name;
            this.species = species;
        }
    }

    public static void main(String[] args) {


        Properties prop = new Properties();
        prop.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        prop.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        prop.setProperty("hibernate.connection.username", "postgres");
        prop.setProperty("hibernate.connection.password", "admin");
        prop.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/poligon_doswiadczalny");

        SessionFactory sessionFactory = new Configuration().addProperties(prop)
                .addAnnotatedClass(Pet.class).buildSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Pet dog = new Pet(0, "Lady", "Dog");
        Pet cat = new Pet(1, "Max", "Cat");
        Pet camel = new Pet(2, null, "Camel");

        session.save(dog);
        session.save(cat);
        session.save(camel);
        session.flush();

        PetDAO petDao = new PetDAO();
        List<Pet> pets = petDao.findNamedPetsBySpecies(session, "Cat");
        for (Pet p : pets) {
            System.out.println(p.id);
            System.out.println(p.name);
            System.out.println(p.species);
        }
    }
}
