package by.teachmeskills.eshop.controllers;

import by.teachmeskills.eshop.entities.User;
import by.teachmeskills.eshop.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static by.teachmeskills.eshop.utils.EshopConstants.CATEGORY_ID;
import static by.teachmeskills.eshop.utils.EshopConstants.ORDER_ID;
import static by.teachmeskills.eshop.utils.EshopConstants.USER;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getProfilePage(@SessionAttribute(USER) User user) {
        return userService.showProfile(user);
    }

    @GetMapping("/exit")
    public ModelAndView exit(@ModelAttribute(USER) User user,
                             SessionStatus sessionStatus, HttpSession httpSession) {
        sessionStatus.setComplete();
        httpSession.invalidate();
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("download")
    public void downloadCsvFile(HttpServletResponse response, @SessionAttribute(USER) User user) throws IOException {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF8");
        response.addHeader("Content-Disposition", "attachment; filename=profile.csv");
        userService.writeProfileToCsv(user, response.getWriter());
    }

    @GetMapping("downloadOrder")
    public void downloadCsvFile(HttpServletResponse response, @RequestParam(ORDER_ID)int orderId) throws IOException {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF8");
        response.addHeader("Content-Disposition", "attachment; filename=order"+orderId+".csv");
        userService.writeOrderToCsv(orderId, response.getWriter());
    }
}
