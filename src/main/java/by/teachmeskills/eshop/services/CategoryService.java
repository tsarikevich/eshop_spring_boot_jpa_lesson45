package by.teachmeskills.eshop.services;

import by.teachmeskills.eshop.entities.Category;
import by.teachmeskills.eshop.entities.User;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.Writer;

public interface CategoryService extends BaseService<Category> {
    ModelAndView getAllCategories(User user);

    void writeCategoriesToCsv(Writer writer);

    void saveCategoriesFromCsv(MultipartFile file);
}
