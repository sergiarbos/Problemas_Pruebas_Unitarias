public interface ProductsDB {
    ProductDTO getProduct(String productID) throws DoesNotExistException;
}
