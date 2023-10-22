package chicko.project.fuelconsumptions.controller;

import chicko.project.fuelconsumptions.dao.IFuelConsumptionDAO;
import chicko.project.fuelconsumptions.entity.Role;
import chicko.project.fuelconsumptions.entity.User;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roles")
public class RoleController {

    private IFuelConsumptionDAO dao;

    public RoleController(IFuelConsumptionDAO dao) {
        this.dao = dao;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String listRoles(Model theModel) {

        List<User> usersAndRoles = dao.findAllUsers();

        theModel.addAttribute("users", usersAndRoles);

        return "role/list-roles";
    }

    @PostMapping("/addEmployee")
    public String addEmployeeRole(@RequestParam("userId") int id) {

        User user = dao.findUserById(id);
        Role employee = new Role("EMPLOYEE");
        employee.setUser(user);
        user.addRole(employee);

        dao.updateUser(user);

        return "redirect:/roles/list";
    }
}
