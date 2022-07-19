package by.teachmeskills.eshop.repositories;

import by.teachmeskills.eshop.entities.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> getOrdersByUserId(int userId);
}
