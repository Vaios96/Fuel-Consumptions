package chicko.project.fuelconsumptions.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "manufacturer_name")
    private String manufacturerName;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL)
    List<Model> models;

    public Manufacturer() {}

    public Manufacturer(String manufacturer_name) {
        this.manufacturerName = manufacturer_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturer_name) {
        this.manufacturerName = manufacturer_name;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "id=" + id +
                ", manufacturer_name='" + manufacturerName + '\'' +
                '}';
    }
}
