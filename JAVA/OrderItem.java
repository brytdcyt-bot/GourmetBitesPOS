package JAVA;

public abstract class OrderItem {
    protected String type;
    protected Drink.Size size;

    public OrderItem(String type, Drink.Size size) {
        this.type = type;
        this.size = size;
    }

    public abstract double calculatePrice();
    public abstract String getDescription();

    public String getType() {
        return type;
    }

    public Drink.Size getSize() {
        return size;
    }
}