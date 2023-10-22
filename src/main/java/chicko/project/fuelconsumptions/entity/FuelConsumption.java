package chicko.project.fuelconsumptions.entity;

import jakarta.persistence.*;

@Entity
public class FuelConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "consumption_rate")
    private float consumptionRate;

    @Column(name = "model_year")
    private int modelYear;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public FuelConsumption() {}

    public FuelConsumption(float consumption_rate, int model_year, Model model, User user) {
        this.consumptionRate = consumption_rate;
        this.modelYear = model_year;
        this.model = model;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getConsumptionRate() {
        return consumptionRate;
    }

    public void setConsumptionRate(float consumption_rate) {
        this.consumptionRate = consumption_rate;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int model_year) {
        this.modelYear = model_year;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Fuel_Consumption{" +
                "id=" + id +
                ", consumption_rate=" + consumptionRate +
                ", model_year=" + modelYear +
                ", model=" + model +
                ", user=" + user +
                '}';
    }
}
