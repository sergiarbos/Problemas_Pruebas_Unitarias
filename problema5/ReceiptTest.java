import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

class ReceiptTest {

    private Receipt receipt;
    private ProductsDB productsDBStub;

    // Creem una classe interna que simula ser la Base de Dades.
    private static class ProductsDBStub implements ProductsDB {
        @Override
        public BigDecimal getPrice(String productID) throws DoesNotExistException {
            // Simulem un producte conegut
            if ("USB_DRIVE".equals(productID)) {
                return new BigDecimal("10.00");
            }
            if ("LAPTOP".equals(productID)) {
                return new BigDecimal("500.00");
            }
            // Per a qualsevol altre ID, simulem que no existeix
            throw new DoesNotExistException("Producte no trobat al Stub");
        }
    }

    @BeforeEach
    void setUp() {
        productsDBStub = new ProductsDBStub();
        receipt = new Receipt(productsDBStub);
    }

    @Test
    @DisplayName("Afegir línia calcula correctament el total usant preus de la BD")
    void testAddLineCalculatesTotalUsingDBPrice() throws Exception {
        // Escenari: 2 unitats de USB_DRIVE (Preu simulat: 10.00)
        receipt.addLine("USB_DRIVE", 2);

        // Esperat: 10.00 * 2 = 20.00
        assertEquals(new BigDecimal("20.00"), receipt.getTotal());
    }

    @Test
    @DisplayName("Llançar excepció si el producte no existeix a la BD")
    void testAddLineThrowsExceptionIfProductNotFound() {
        // Verifiquem que l'excepció de la BD es propaga correctament
        assertThrows(DoesNotExistException.class, () -> {
            receipt.addLine("ID_INEXISTENT", 1);
        }, "Ha de saltar l'excepció definida a la interfície ProductsDB");
    }

    @Test
    @DisplayName("Integració: Afegir línia i després impostos")
    void testAddLineAndTaxes() throws Exception {
        // 1. Afegim producte: 500.00 * 1 = 500.00
        receipt.addLine("LAPTOP", 1);

        // 2. Afegim impostos del 10% (0.10)
        receipt.addTaxes(new BigDecimal("0.10"));

        assertEquals(new BigDecimal("550.00"), receipt.getTotal());
    }
}
