package JAVA;

public class Topping {

    public static final String Topping = null;
    public static Topping SMALL;
    private final String name;
    private final Topping type;
    private final double price; 

    public Topping(String name, Topping type, double price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Topping getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
}
