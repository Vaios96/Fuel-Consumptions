package chicko.project.fuelconsumptions.controller;

import chicko.project.fuelconsumptions.dao.IFuelConsumptionDAO;
import chicko.project.fuelconsumptions.entity.Role;
import chicko.project.fuelconsumptions.entity.User;
import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginRegisterController {

    private IFuelConsumptionDAO dao;

    public LoginRegisterController(IFuelConsumptionDAO dao) {
        this.dao = dao;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/login")
    public String loginForm(Model theModel) {

        User tmpUser = new User();

        theModel.addAttribute(tmpUser);

        return "login-register/login";
    }

    @GetMapping("/register")
    public String registerForm(Model theModel) {

        User tmpUser = new User();

        theModel.addAttribute(tmpUser);

        return "login-register/register";
    }

    @PostMapping("/processRegistration")
    public String processRegistration(@Valid @ModelAttribute("user")User user, BindingResult bindingResult) {

        if (dao.findUserByUsername(user.getUsername()) != null) {
            FieldError error = new FieldError("user", "username", "Username already exists");
            bindingResult.addError(error);
        }

        if (user.getUsername() == null || user.getUsername().length() < 3 || user.getUsername().length() > 16) {
            FieldError error = new FieldError("user", "username", "Username must be 3-16 characters long");
            bindingResult.addError(error);
        }

        if (user.getPassword() == null || user.getPassword().length() < 3 || user.getPassword().length() > 16) {
            FieldError error = new FieldError("user", "password", "Password must be 3-16 characters long");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            return "login-register/register";
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Role tempRole = new Role("VISITOR");
        tempRole.setUser(user);
        user.addRole(tempRole);

        dao.createUser(user);

        return "redirect:/login";
    }
}
