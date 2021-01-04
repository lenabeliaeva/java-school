package com.example.demo.dao;

import com.example.demo.models.Option;
import com.example.demo.models.Tariff;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

@Repository
public class OptionDaoImpl implements OptionDao {

    private static final EntityManagerFactory emfObj;

    static {
        emfObj = Persistence.createEntityManagerFactory("eCare");
    }

    private final EntityManager entityManager = emfObj.createEntityManager();

    @Override
    public void add(Option option) {
        entityManager.getTransaction().begin();
        entityManager.persist(option);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Option> getAll() {
        return entityManager.createQuery("select o from Option o").getResultList();
    }

    @Override
    public List<Option> getAllByTariffId(long tariffId) {
        Query query = entityManager.createQuery("select o from Option o left join o.tariffs t where t.id = :i");
        query.setParameter("i", tariffId);
        return query.getResultList();
    }

    @Override
    public void delete(Option option) {
        entityManager.getTransaction().begin();
        entityManager.remove(option);
        entityManager.getTransaction().commit();
    }

    @Override
    public Option getById(long id) {
        entityManager.getTransaction().begin();
        Option option = entityManager.find(Option.class, id);
        entityManager.getTransaction().commit();
        return option;
    }

    @Override
    public void update(Option option) {
        entityManager.getTransaction().begin();
        entityManager.merge(option);
        entityManager.getTransaction().commit();
    }
}