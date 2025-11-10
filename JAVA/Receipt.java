package JAVA;

import java.io.File;
import java.nio.file.Path;

public class Receipt 
{
    private readonly Order order;

    public Receipt(Order order) 
    {
        this.order = order;
    }

    public void Save()
    {
        var timestamp = DateTime.Now.ToString("yyyyMMdd_HHmmss");
        var filename = Path.Combine("receipts", $"receipt_{timestamp}.txt");

        Directory.CreateDirectory("receipts");

        var content = new StringBuilder();
        content.AppendLine("---- Order Receipt ----");
        content.AppendLine($"Date: {DateTime.Now}");
        content.AppendLine(order.GetOrderDetails());
        content.AppendLine($"Total: ${order.CalculateTotal():0.00}");

        File.WriteAllText(filename, content.ToString());
    }
}