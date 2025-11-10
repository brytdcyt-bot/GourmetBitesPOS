package JAVA;

public enum ToppingType
{
    Regular,
    Premium
}

public class Topping
{
    public string Name { get; }
    public ToppingType Type { get; }
    public decimal Price { get; }  // extra cost if premium and extra portions requested

    public Topping(string name, ToppingType type, decimal price)
    {
        Name = name;
        Type = type;
        Price = price;
    }
}