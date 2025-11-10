public enum Size
{
    Small = 1,
    Medium = 2,
    Large = 3
}

public class SizeExtensions
{
    public static decimal GetPriceMultiplier(this Size size)
    {
        return size switch
        {
            Size.Small => 1.0m,
            Size.Medium => 1.5m,
            Size.Large => 2.0m,
            _ => 1.0m
        };
    }
}
