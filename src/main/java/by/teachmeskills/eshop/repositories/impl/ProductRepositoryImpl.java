package by.teachmeskills.eshop.repositories.impl;

import by.teachmeskills.eshop.entities.Product;
import by.teachmeskills.eshop.repositories.ProductRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static by.teachmeskills.eshop.utils.EshopConstants.QUANTITY_PRODUCTS_ON_PAGE;

@Transactional
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Product create(Product entity) {
        entityManager.persist(entity);
        return entity;
    }

    public List<Product> read() {
        return entityManager.createQuery("select p from Product p").getResultList();
    }

    public Product update(Product entity) {
        entityManager.merge(entity);
        return entity;
    }

    public void delete(int id) {
        entityManager.remove(entityManager.find(Product.class,id));
    }

    @Override
    public Product getProductById(int productId) {
        return entityManager.find(Product.class, productId);
    }

    @Override
    public List<Product> getProductsByCategoryId(int categoryId) {
        Query query = entityManager.createQuery("select p from Product p where p.category.id=:categoryId");
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsForOnePage(String request, int page) {
        int firstResult;
        if (page > 1) {
            firstResult = (page - 1) * QUANTITY_PRODUCTS_ON_PAGE;
        } else {
            firstResult = 0;
        }
        Query query = getQueryProductsByRequest(request);
        query.setFirstResult(firstResult);
        query.setMaxResults(QUANTITY_PRODUCTS_ON_PAGE);
        return query.getResultList();
    }

    @Override
    public List<Integer> findAllProductsQuantityByRequest(String request) {
        int quantityProducts = findQuantityAllProductsByRequest(request);
        int quantityPages;
        List<Integer> pages = new ArrayList<>();
        if (quantityProducts % QUANTITY_PRODUCTS_ON_PAGE != 0 && quantityProducts > QUANTITY_PRODUCTS_ON_PAGE) {
            quantityPages = quantityProducts / QUANTITY_PRODUCTS_ON_PAGE + 1;
        } else if (quantityProducts <= QUANTITY_PRODUCTS_ON_PAGE) {
            quantityPages = 1;
        } else {
            quantityPages = quantityProducts / QUANTITY_PRODUCTS_ON_PAGE;
        }
        for (int i = 1; i <= quantityPages; i++) {
            pages.add(i);
        }
        return pages;
    }

    private Integer findQuantityAllProductsByRequest(String request) {
        Query query = getQueryProductsByRequest(request);
        return query.getResultList().size();
    }

    private Query getQueryProductsByRequest(String request) {
        String setRequest = "%" + request + "%";
        Query query = entityManager.createQuery("select p from Product p where p.name like :setRequest or p.description like: setRequest order by p.id desc");
        query.setParameter("setRequest", setRequest);
        return query;
    }
}

