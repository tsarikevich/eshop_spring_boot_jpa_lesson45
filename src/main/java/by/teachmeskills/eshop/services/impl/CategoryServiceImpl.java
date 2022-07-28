package by.teachmeskills.eshop.services.impl;

import by.teachmeskills.eshop.entities.Category;
import by.teachmeskills.eshop.entities.Image;
import by.teachmeskills.eshop.entities.User;
import by.teachmeskills.eshop.repositories.impl.CategoryRepositoryImpl;
import by.teachmeskills.eshop.repositories.impl.ImageRepositoryImpl;
import by.teachmeskills.eshop.services.CategoryService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static by.teachmeskills.eshop.utils.EshopConstants.REDIRECT_TO_LOGIN_PAGE;
import static by.teachmeskills.eshop.utils.PagesPathEnum.HOME_PAGE;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.CATEGORIES;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.CATEGORIES_IMAGES;

@Service
@Log4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepositoryImpl categoryRepository;
    private final ImageRepositoryImpl imageRepository;

    public CategoryServiceImpl(CategoryRepositoryImpl categoryRepository, ImageRepositoryImpl imageRepository) {
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public Category create(Category entity) {
        return categoryRepository.create(entity);
    }

    @Override
    public List<Category> read() {
        return categoryRepository.read();
    }

    @Override
    public Category update(Category entity) {
        return categoryRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        categoryRepository.delete(id);
    }

    @Override
    public ModelAndView getAllCategories(User user) {
        if (Optional.ofNullable(user.getLogin()).isPresent()
                && Optional.ofNullable(user.getPassword()).isPresent()
                && Optional.ofNullable(user.getEmail()).isPresent()) {
            ModelMap model = new ModelMap();
            List<Category> categories = read();
            List<Image> categoriesImages = imageRepository.getAllCategoriesImages();
            model.addAttribute(CATEGORIES.getValue(), categories);
            model.addAttribute(CATEGORIES_IMAGES.getValue(), categoriesImages);
            return new ModelAndView(HOME_PAGE.getPath(), model);
        } else {
            return new ModelAndView(REDIRECT_TO_LOGIN_PAGE);
        }
    }

    @Override
    public void writeCategoriesToCsv(Writer writer) {
        try {
            List<Category> categories = read();
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            beanToCsv.write(categories);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void saveCategoriesFromCsv(MultipartFile file) {
        List<Category> csvCategories = parseCsv(file);
        if (Optional.ofNullable(csvCategories).isPresent() && csvCategories.size() > 0) {
            csvCategories.stream().forEach(categoryRepository::create);
        }
    }

    private List<Category> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<Category> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Category.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .withSeparator(',')
                        .build();
                return csvToBean.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.error("Empty CSV file is uploaded.");
        }
        return Collections.emptyList();
    }
}

