import java.math.BigDecimal;

public interface ProductsDB {
    BigDecimal getPrice(String productID) throws DoesNotExistException;
}
