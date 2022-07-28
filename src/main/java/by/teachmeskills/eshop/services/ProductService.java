package by.teachmeskills.eshop.services;

import by.teachmeskills.eshop.entities.Product;
import by.teachmeskills.eshop.entities.User;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.Writer;

public interface ProductService extends BaseService<Product> {
    ModelAndView showSearchProductPage(User user);

    ModelAndView getProductData(User user, int id);

    ModelAndView getCategoryProductsData(int id, String nameCategory);

    ModelAndView findAllProductsByRequest(String request, int page);

    void writeProductsCategoryToCsv(int categoryId, Writer writer);

    void saveProductsCategoryFromCsv(int id, MultipartFile file);
}
