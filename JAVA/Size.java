package JAVA;

public enum Size {

    SMALL (1.0),
    MEDIUM (1.5),
    LARGE (2.0);

    private final double priceMultiplier;

    Size(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }   

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}