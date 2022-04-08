package tasks;

import lombok.Data;

@Data
// создаём класс под JSON
public class PojoProduct {

    private Object username;
    private Object password;
    private Object name;
    private Object description;
    private Object price;
}
