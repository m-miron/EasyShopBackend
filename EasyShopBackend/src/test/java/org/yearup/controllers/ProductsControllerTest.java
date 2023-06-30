package org.yearup.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductsControllerTest {
    @Test
    void updateExistingProduct() {
        // Prepare
        int productId = 1;
        Product updatedProduct = new Product(productId, "Updated Product", new BigDecimal("99.99"),
                1, "Updated description", "Updated color", 10, true, "updated-image.jpg");

        // Mock the dependencies
        ProductDao productDao = Mockito.mock(ProductDao.class);
        ProductsController controller = new ProductsController(productDao);

        // Set up the mock behavior
        Mockito.doNothing().when(productDao).update(Mockito.eq(productId), Mockito.eq(updatedProduct));

        // Execute and assert
        assertDoesNotThrow(() -> controller.updateProduct(productId, updatedProduct));
        Mockito.verify(productDao, Mockito.times(1)).update(Mockito.eq(productId), Mockito.eq(updatedProduct));
    }

    @Test
    void updateProductException() {
        // Prepare
        int productId = 1;
        Product updatedProduct = new Product(productId, "Updated Product", new BigDecimal("99.99"),
                1, "Updated description", "Updated color", 10, true, "updated-image.jpg");

        // Mock the dependencies
        ProductDao productDao = Mockito.mock(ProductDao.class);
        ProductsController controller = new ProductsController(productDao);

        // Set up the mock behavior
        Mockito.doThrow(new RuntimeException("Update failed"))
                .when(productDao)
                .update(Mockito.eq(productId), Mockito.eq(updatedProduct));

        // Execute and assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> controller.updateProduct(productId, updatedProduct));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals("Oops... our bad.", exception.getReason());
    }
}
