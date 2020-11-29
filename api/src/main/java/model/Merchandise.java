package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lkxed
 */
@Getter
@Setter
@AllArgsConstructor
public class Merchandise implements Serializable {
    private int id;
    private String name;
    private double price;
    private int inventory;

    @Override
    public String toString() {
        return "{id: " + id + ", " +
                "name: " + "\"" + name + "\"" + ", " +
                "price: " + price + ", " +
                "inventory: " + inventory + "}";
    }
}
