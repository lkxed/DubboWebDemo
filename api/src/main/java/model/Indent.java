package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author lkxed
 */
@Getter
@Setter
@AllArgsConstructor
public class Indent implements Serializable {
    private int id;
    private int accountId;
    private int merchandiseId;
    private int number;
    private double sales;
    private Timestamp createTime;

    @Override
    public String toString() {
        return "{id: " + id + ", " +
                "accountId: " + accountId + ", " +
                "merchandiseId: " + merchandiseId + ", " +
                "number: " + number + ", " +
                "sales: " + sales + ", " +
                "createTime: " + "\"" + createTime.toString() + "\"" + "}";
    }
}
