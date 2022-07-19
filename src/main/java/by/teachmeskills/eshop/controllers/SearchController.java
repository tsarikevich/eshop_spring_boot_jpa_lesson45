package by.teachmeskills.eshop.controllers;

import by.teachmeskills.eshop.entities.User;
import by.teachmeskills.eshop.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static by.teachmeskills.eshop.utils.EshopConstants.REDIRECT_TO_SEARCH_PAGE;
import static by.teachmeskills.eshop.utils.EshopConstants.SEARCH;
import static by.teachmeskills.eshop.utils.EshopConstants.USER;

@RestController
@RequestMapping(value = "/search")
public class SearchController {
    private final ProductService productService;

    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ModelAndView getSearchPage(@SessionAttribute(USER) User user) {
        return productService.showSearchProductPage(user);
    }

    @PostMapping
    public ModelAndView getSearchPage(@RequestParam(SEARCH) String request) {
        if (request.isBlank()) {
            return new ModelAndView(REDIRECT_TO_SEARCH_PAGE);
        } else {
            String encode = URLEncoder.encode(request, StandardCharsets.UTF_8);
            String response = REDIRECT_TO_SEARCH_PAGE + "/" + encode + "/1";
            return new ModelAndView(response);
        }
    }

    @GetMapping("{request}/{page}")
    public ModelAndView getPaginationPage(@PathVariable String request, @PathVariable int page) {
        return productService.findAllProductsByRequest(request, page);
    }
}
