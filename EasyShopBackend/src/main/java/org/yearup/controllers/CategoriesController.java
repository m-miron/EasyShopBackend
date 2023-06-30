package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;


// add annotation to allow cross site origin requests
@RestController// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
@RequestMapping("/categories")
@CrossOrigin("*")// Replace with the appropriate client URL
public class CategoriesController {
    private CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired // Create an Autowired controller to inject the categoryDao and ProductDao
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }


    @GetMapping // add the appropriate annotation for a get action
    @PreAuthorize("permitAll()")
    public List<Category> getAll() {
        // find and return all categories
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        Category category = categoryDao.getById(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
        }
        return ResponseEntity.ok(category);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getProductsByCategoryId(@PathVariable int categoryId) {
        return productDao.listByCategoryId(categoryId); // get a list of product by categoryId
    }

    @PostMapping // add annotation to call this method for a POST action
    @PreAuthorize("hasRole('ROLE_ADMIN')")// add annotation to ensure that only an ADMIN can call this function
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category) {
        // insert the category
        try {
            return categoryDao.create(category);
        } catch (Exception ex) {
            throw new ResponseStatusException((HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }


    @PutMapping("/{id}") // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    @PreAuthorize("hasRole('ROLE_ADMIN')")// add annotation to ensure that only an ADMIN can call this function
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        // update the category by id
        Category existingCategory = categoryDao.getById(id);
        if (existingCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
        }
        category.setCategoryId(id);
        categoryDao.update(id, category);
    }


    @DeleteMapping("/{id}") // add annotation to call this method for a DELETE action - the url path must include the categoryId
    @PreAuthorize("hasRole('ROLE_ADMIN')")// add annotation to ensure that only an ADMIN can call this function
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id) {
        // delete the category by id
        try {
            Category existingCategory = categoryDao.getById(id);
            if (existingCategory == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            categoryDao.delete(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}