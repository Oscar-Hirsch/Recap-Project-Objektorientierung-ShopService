import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepoTest {

    private ProductRepo repo;

    @BeforeEach
    void setUp() {
        repo = new ProductRepo();
        repo.addProduct(new Product("1", "Apfel"));
        repo.addProduct(new Product("2", "Birne"));
        repo.addProduct(new Product("3", "Banane"));
    }

    @Test
    void getProducts() {

        //WHEN
        List<Product> actual = repo.getProducts();

        //THEN
        List<Product> expected = new ArrayList<>();
        expected.add(new Product("1", "Apfel"));
        expected.add(new Product("2", "Birne"));
        expected.add(new Product("3", "Banane"));
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void getProductById() {
        //GIVEN
        ProductRepo repo = new ProductRepo();
        repo.addProduct(new Product("1", "Apfel"));

        //WHEN
        Product actual = repo.getProductById("1").orElse(null);

        //THEN
        Product expected = new Product("1", "Apfel");
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void addProduct() {
        //GIVEN
        ProductRepo repo = new ProductRepo();
        Product newProduct = new Product("9", "Banane");

        //WHEN
        Product actual = repo.addProduct(newProduct);

        //THEN
        Product expected = new Product("9", "Banane");
        assertEquals(actual, expected);
        assertEquals(repo.getProductById("9").orElse(null), expected);
    }

    @org.junit.jupiter.api.Test
    void removeProduct() {
        //GIVEN
        ProductRepo repo = new ProductRepo();

        //WHEN
        repo.removeProduct("1");

        //THEN
        assertNull(repo.getProductById("1").orElse(null));
    }
}
