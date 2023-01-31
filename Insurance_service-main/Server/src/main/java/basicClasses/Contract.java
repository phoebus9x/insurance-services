package basicClasses;

import java.io.Serializable;
import java.time.LocalDate;

public class Contract implements Serializable {
    private int idContract;
    private int idService;
    private int idStatus;
    private float price;
    private LocalDate startDate;
    private LocalDate endDate;

    public Contract() {}

    public Contract(int idService, int idStatus, float price, LocalDate startDate, LocalDate endDate) {
        this.idService = idService;
        this.idStatus = idStatus;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Contract(int idContract, int idService, int idStatus, float price, LocalDate startDate, LocalDate endDate) {
        this.idContract = idContract;
        this.idService = idService;
        this.idStatus = idStatus;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getIdContract() {
        return idContract;
    }

    public void setIdContract(int idContract) {
        this.idContract = idContract;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
