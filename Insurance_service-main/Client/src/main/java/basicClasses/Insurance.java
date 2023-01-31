package basicClasses;

import java.io.Serializable;

public class Insurance implements Serializable {
    private int idInsurance;
    private String name;
    private float pricePerMonth;
    private float payout;

    public Insurance() {}

    public Insurance(String name, float pricePerMonth, float payout) {
        this.name = name;
        this.pricePerMonth = pricePerMonth;
        this.payout = payout;
    }

    public Insurance(int idInsurance, String name, float pricePerMonth, float payout) {
        this.idInsurance = idInsurance;
        this.name = name;
        this.pricePerMonth = pricePerMonth;
        this.payout = payout;
    }

    public int getIdInsurance() {
        return idInsurance;
    }

    public void setIdInsurance(int idInsurance) {
        this.idInsurance = idInsurance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(float pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public float getPayout() {
        return payout;
    }

    public void setPayout(float payout) {
        this.payout = payout;
    }
}
