package chicko.project.fuelconsumptions.controller;

import chicko.project.fuelconsumptions.dao.IFuelConsumptionDAO;
import chicko.project.fuelconsumptions.entity.Manufacturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manufacturers")
public class ManufacturerController {

    private IFuelConsumptionDAO dao;

    @Autowired
    public ManufacturerController(IFuelConsumptionDAO dao) {
        this.dao = dao;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String listManufacturers(Model theModel) {

        List<Manufacturer> manufacturers = dao.findAllManufacturers();

        theModel.addAttribute("manufacturers", manufacturers);

        return "manufacturer/list-manufacturers";
    }

    @GetMapping("/add")
    public String addManufacturerForm(Model theModel) {

        Manufacturer theManufacturer = new Manufacturer();

        theModel.addAttribute("manufacturer", theManufacturer);

        return "manufacturer/manufacturer-form";
    }

    @PostMapping("/save")
    public String saveManufacturer(@ModelAttribute("manufacturer") Manufacturer theManufacturer, BindingResult bindingResult) {

        if (theManufacturer.getManufacturerName() == null || theManufacturer.getManufacturerName().length() > 45) {
            FieldError error = new FieldError("manufacturer", "manufacturerName", "Manufacturer's name must be between 1 and 45 characters long");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            return "manufacturer/update-manufacturer";
        }

        dao.createManufacturer(theManufacturer);

        return "redirect:/manufacturers/list";
    }

    @GetMapping("/update")
    public String updateManufacturer(@RequestParam("manufacturerId") int id, Model model) {

        Manufacturer manufacturer = dao.findManufacturer(id);

        model.addAttribute("manufacturer", manufacturer);

        return "manufacturer/update-manufacturer";
    }

    @PostMapping("/processUpdate")
    public String processUpdate(@ModelAttribute("manufacturer") Manufacturer manufacturer, BindingResult bindingResult) {

        if (manufacturer.getManufacturerName() == null || manufacturer.getManufacturerName().length() > 45) {
            FieldError error = new FieldError("manufacturer", "manufacturerName", "Manufacturer's name must be between 1 and 45 characters long");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            return "manufacturer/update-manufacturer";
        }

        Manufacturer updatedManufacturer = dao.findManufacturer(manufacturer.getId());
        updatedManufacturer.setManufacturerName(manufacturer.getManufacturerName());

        dao.updateManufacturer(updatedManufacturer);

        return "redirect:/manufacturers/list";
    }

    @GetMapping("/delete")
    public String deleteModel(@RequestParam("manufacturerId") int id) {
        dao.deleteManufacturerById(id);

        return "redirect:/manufacturers/list";
    }
}
