public class Drink {
    public enum Size {
        SMALL(1.0), MEDIUM(1.5), LARGE(2.0);

        private final double priceMultiplier;

        Size(double priceMultiplier) {
            this.priceMultiplier = priceMultiplier;
        }

        public double getPriceMultiplier() {
            return priceMultiplier;
        }
    }

    private String flavor;
    private Size size;
    private double basePrice;

    public Drink(String flavor, Size size, double basePrice) {
        this.flavor = flavor;
        this.size = size;
        this.basePrice = basePrice;
    }

    public double calculatePrice() {
        return basePrice * size.getPriceMultiplier();
    }

    public String getDescription() {
        return String.format("%s Drink (%s) - $%.2f", size.name(), flavor, calculatePrice());
    }

    public String getFlavor() {
        return flavor;
    }

    public Size getSize() {
        return size;
    }
}