package by.teachmeskills.eshop.repositories;

import by.teachmeskills.eshop.entities.Category;

public interface CategoryRepository {
    Category findCategoryById(int id);
}
