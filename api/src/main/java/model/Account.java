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
public class Account implements Serializable {
    private int id;
    private String username;
    private double balance;

    @Override
    public String toString() {
        return "{id: " + id + ", " +
                "username: " + "\"" + username + "\"" + ", " +
                "balance: " + balance + "}";
    }
}
