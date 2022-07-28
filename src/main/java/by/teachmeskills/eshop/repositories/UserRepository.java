package by.teachmeskills.eshop.repositories;

import by.teachmeskills.eshop.entities.User;

public interface UserRepository {

    User getUserFromBaseByLoginAndPassword(User user);

    User getUserFromBaseByLogin(User user);

    User getUserById(int id);
}
