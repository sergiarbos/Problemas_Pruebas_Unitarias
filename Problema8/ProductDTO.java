import java.math.BigDecimal;

public class ProductDTO {
    private String productID;
    private String description;
    private BigDecimal price;

    public ProductDTO(String productID, String description, BigDecimal price) {
        this.productID = productID;
        this.description = description;
        this.price = price;
    }

    public String getProductID() { return productID; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
}
