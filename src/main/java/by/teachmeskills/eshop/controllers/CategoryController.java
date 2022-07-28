package by.teachmeskills.eshop.controllers;

import by.teachmeskills.eshop.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.teachmeskills.eshop.utils.EshopConstants.CATEGORY_ID;
import static by.teachmeskills.eshop.utils.EshopConstants.CATEGORY_NAME;
import static by.teachmeskills.eshop.utils.EshopConstants.FILE;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final ProductService productService;

    public CategoryController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ModelAndView openCategoryProductPage(@RequestParam(CATEGORY_ID) int id, @RequestParam(CATEGORY_NAME) String nameCategory) {
        return productService.getCategoryProductsData(id, nameCategory);
    }

    @GetMapping("download")
    public void downloadCsvFile(HttpServletResponse response, @RequestParam(CATEGORY_ID) int id) throws IOException {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF8");
        response.addHeader("Content-Disposition", "attachment; filename=productsCategory.csv");
        productService.writeProductsCategoryToCsv(id, response.getWriter());
    }

    @PostMapping("/upload")
    public ModelAndView uploadCategoriesFromFile(@RequestParam(FILE) MultipartFile file,@RequestParam(CATEGORY_ID) int id, @RequestParam(CATEGORY_NAME) String nameCategory) {
        productService.saveProductsCategoryFromCsv(id,file);
        return new ModelAndView("redirect:/category?categoryId="+id+"&nameCategory="+nameCategory);
    }
}
