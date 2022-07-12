package by.teachmeskills.eshop.repositories.impl;

import by.teachmeskills.eshop.entities.Image;
import by.teachmeskills.eshop.entities.Product;
import by.teachmeskills.eshop.repositories.ImageRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class ImageRepositoryImpl implements ImageRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Image create(Image entity) {
        entityManager.persist(entity);
        return entity;
    }

    public List<Image> read() {
        return entityManager.createQuery("select i from Image i").getResultList();
    }

    public Image update(Image entity) {
        entityManager.merge(entity);
        return entity;
    }

    public void delete(int id) {
      entityManager.remove(entityManager.find(Image.class,id));
    }

    @Override
    public List<Image> getAllCategoriesImages() {
        Query query = entityManager.createQuery("select i from Image i where i.product.id is null");
        return query.getResultList();
    }

    @Override
    public List<Image> getImagesByProductId(int productId) {
        Query query = entityManager.createQuery("select i from Image i where i.product.id=:productId");
        query.setParameter("productId", productId);
        return query.getResultList();
    }

    @Override
    public List<Image> getAllOrderPrimaryImagesByUserId(int userId) {
        Query query = entityManager.createQuery("select distinct i from Image i where i.product.id in (select key(o.products).id from Order o where o.user.id=:userId) " +
                "and i.primaryFlag = true");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<Image> getPrimaryImagesByCategoryId(int categoryId) {
        Query query = entityManager.createQuery("select i from Image i where i.category.id=:categoryId");
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    @Override
    public List<Image> getPrimaryImagesByListProducts(List<Product> products) {
        List<Image> images = new ArrayList<>();
        for (Product product : products) {
            Query query = entityManager.createQuery("select distinct i from Image i where i.primaryFlag=true and i.product.id=:productId");
            query.setParameter("productId", product.getId());
            Image image = (Image) query.getSingleResult();
            images.add(image);
        }
        return images;
    }
}

