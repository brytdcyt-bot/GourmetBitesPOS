public class SignatureItem : OrderItem
{
    private List<(Topping topping, int quantity)> toppings = new();
    private decimal basePrice;
    private string name;

    public SignatureItem(string name, Size size, decimal basePrice)
    {
        this.name = name;
        Size = size;
        this.basePrice = basePrice;
        InitializeDefaultToppings();
    }

    private void InitializeDefaultToppings()
    {
        // add default toppings here
    }

    public void AddTopping(Topping topping, int quantity = 1)
    {
        // modify toppings list
    }

    public void RemoveTopping(string toppingName)
    {
        // remove from toppings list
    }

    // Implement CalculatePrice and GetDescription similar to CustomItem
}