package by.teachmeskills.eshop.repositories;

import by.teachmeskills.eshop.entities.Product;

import java.util.List;

public interface ProductRepository {
    Product getProductById(int productId);

    List<Product> getProductsByCategoryId(int categoryId);

    List<Product> getProductsForOnePage(String request, int page);

    List<Integer> findAllProductsQuantityByRequest(String request);
}
