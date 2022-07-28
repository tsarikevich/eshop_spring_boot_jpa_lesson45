package by.teachmeskills.eshop.services;

import by.teachmeskills.eshop.entities.User;
import by.teachmeskills.eshop.exceptions.AuthorizationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.io.Writer;

public interface UserService extends BaseService<User> {
    ModelAndView authenticate(User user, BindingResult bindingResult) throws AuthorizationException;

    ModelAndView registration(User user, int year, int month, int day);

    ModelAndView showProfile(User user);

    void writeProfileToCsv(User user, Writer writer);

    void writeOrderToCsv(int orderId, Writer writer);
}
