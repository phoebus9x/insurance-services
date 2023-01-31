package dbWork;

public abstract class AbstractFactory {
    public abstract DbUser getUser();
    public abstract DbAuthorization getAuthorization();
    public abstract DbAdmin getAdmin();
    public abstract DbAgent getAgent();
    public abstract DbInsurance getInsurance();
    public abstract DbContract getContract();
}
