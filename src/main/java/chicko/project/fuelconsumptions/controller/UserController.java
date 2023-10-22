package chicko.project.fuelconsumptions.controller;

import chicko.project.fuelconsumptions.dao.IFuelConsumptionDAO;
import chicko.project.fuelconsumptions.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private IFuelConsumptionDAO dao;

    @Autowired
    public UserController(IFuelConsumptionDAO dao) {
        this.dao = dao;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/change-password")
    public String userInformation(Model theModel) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = dao.findUserByUsername(username);

        theModel.addAttribute("user", user);

        return "user/information";
    }

    @PostMapping("/processUpdate")
    public String processUpdate(@ModelAttribute("user")User user, BindingResult bindingResult) {

        if (user.getPassword() == null || user.getPassword().length() < 3 || user.getPassword().length() > 16) {
            FieldError error = new FieldError("user", "password", "Password must be 3-16 characters long");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            return "user/information";
        }

        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setUsername(user.getUsername());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        updatedUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));


        dao.updateUser(updatedUser);

        return "redirect:/user/change-password";
    }
}
