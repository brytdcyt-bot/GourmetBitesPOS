public class Side {
    private String name;
    private double price;

    public Side(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return String.format("%s Side - $%.2f", name, price);
    }

    public String getName() {
        return name;
    }
}