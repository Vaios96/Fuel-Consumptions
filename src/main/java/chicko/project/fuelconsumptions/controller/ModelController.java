package chicko.project.fuelconsumptions.controller;

import chicko.project.fuelconsumptions.dao.IFuelConsumptionDAO;
import chicko.project.fuelconsumptions.entity.Manufacturer;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/models")
public class ModelController {

    private IFuelConsumptionDAO dao;

    public ModelController(IFuelConsumptionDAO dao) {
        this.dao = dao;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/list")
    public String listModels(Model theModel) {

        List<chicko.project.fuelconsumptions.entity.Model> models = dao.findAllModels();

        theModel.addAttribute("models", models);

        return "model/list-models";
    }

    @GetMapping("/add")
    public String addModelForm(Model theModel) {

        List<Manufacturer> manufacturers = dao.findAllManufacturers();

        chicko.project.fuelconsumptions.entity.Model carModel = new chicko.project.fuelconsumptions.entity.Model();

        theModel.addAttribute("manufacturers", manufacturers);
        theModel.addAttribute("model", carModel);

        return "model/model-form";
    }

    @PostMapping("/save")
    public String saveModel(@ModelAttribute("model")chicko.project.fuelconsumptions.entity.Model carModel, BindingResult bindingResult, Model model) {

        if (carModel.getModelName() == null || carModel.getModelName().length() > 45) {
            FieldError error = new FieldError("model", "modelName", "Model's name must be between 1 & 45 characters long");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            List<Manufacturer> manufacturers = dao.findAllManufacturers();
            model.addAttribute("manufacturers", manufacturers);
            return "model/model-form";
        }

        // Setting the Manufacturer of the model. Taking the id from the name, finding the manufacturer and setting it as the model's manufacturer
        Manufacturer tmp = carModel.getManufacturer();
        int tmpId = Integer.parseInt(tmp.getManufacturerName());
        tmp = dao.findManufacturer(tmpId);
        carModel.setManufacturer(tmp);

        dao.createModel(carModel);

        return "redirect:/models/list";
    }

    @GetMapping("/update")
    public String updateModel(@RequestParam("modelId") int id, Model theModel) {

        chicko.project.fuelconsumptions.entity.Model carModel = dao.findModelById(id);

        theModel.addAttribute("model", carModel);

        return "model/update-model";
    }

    @PostMapping("/processUpdate")
    public String processUpdate(@ModelAttribute("model")chicko.project.fuelconsumptions.entity.Model carModel, BindingResult bindingResult, Model theModel) {

        if (carModel.getModelName() == null || carModel.getModelName().length() > 45) {
            FieldError error = new FieldError("model", "modelName", "Model's name must be between 1 & 45 characters long");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            return "model/update-model";
        }

        chicko.project.fuelconsumptions.entity.Model updatedModel = dao.findModelById(carModel.getId());
        updatedModel.setModelName(carModel.getModelName());

        dao.updateModel(updatedModel);

        return "redirect:/models/list";
    }

    @GetMapping("/delete")
    public String deleteModel(@RequestParam("modelId") int id) {

        dao.deleteModelById(id);

        return "redirect:/models/list";
    }
}
