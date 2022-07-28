package by.teachmeskills.eshop.services.impl;

import by.teachmeskills.eshop.entities.Category;
import by.teachmeskills.eshop.entities.Image;
import by.teachmeskills.eshop.entities.Product;
import by.teachmeskills.eshop.entities.User;
import by.teachmeskills.eshop.repositories.impl.CategoryRepositoryImpl;
import by.teachmeskills.eshop.repositories.impl.ImageRepositoryImpl;
import by.teachmeskills.eshop.repositories.impl.ProductRepositoryImpl;
import by.teachmeskills.eshop.services.ProductService;
import by.teachmeskills.eshop.utils.EshopConstants;
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

import static by.teachmeskills.eshop.utils.EshopConstants.CATEGORY_ID;
import static by.teachmeskills.eshop.utils.EshopConstants.REDIRECT_TO_LOGIN_PAGE;
import static by.teachmeskills.eshop.utils.PagesPathEnum.CATEGORY_PAGE;
import static by.teachmeskills.eshop.utils.PagesPathEnum.PRODUCT_PAGE;
import static by.teachmeskills.eshop.utils.PagesPathEnum.SEARCH_PAGE;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.IMAGES_FROM_SEARCH;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.NAME_CATEGORY;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.ONE_PRODUCT;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.ONE_PRODUCT_IMAGES;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.PRODUCTS;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.PRODUCTS_FROM_SEARCH;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.PRODUCTS_IMAGES;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.QUANTITY_PRODUCTS_PAGES;
import static by.teachmeskills.eshop.utils.RequestParamsEnum.SEARCH_REQUEST;

@Service
@Log4j
public class ProductServiceImpl implements ProductService {
    private ProductRepositoryImpl productRepository;
    private ImageRepositoryImpl imageRepository;
    private CategoryRepositoryImpl categoryRepository;

    public ProductServiceImpl(ProductRepositoryImpl productRepository, ImageRepositoryImpl imageRepository, CategoryRepositoryImpl categoryRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product create(Product entity) {
        return productRepository.create(entity);
    }

    @Override
    public List<Product> read() {
        return productRepository.read();
    }

    @Override
    public Product update(Product entity) {
        return productRepository.update(entity);
    }

    @Override
    public void delete(int id) {
        productRepository.delete(id);
    }

    @Override
    public ModelAndView showSearchProductPage(User user) {
        if (Optional.ofNullable(user.getLogin()).isPresent()
                && Optional.ofNullable(user.getPassword()).isPresent()
                && Optional.ofNullable(user.getEmail()).isPresent()) {
            return new ModelAndView(SEARCH_PAGE.getPath());
        } else {
            return new ModelAndView(REDIRECT_TO_LOGIN_PAGE);
        }
    }

    @Override
    public ModelAndView getProductData(User user, int id) {
        if (Optional.ofNullable(user.getLogin()).isPresent()
                && Optional.ofNullable(user.getPassword()).isPresent()
                && Optional.ofNullable(user.getEmail()).isPresent()) {
            ModelMap modelMap = new ModelMap();
            Product product = productRepository.getProductById(id);
            List<Image> productImages = imageRepository.getImagesByProductId(id);
            modelMap.addAttribute(ONE_PRODUCT.getValue(), product);
            modelMap.addAttribute(ONE_PRODUCT_IMAGES.getValue(), productImages);
            return new ModelAndView(PRODUCT_PAGE.getPath(), modelMap);
        } else {
            return new ModelAndView(EshopConstants.REDIRECT_TO_LOGIN_PAGE);
        }
    }

    @Override
    public ModelAndView getCategoryProductsData(int id, String nameCategory) {
        ModelMap modelMap = new ModelMap();
        List<Product> categoryProducts = productRepository.getProductsByCategoryId(id);
        List<Image> productsImages = imageRepository.getPrimaryImagesByCategoryId(id);
        modelMap.addAttribute(PRODUCTS.getValue(), categoryProducts);
        modelMap.addAttribute(PRODUCTS_IMAGES.getValue(), productsImages);
        modelMap.addAttribute(NAME_CATEGORY.getValue(), nameCategory);
        modelMap.addAttribute(CATEGORY_ID, id);
        return new ModelAndView(CATEGORY_PAGE.getPath(), modelMap);
    }

    @Override
    public ModelAndView findAllProductsByRequest(String request, int page) {
        ModelMap modelMap = new ModelMap();
        List<Product> products = productRepository.getProductsForOnePage(request, page);
        List<Integer> quantityProductsPages = productRepository.findAllProductsQuantityByRequest(request);
        List<Image> images = imageRepository.getPrimaryImagesByListProducts(products);
        modelMap.addAttribute(IMAGES_FROM_SEARCH.getValue(), images);
        modelMap.addAttribute(PRODUCTS_FROM_SEARCH.getValue(), products);
        modelMap.addAttribute(QUANTITY_PRODUCTS_PAGES.getValue(), quantityProductsPages);
        modelMap.addAttribute(SEARCH_REQUEST.getValue(), request);
        return new ModelAndView(SEARCH_PAGE.getPath(), modelMap);
    }

    @Override
    public void writeProductsCategoryToCsv(int categoryId, Writer writer) {
        try {
            List<Product> products = productRepository.getProductsByCategoryId(categoryId);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();
            beanToCsv.write(products);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void saveProductsCategoryFromCsv(int id, MultipartFile file) {
        Category category = categoryRepository.findCategoryById(id);
        List<Product> csvProducts = parseCsv(file);
        for (Product csvProduct : csvProducts) {
            csvProduct.setCategory(category);
        }
        if (Optional.of(csvProducts).isPresent() && csvProducts.size() > 0) {
            csvProducts.stream().forEach(productRepository::create);
        }
    }

    private List<Product> parseCsv(MultipartFile file) {
        if (Optional.ofNullable(file).isPresent()) {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<Product> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Product.class)
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
