package sojka.maciej.projektPAI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sojka.maciej.projektPAI.entities.User;
import sojka.maciej.projektPAI.entities.UserRepo;
import sojka.maciej.projektPAI.services.UserServices;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserServices service;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(
            @Valid User user, BindingResult br, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        if (br.hasErrors()) return "signup_form";
        service.register(user, getSiteURL(request));
        return "success";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (service.verify(code)) return "verify_success";
        else return "verify_fail";
    }

    @GetMapping("/edit")
    public String editPage(Model m, Principal principal) {
        m.addAttribute("user", userRepo.findByEmail(principal.getName()));
        return "edit_form";
    }

    @PostMapping("/process_edit")
    public String editPOST(@ModelAttribute(value = "user") @Valid User user,
                           BindingResult binding, Principal principal) {
        if (binding.hasErrors()) return "edit_form";
        user.setEnabled(true);
        service.updateUser(userRepo.findByEmail(principal.getName()).getId(), user);
        return "success";
    }

    @GetMapping("/delete")
    public String delete(Model m, Principal principal) {
        userRepo.delete(userRepo.findByEmail(principal.getName()));
        return "redirect:/logout";
    }

}
