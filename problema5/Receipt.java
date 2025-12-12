import java.math.BigDecimal;

public class Receipt {
    
    private ProductsDB productsDB;
    private BigDecimal total;
    private boolean isClosed;

    public Receipt(ProductsDB productsDB) {
        this.productsDB = productsDB;
        this.total = BigDecimal.ZERO; // Inicialitzem a 0
        this.isClosed = false;
    }

    public void addLine(String productID, int numUnits) 
            throws IsClosedException, DoesNotExistException {
        
        if (isClosed) {
            throw new IsClosedException();
        }

        // Obtenim el preu a través de la interfície 
        BigDecimal price = productsDB.getPrice(productID);

        BigDecimal lineTotal = price.multiply(new BigDecimal(numUnits));

        this.total = this.total.add(lineTotal);
    }

    public void addTaxes(BigDecimal percent) throws IsClosedException {
        if (isClosed) {
            throw new IsClosedException();
        }

        // Calculem impostos
        BigDecimal taxAmount = this.total.multiply(percent);
        
        this.total = this.total.add(taxAmount);
        
        this.isClosed = true;
    }

    public BigDecimal getTotal() {
        return this.total;
    }
}
