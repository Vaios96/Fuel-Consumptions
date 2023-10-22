package chicko.project.fuelconsumptions.dao;

import chicko.project.fuelconsumptions.entity.*;

import java.util.List;

public interface IFuelConsumptionDAO {

    // Manufacturer

    void createManufacturer(Manufacturer manufacturer);

    Manufacturer findManufacturer(int id);

    List<Manufacturer> findAllManufacturers();

    void updateManufacturer(Manufacturer manufacturer);

    void deleteManufacturerById(Integer id);


    // Model

    void createModel(Model model);

    Model findModelById(int id);

    List<Model> findAllModels();

    List<Model> findAllModelsByManufacturerId(int id);

    void updateModel(Model model);

    void deleteModelById(Integer id);


    // User

    void createUser(User user);

    User findUserById(int id);

    List<User> findAllUsers();

    void updateUser(User user);

    void deleteUserById(Integer id);


    // Role

    void createRole(Role role);

    Role findRoleByUserIdAndRole(int id, String role);

    List<Role> findRolesByUserId(int id);

    void deleteRoleByUserIdAndRole(Integer id, String role);


    // Fuel Consumption

    void createFuelConsumption(FuelConsumption fuelConsumption);

    FuelConsumption findFuelConsumptionById(int id);

    List<FuelConsumption> findFuelConsumptionByModel(Model model);

    List<FuelConsumption> findFuelConsumptionByManufacturer(Manufacturer manufacturer);

    List<FuelConsumption> findFuelConsumptionByUser(User user);

    List<FuelConsumption> findAllFuelConsumption();

    void updateFuelConsumption(FuelConsumption fuelConsumption);

    void deleteFuelConsumptionById(Integer id);

    Integer findUserIdByUsername(String username);

    User findUserByUsername(String username);
}
