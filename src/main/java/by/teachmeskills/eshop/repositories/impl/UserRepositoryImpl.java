package by.teachmeskills.eshop.repositories.impl;

import by.teachmeskills.eshop.entities.User;
import by.teachmeskills.eshop.repositories.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public User create(User entity) {
        entityManager.persist(entity);
        return entity;
    }

    public List<User> read() {
        return entityManager.createQuery("select u from User u").getResultList();
    }

    public User update(User entity) {
        entityManager.merge(entity);
        return entity;
    }

    public void delete(int id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public User getUserFromBaseByLoginAndPassword(User user) {
        Query query = entityManager.createQuery("select u from User u where u.login=:login and u.password=:password");
        query.setParameter("login", user.getLogin());
        query.setParameter("password", user.getPassword());
        return (User) query.getSingleResult();
    }

    @Override
    public User getUserFromBaseByLogin(User user) {
        Query query = entityManager.createQuery("select u from User u where u.login=:login");
        query.setParameter("login", user.getLogin());
        return (User) query.getSingleResult();
    }

    @Override
    public User getUserById(int id) {
        return entityManager.find(User.class, id);
    }
}
