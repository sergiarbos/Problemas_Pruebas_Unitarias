# Problemas_Pruebas_Unitarias

## Problema 3.        Clase Receipt básica 
## Se dispone de la clase Receipt (recibo) que tiene los siguientes métodos: 
1 public class Receipt 
2   public void addLine(BigDecimal pricePerUnit,  
3      int numUnits)  
4     throws IsClosedException 
5   public void addTaxes(BigDecimal percent)  
6     throws IsClosedException 
7   public BigDecimal getTotal()... 
8 } 
## Las especificaciones son las siguientes: 
## • addLine añade una línea al recibo utilizando dos argumentos: numUnits, número de unidades y pricePerUnit, precio unitario. 
## • addTaxes añade al recibo los impuestos correspondientes a un tanto por ciento percent y cierra el recibo. 
## • Las operaciones addLine y addTaxes lanzan IsClosedException si se aplican sobre un recibo ya cerrado. 
## • getTotal devuelve el total del recibo y puede llamarse en cualquier momento. Definid una o más clases de prueba para la clase Receipt. 

# Problemas utilizando Dobles 
## Problema 5. Clase Receipt searchable 
## El Problema 3 presentaba una clase Receipt que era poco real ya que, normalmente la información sobre precios y descripción de los productos reside en una base de datos, a la que acceder mediante el identificador del producto. Por ello, la nueva versión, tendrá la forma: 
1 public class Receipt 
2   private ProductsDB = new ProductsDB(); 
3   public void addLine(String productID,  
4      int numUnits)  
5     throws IsClosedException, DoesNotExistException 
6   public void addTaxes(BigDecimal percent)  
7     throws IsClosedException 
8   public BigDecimal getTotal() 
9 } 
## Lo que queremos es poder probar esta versión de la clase Receipt de forma independiente de la base de datos (podéis referenciarla ProductDB). Modificad y/o añadid lo que haga falta para poder probar la clase Receipt de forma independiente y desarrollar un conjunto de tests que prueben su funcionalidad. La operación en la que estamos interesados de la base de datos es: public BigDecimal getPrice(String productID) throws DoesNotExistException; Ahora la nueva versión de addLine lanza también DoesNotExistException cuando se le pasa un identificador de producto que no existe en la BD. Definid pruebas unitarias para la clase Receipt definida aquí. 

# Problema 8. Clase Receipt printable  
## Suponed que a la clase Receipt del Problema 5 le añadimos un método para imprimir el recibo, printReceipt(), con la siguiente cabecera: 
 void printReceipt() throws DoesNotExistException,  
       IsNotClosedException 
       
## donde IsNotClosedException se lanza si se intenta aplicar sobre un recibo sin cerrar. 
 
## Para ello, la clase Receipt depende de la clase ReceiptPrinter, la cual  tiene cinco métodos: 
• public void init(); 
• public void addProduct(String description,  
                       int quantity, 
                       BigDecimal price); 
• public void addTaxes(BigDecimal taxes);
public void print(BigDecimal total); 
• public String getOutput(); 
## Veamos el comportamiento de las operaciones (están muy simplificadas respecto a un caso real, ya que no consideramos alineamientos, etc, etc): 
## • init añade la cabecera del recibo que, simplificando, consiste en la cadena “Acme S.A.” y un salto de línea. 
## • addProduct añade la línea de un producto. De cada producto se imprimen los tres campos pasados como argumento (descripción, cantidad y precio), transformados en Strings si es necesario, y separados por tabuladores. Por supuesto, se invocará tantas veces como líneas existentes en Receipt. 
## • addTaxes añade una línea con el texto “TAXES”, un tabulador y el valor de los impuestos. 
## • print añade una línea con el texto “----------“, y a continuación otra línea con el texto “TOTAL”, un tabulador y el valor total, impuestos incluidos. 
## • getOutput retorna la cadena de texto resultante. 
## Para hacer los tests, sustituiréis la clase ReceiptPrinter por una doble, de manera que el test pueda comprobar lo que se ha impreso. La descripción y el precio de un producto se obtendrá de la base de datos (ProductsDB), a tratar convenientemente. Supondremos que ProductsDB dispone del siguiente método: 
public ProductDTO getProduct(String productID) 
throws DoesNotExistException; 
## el cual devuelve una instancia de la clase ProductDTO siguiente: 
1 public class ProductDTO { 
2   private String productID; 
3   private String description; 
4   private BigDecimal price; 
5   
6   // getters and setters 
7 } 
## para obtener toda la información necesaria sobre un producto. 
## Definid pruebas unitarias para la clase Receipt definida aquí.
