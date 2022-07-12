package by.teachmeskills.eshop.utils;

import lombok.Getter;

@Getter
public enum RequestParamsEnum {
    IMAGES_FROM_SEARCH("foundImages"),
    PRODUCTS_FROM_SEARCH("foundProducts"),
    SEARCH_REQUEST("search_request"),
    CATEGORIES("categories"),
    CATEGORIES_IMAGES("categoriesImages"),
    PRODUCTS("products"),
    PRODUCTS_IMAGES("productsImages"),
    QUANTITY_PRODUCTS_PAGES("quantityProductsPages"),
    CART_PRODUCTS_IMAGES("cartProductsImages"),
    ONE_PRODUCT("oneProduct"),
    ONE_PRODUCT_IMAGES("oneProductImages"),
    ORDERS("orders"),
    ORDER_PRODUCTS_IMAGES("orderProductsImages"),
    TOTAL_COST("totalCost"),
    CART_PRODUCTS("cartProductsList"),
    NAME_CATEGORY("nameCategory"),
    CHECK_NEW_USER("isNewUserAdd"),
    USER_LOGIN("userLogin");

    private final String value;

    RequestParamsEnum(String value) {
        this.value = value;
    }
}
