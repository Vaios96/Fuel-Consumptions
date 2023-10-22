package chicko.project.fuelconsumptions.dao;

import chicko.project.fuelconsumptions.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class FuelConsumptionDAOImpl implements IFuelConsumptionDAO {

    private EntityManager entityManager;

    @Autowired
    public FuelConsumptionDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Manufacturer

    @Override
    @Transactional
    public void createManufacturer(Manufacturer manufacturer) {
        entityManager.persist(manufacturer);
    }

    @Override
    public Manufacturer findManufacturer(int id) {
        return entityManager.find(Manufacturer.class, id);
    }

    @Override
    public List<Manufacturer> findAllManufacturers() {
        TypedQuery<Manufacturer> query = entityManager.createQuery("FROM Manufacturer", Manufacturer.class);

        List<Manufacturer> manufacturers = query.getResultList();

        return  manufacturers;
    }

    @Override
    @Transactional
    public void updateManufacturer(Manufacturer manufacturer) {
        entityManager.merge(manufacturer);
    }

    @Override
    @Transactional
    public void deleteManufacturerById(Integer id) {
        Manufacturer tmp = findManufacturer(id);

        entityManager.remove(tmp);
    }

    // Model


    @Override
    @Transactional
    public void createModel(Model model) {
        entityManager.persist(model);
    }

    @Override
    public Model findModelById(int id) {
        return entityManager.find(Model.class, id);
    }

    @Override
    public List<Model> findAllModels() {
        TypedQuery<Model> query = entityManager.createQuery("FROM Model", Model.class);

        return query.getResultList();
    }

    @Override
    public List<Model> findAllModelsByManufacturerId(int id) {
        TypedQuery<Model> query = entityManager.createQuery("FROM Model WHERE manufacturer.id = :data", Model.class);

        query.setParameter("data", id);

        List<Model> models = query.getResultList();

        return models;
    }

    @Override
    @Transactional
    public void updateModel(Model model) {
        entityManager.merge(model);
    }

    @Override
    @Transactional
    public void deleteModelById(Integer id) {
        Model tmp = findModelById(id);

        entityManager.remove(tmp);
    }

    // User


    @Override
    @Transactional
    public void createUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> findAllUsers() {
        TypedQuery query = entityManager.createQuery("FROM User", User.class);

        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        User tmp = findUserById(id);

        entityManager.remove(tmp);
    }

    // Role


    @Override
    @Transactional
    public void createRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public Role findRoleByUserIdAndRole(int id, String role) {
        TypedQuery<Role> query = entityManager.createQuery(
                "SELECT r FROM Role r WHERE r.user.id = :userId AND r.role = :roleName",
                Role.class
        );
        query.setParameter("userId", id);
        query.setParameter("roleName", role);

        Role theRole = query.getSingleResult();

        return theRole;
    }

    @Override
    public List<Role> findRolesByUserId(int id) {
        TypedQuery<Role> query = entityManager.createQuery("FROM Role WHERE user.id = :data", Role.class);
        query.setParameter(":data", id);

        List<Role> roles = query.getResultList();

        return roles;
    }

    @Override
    @Transactional
    public void deleteRoleByUserIdAndRole(Integer id, String role) {
        Role tmp = findRoleByUserIdAndRole(id, role);

        entityManager.remove(tmp);
    }

    // Fuel Consumption


    @Override
    @Transactional
    public void createFuelConsumption(FuelConsumption fuelConsumption) {
        entityManager.persist(fuelConsumption);
    }

    @Override
    public FuelConsumption findFuelConsumptionById(int id) {
        return entityManager.find(FuelConsumption.class, id);
    }

    @Override
    public List<FuelConsumption> findFuelConsumptionByModel(Model model) {
        TypedQuery<FuelConsumption> query = entityManager.createQuery("SELECT fc FROM FuelConsumption fc WHERE fc.model = :model", FuelConsumption.class);
        query.setParameter("templates/model", model);

        List<FuelConsumption> fcs = query.getResultList();
        return fcs;
    }

    @Override
    public List<FuelConsumption> findFuelConsumptionByManufacturer(Manufacturer manufacturer) {
        TypedQuery<FuelConsumption> query = entityManager.createQuery(
                "SELECT fc FROM FuelConsumption fc WHERE fc.model.manufacturer = :manufacturer",
                FuelConsumption.class
        );
        query.setParameter("manufacturer", manufacturer);

        List<FuelConsumption> fcs = query.getResultList();
        return fcs;
    }

    @Override
    public List<FuelConsumption> findFuelConsumptionByUser(User user) {
        TypedQuery<FuelConsumption> query = entityManager.createQuery(
                "SELECT fc FROM FuelConsumption fc WHERE fc.user = :user",
                FuelConsumption.class
        );
        query.setParameter("user", user);

        List<FuelConsumption> fcs = query.getResultList();
        return fcs;
    }

    @Override
    public List<FuelConsumption> findAllFuelConsumption() {
        TypedQuery<FuelConsumption> query = entityManager.createQuery("FROM FuelConsumption ", FuelConsumption.class);

        return query.getResultList();
    }

    @Override
    @Transactional
    public void updateFuelConsumption(FuelConsumption fuelConsumption) {
        entityManager.merge(fuelConsumption);
    }

    @Override
    @Transactional
    public void deleteFuelConsumptionById(Integer id) {
        FuelConsumption tmp = findFuelConsumptionById(id);

        entityManager.remove(tmp);
    }

    @Override
    public Integer findUserIdByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT user FROM User user WHERE user.username = :user",
                User.class
        );
        query.setParameter("user", username);

        User foundUser = query.getSingleResult();
        return foundUser.getId();
    }

    @Override
    public User findUserByUsername(String username) {
        User user = null;

        try {
            user = entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException ignored) {

        }

        return user;
    }
}
