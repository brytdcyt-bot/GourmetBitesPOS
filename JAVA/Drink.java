package JAVA;

public class Drink {

    // ✅ Nested enum representing drink sizes and their price multipliers
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

    // === Fields ===
    private final String flavor;
    private final Size size;
    private final double basePrice;

    // === Constructor ===
    public Drink(String flavor, Size size, double basePrice) {
        this.flavor = flavor;
        this.size = size;
        this.basePrice = basePrice;
    }

    // === Core behavior ===
    public double calculatePrice() {
        return basePrice * size.getPriceMultiplier();
    }

    public String getDescription() {
        return String.format("%s %s Drink - $%.2f",
                size.name().charAt(0) + size.name().substring(1).toLowerCase(),
                flavor,
                calculatePrice());
    }

    // === Getters ===
    public String getFlavor() {
        return flavor;
    }

    public Size getSize() {
        return size;
    }

    public double getBasePrice() {
        return basePrice;
    }
}