package basicClasses;

import java.io.Serializable;
import java.time.LocalDate;

public class Agent extends UserBase implements Serializable {
    private LocalDate employmentDate;

    public Agent() {}

    public Agent(String surname, String name, LocalDate birthday, String phone, String address, LocalDate employmentDate) {
        super(surname, name, birthday, phone, address);
        this.employmentDate = employmentDate;
    }

    public Agent(int idUser, String surname, String name, LocalDate birthday, String phone, String address, LocalDate employmentDate) {
        super(idUser, surname, name, birthday, phone, address);
        this.employmentDate = employmentDate;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }
}
