package by.teachmeskills.eshop.repositories.impl;

import by.teachmeskills.eshop.entities.Category;
import by.teachmeskills.eshop.repositories.CategoryRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
@Log4j
public class CategoryRepositoryImpl implements CategoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Category create(Category entity) {
        try {
            entityManager.persist(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return entity;
    }

    public List<Category> read() {
        return entityManager.createQuery("select c from Category c").getResultList();
    }

    public Category update(Category entity) {
        entityManager.merge(entity);
        return entity;
    }

    public void delete(int id) {
        entityManager.remove(entityManager.find(Category.class, id));
    }

    @Override
    public Category findCategoryById(int id) {
        return entityManager.find(Category.class, id);
    }
}
