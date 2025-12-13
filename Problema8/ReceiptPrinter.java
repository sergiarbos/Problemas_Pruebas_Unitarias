import java.math.BigDecimal;

public interface ReceiptPrinter {
    void init();
    void addProduct(String description, int quantity, BigDecimal price);
    void addTaxes(BigDecimal taxes);
    void print(BigDecimal total);
    String getOutput();
}
