import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Receipt {
    
    private ProductsDB productsDB;
    private ReceiptPrinter printer;
    private BigDecimal total;
    private BigDecimal taxesAmount;
    private boolean isClosed;
    private List<LineItem> lines;

    private static class LineItem {
        String productID;
        int numUnits;
        LineItem(String id, int units) { this.productID = id; this.numUnits = units; }
    }

    public Receipt(ProductsDB productsDB, ReceiptPrinter printer) {
        this.productsDB = productsDB;
        this.printer = printer;
        this.total = BigDecimal.ZERO;
        this.taxesAmount = BigDecimal.ZERO;
        this.isClosed = false;
        this.lines = new ArrayList<>();
    }

    public void addLine(String productID, int numUnits) 
            throws IsClosedException, DoesNotExistException {
        
        if (isClosed) throw new IsClosedException();

        // Verifiquem que el producte existeix i obtenim preu
        ProductDTO product = productsDB.getProduct(productID);

        // Calculem el subtotal de la línia per mantenir el total actualitzat
        BigDecimal lineTotal = product.getPrice().multiply(new BigDecimal(numUnits));
        this.total = this.total.add(lineTotal);
        
        this.lines.add(new LineItem(productID, numUnits));
    }

    public void addTaxes(BigDecimal percent) throws IsClosedException {
        if (isClosed) throw new IsClosedException();

        // Calculem quina part del total són impostos
        this.taxesAmount = this.total.multiply(percent);
        this.total = this.total.add(this.taxesAmount);
        
        this.isClosed = true;
    }
    
    public void printReceipt() throws DoesNotExistException, IsNotClosedException {
        if (!isClosed) {
            throw new IsNotClosedException("No es pot imprimir un rebut obert");
        }

        printer.init();

        for (LineItem line : lines) {
            ProductDTO dto = productsDB.getProduct(line.productID);
            
            printer.addProduct(dto.getDescription(), line.numUnits, dto.getPrice());
        }

        printer.addTaxes(this.taxesAmount);
        printer.print(this.total);
    }

    public BigDecimal getTotal() {
        return this.total;
    }
}
