package pbo.f01;

/**
 * 12S22033 - Mickael Sitompul
 * 12S22027 - Ferry Panjaitan
 * mvn clean compile assembly:single
 * java -cp .\target\f01-1.0-SNAPSHOT-jar-with-dependencies.jar pbo.f01.App
 */

import java.util.List;
import java.util.function.BiPredicate;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import pbo.f01.model.Dorm;
import pbo.f01.model.Student;

public class Constrac {
    private EntityManagerFactory factory;
    private EntityManager entityManager;
    private BiPredicate<Object, Boolean> saveEntity;

    public Constrac(String persistenceUnit) {
        this.factory = Persistence.createEntityManagerFactory(persistenceUnit);
        this.entityManager = factory.createEntityManager();
        this.saveEntity = (entityObject, insert) -> {
            entityManager.getTransaction().begin();
            if (insert) {
                entityManager.persist(entityObject);
            } else {
                entityManager.merge(entityObject);
            }
            entityManager.flush();
            entityManager.getTransaction().commit();
            return true;
        };
        this.cleanTables();
    }

    public void shutdown() {
        this.entityManager.close();
        this.factory.close();
    }

    public void save(Object object) {
        this.saveEntity.test(object, true);
    }

    public void update(Object object) {
        this.saveEntity.test(object, false);
    }

    public Student findStudent(String id) {
        return this.entityManager.find(Student.class, id);
    }

    public Dorm findDorm(String name) {
        return this.entityManager.find(Dorm.class, name);
    }

    public List<Dorm> getAllDorms() {
        String jpql = "SELECT d FROM Dorm d ORDER BY d.name";
        return this.entityManager.createQuery(jpql, Dorm.class)
                .getResultList();
    }

    private void cleanTables() {
        String deleteStudentJpql = "DELETE FROM Student s";
        String deleteDormJpql = "DELETE FROM Dorm d";

        this.entityManager.getTransaction().begin();
        this.entityManager.createQuery(deleteStudentJpql).executeUpdate();
        this.entityManager.createQuery(deleteDormJpql).executeUpdate();
        this.entityManager.flush();
        this.entityManager.getTransaction().commit();
    }
}
