package chicko.project.fuelconsumptions.controller;

import chicko.project.fuelconsumptions.dao.IFuelConsumptionDAO;
import chicko.project.fuelconsumptions.entity.FuelConsumption;
import chicko.project.fuelconsumptions.entity.Manufacturer;
import chicko.project.fuelconsumptions.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fuel-consumptions")
public class FuelConsumptionsController {

    private IFuelConsumptionDAO dao;

    @Autowired
    public FuelConsumptionsController(IFuelConsumptionDAO dao) {
        this.dao = dao;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String listConsumptions(Model model, Authentication authentication) {

        List<FuelConsumption> fuelConsumptions = dao.findAllFuelConsumption();
        model.addAttribute("fuelConsumptions", fuelConsumptions);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Integer id = dao.findUserIdByUsername(userDetails.getUsername());
        model.addAttribute("userId", id);

        return "consumption/list-consumptions";
    }

    @GetMapping("/add/choose-manufacturer")
    public String chooseManufacturerForFuelConsumption(Model theModel) {

        List<Manufacturer> manufacturers = dao.findAllManufacturers();
        Manufacturer chosenManufacturer = new Manufacturer();
        theModel.addAttribute("manufacturers", manufacturers);
        theModel.addAttribute("chosenManufacturer", chosenManufacturer);

        return "consumption/consumption-form-choose-manufacturer";
    }

    @GetMapping("/add/fuel-consumption-details")
    public String fuelConsumptionDetails(@RequestParam(name = "chosenManufacturer") Integer manufacturerId, Model theModel) {

        Manufacturer chosenManufacturer = dao.findManufacturer(manufacturerId);
        List<chicko.project.fuelconsumptions.entity.Model> models = dao.findAllModels();

        theModel.addAttribute("models", models);
        theModel.addAttribute("chosenManufacturer", chosenManufacturer);

        theModel.addAttribute("fuelConsumption", new FuelConsumption());

        return "consumption/consumption-form-consumption-details";
    }

    @PostMapping("/add/save")
    public String saveFuelConsumption(@Valid @ModelAttribute("fuelConsumption")FuelConsumption fuelConsumption, BindingResult bindingResult, Model theModel) {

        if (fuelConsumption.getConsumptionRate() <= 0 || fuelConsumption.getConsumptionRate() > 50) {
            FieldError error = new FieldError("fuelConsumption", "consumptionRate", "Consumption Rate can not be negative or higher than 50");
            bindingResult.addError(error);
        }

        if (fuelConsumption.getModelYear() < 1900 || fuelConsumption.getModelYear() > 2023) {
            FieldError error = new FieldError("fuelConsumption", "modelYear", "Model's year of manufacture can not be earlier than 1900 or higher than 2023");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            chicko.project.fuelconsumptions.entity.Model currentModel = dao.findModelById(fuelConsumption.getModel().getId());
            Manufacturer chosenManufacturer = dao.findManufacturer(currentModel.getManufacturer().getId());
            List<chicko.project.fuelconsumptions.entity.Model> models = dao.findAllModels();

            theModel.addAttribute("models", models);
            theModel.addAttribute("chosenManufacturer", chosenManufacturer);

            return "consumption/consumption-form-consumption-details";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = dao.findUserByUsername(username);

        chicko.project.fuelconsumptions.entity.Model model = new chicko.project.fuelconsumptions.entity.Model();
        model = dao.findModelById(fuelConsumption.getModel().getId());

        fuelConsumption.setUser(user);
        fuelConsumption.setModel(model);

        dao.createFuelConsumption(fuelConsumption);

        return "redirect:/fuel-consumptions/list";
    }

    @GetMapping("/delete")
    public String deleteModel(@RequestParam("consumptionId") int id) {

        dao.deleteFuelConsumptionById(id);

        return "redirect:/fuel-consumptions/list";
    }

    @GetMapping("/update")
    public String updateModel(@RequestParam("consumptionId") int id, Model theModel) {

        FuelConsumption fuelConsumption = dao.findFuelConsumptionById(id);
        List<chicko.project.fuelconsumptions.entity.Model> models = dao.findAllModels();

        theModel.addAttribute("fuelConsumption", fuelConsumption);
        theModel.addAttribute("models", models);

        return "consumption/update-consumption";
    }

    @PostMapping("/processUpdate")
    public String processUpdate(@Valid @ModelAttribute("consumptionId")FuelConsumption fuelConsumption, BindingResult bindingResult, Model theModel) {

        if (fuelConsumption.getConsumptionRate() <= 0 || fuelConsumption.getConsumptionRate() > 50) {
            FieldError error = new FieldError("fuelConsumption", "consumptionRate", "Consumption Rate can not be negative or higher than 50");
            theModel.addAttribute("consumptionRateError", error);
            bindingResult.addError(error);
        }

        if (fuelConsumption.getModelYear() < 1900 || fuelConsumption.getModelYear() > 2023) {
            FieldError error = new FieldError("fuelConsumption", "modelYear", "Model's year of manufacture can not be earlier than 1900 or higher than 2023");
            theModel.addAttribute("modelYearError", error);
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            FuelConsumption fuelConsumptionForRedirect = dao.findFuelConsumptionById(fuelConsumption.getId());
            List<chicko.project.fuelconsumptions.entity.Model> models = dao.findAllModels();

            theModel.addAttribute("models", models);
            theModel.addAttribute("fuelConsumption", fuelConsumptionForRedirect);

            return "consumption/update-consumption";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = dao.findUserByUsername(username);

        chicko.project.fuelconsumptions.entity.Model model = new chicko.project.fuelconsumptions.entity.Model();
        model = dao.findModelById(fuelConsumption.getModel().getId());

        fuelConsumption.setModel(model);
        fuelConsumption.setUser(user);

        dao.updateFuelConsumption(fuelConsumption);

        return "redirect:/fuel-consumptions/list";
    }


}
