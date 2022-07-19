package by.teachmeskills.eshop.repositories.impl;

import by.teachmeskills.eshop.entities.Order;
import by.teachmeskills.eshop.entities.Product;
import by.teachmeskills.eshop.repositories.OrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
@Transactional
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Order create(Order entity) {
        entityManager.persist(entity);
        return entity;
    }

    public List<Order> read() {
        return entityManager.createQuery("select o from Order o").getResultList();
    }

    public Order update(Order entity) {
        entityManager.merge(entity);
        return entity;
    }

    public void delete(int id) {
        entityManager.remove(entityManager.find(Order.class, id));
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        Query query = entityManager.createQuery("select o from Order o where o.user.id=:userId");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

}
