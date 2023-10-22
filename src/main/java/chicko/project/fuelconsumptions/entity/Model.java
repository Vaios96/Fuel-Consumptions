package chicko.project.fuelconsumptions.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(name = "model_name")
    private String modelName;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    private List<FuelConsumption> fuelConsumptions;


    public Model() {}

    public Model(String modelName, Manufacturer manufacturer) {
        this.modelName = modelName;
        this.manufacturer = manufacturer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<FuelConsumption> getFuelConsumptions() {
        return fuelConsumptions;
    }

    public void setFuelConsumptions(List<FuelConsumption> fuelConsumptions) {
        this.fuelConsumptions = fuelConsumptions;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", model_name='" + modelName + '\'' +
                ", manufacturer=" + manufacturer +
                '}';
    }
}
