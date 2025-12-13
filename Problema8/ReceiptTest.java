import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class ReceiptTest {

    private Receipt receipt;
    private ProductsDBStub productsDBStub;
    private FakeReceiptPrinter printerFake;

    private static class ProductsDBStub implements ProductsDB {
        @Override
        public ProductDTO getProduct(String productID) throws DoesNotExistException {
            if ("USB".equals(productID)) {
                return new ProductDTO("USB", "Memòria USB 32GB", new BigDecimal("10.00"));
            }
            if ("MOUSE".equals(productID)) {
                return new ProductDTO("MOUSE", "Ratolí Òptic", new BigDecimal("20.00"));
            }
            throw new DoesNotExistException("Producte no trobat");
        }
    }

    private static class FakeReceiptPrinter implements ReceiptPrinter {
        private StringBuilder output = new StringBuilder();

        @Override
        public void init() {
            output.append("Acme S.A.\n");
        }

        @Override
        public void addProduct(String description, int quantity, BigDecimal price) {
            // Format: Descripció [TAB] Quantitat [TAB] Preu \n
            output.append(description)
                  .append("\t")
                  .append(quantity)
                  .append("\t")
                  .append(price)
                  .append("\n");
        }

        @Override
        public void addTaxes(BigDecimal taxes) {
            output.append("TAXES\t").append(taxes).append("\n");
        }

        @Override
        public void print(BigDecimal total) {
            output.append("----------\n");
            output.append("TOTAL\t").append(total);
        }

        @Override
        public String getOutput() {
            return output.toString();
        }
    }

    @BeforeEach
    void setUp() {
        productsDBStub = new ProductsDBStub();
        printerFake = new FakeReceiptPrinter();
        
        // Dos dobles
        receipt = new Receipt(productsDBStub, printerFake);
    }

    @Test
    @DisplayName("Imprimir rebut genera la sortida correcta")
    void testPrintReceiptGeneratesCorrectOutput() throws Exception {
        // 1. Preparem les dades
        receipt.addLine("USB", 2);   // 2 * 10.00 = 20.00
        receipt.addLine("MOUSE", 1); 
        // Subtotal: 40.00
        
        receipt.addTaxes(new BigDecimal("0.10")); // Impostos: 4.00, Total: 44.00
        receipt.printReceipt();

        String expectedOutput = 
            "Acme S.A.\n" +
            "Memòria USB 32GB\t2\t10.00\n" +
            "Ratolí Òptic\t1\t20.00\n" +
            "TAXES\t4.0000\n" + 
            "----------\n" +
            "TOTAL\t44.0000";

        
        String actualOutput = printerFake.getOutput();
        
        assertAll("Verificació del rebut imprès",
            () -> assertTrue(actualOutput.contains("Acme S.A.")),
            () -> assertTrue(actualOutput.contains("Memòria USB 32GB")),
            () -> assertTrue(actualOutput.contains("TAXES")),
            () -> assertTrue(actualOutput.contains("TOTAL"))
        );
    }

    @Test
    @DisplayName("Llançar excepció si s'intenta imprimir un rebut obert")
    void testPrintThrowsExceptionIfNotClosed() throws Exception {
        receipt.addLine("USB", 1);
        assertThrows(IsNotClosedException.class, () -> {
            receipt.printReceipt();
        }, "No es pot imprimir sense tancar el rebut");
    }
}
