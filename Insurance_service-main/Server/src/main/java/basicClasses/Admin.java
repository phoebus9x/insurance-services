package basicClasses;

import java.io.Serializable;
import java.time.LocalDate;

public class Admin extends UserBase implements Serializable {
    public Admin() {}

    public Admin(String surname, String name, LocalDate birthday, String phone, String address) {
        super(surname, name, birthday, phone, address);
    }

    public Admin(int idUser, String surname, String name, LocalDate birthday, String phone, String address) {
        super(idUser, surname, name, birthday, phone, address);
    }
}
