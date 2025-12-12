import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class ReceiptTest {

    private Receipt receipt;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
    }

    @Test
    @DisplayName("Calcular total sumant línies (sense tancar)")
    void testAddLinesCalculatesTotalCorrectly() throws IsClosedException {
        // Escenari: Afegim productes amb preus i quantitats inventades
        receipt.addLine(new BigDecimal("10.00"), 2); // 20.00
        receipt.addLine(new BigDecimal("5.50"), 2);  // 11.00

        BigDecimal expected = new BigDecimal("31.00");
        assertEquals(expected, receipt.getTotal());
    }

    @Test
    @DisplayName("Aplicar impostos actualitza el total")
    void testAddTaxesUpdatesTotal() throws IsClosedException {
        // Rebut amb un subtotal de 100.00
        receipt.addLine(new BigDecimal("100.00"), 1);

        // 10% d'impostos
        receipt.addTaxes(new BigDecimal("0.10"));

        assertEquals(new BigDecimal("110.00"), receipt.getTotal());
    }

    @Test
    @DisplayName("Llançar excepció en afegir línia en un rebut tancat")
    void testAddLineThrowsExceptionAfterClosing() throws IsClosedException {
        receipt.addLine(new BigDecimal("10.00"), 1);
        receipt.addTaxes(new BigDecimal("0.10")); 

        assertThrows(IsClosedException.class, () -> {
            receipt.addLine(new BigDecimal("5.00"), 1);
        }, "No s'ha de permetre afegir línies a un rebut tancat");
    }

    @Test
    @DisplayName("Llançar excepció en afegir impostos per segona vegada")
    void testAddTaxesThrowsExceptionIfAlreadyClosed() throws IsClosedException {
        receipt.addTaxes(new BigDecimal("0.10"));

        assertThrows(IsClosedException.class, () -> {
            receipt.addTaxes(new BigDecimal("0.05"));
        }, "No s'ha de permetre aplicar impostos a un rebut ja tancat");
    }
    
    @Test
    @DisplayName("El total es pot consultar fins i tot amb el rebut tancat")
    void testGetTotalWorksAfterClosing() throws IsClosedException {
        receipt.addLine(new BigDecimal("10.00"), 1);
        receipt.addTaxes(new BigDecimal("0.10")); // Tanca el rebut
        
        assertDoesNotThrow(() -> {
            receipt.getTotal();
        });
        
        assertNotNull(receipt.getTotal());
    }
}
