package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;



// add annotation to allow cross site origin requests
@RestController// add the annotations to make this a REST controller
@RequestMapping("/categories")
// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
@CrossOrigin(origins = "http://localhost:8080")// Replace with the appropriate client URL
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // add the appropriate annotation for a get action
    @GetMapping
    public List<Category> getAll()
    {
        // find and return all categories
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action
    @GetMapping("/{id}")
    public Category getById(@PathVariable int id)
    {
        // get the category by id
        return categoryDao.getById(id);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return productDao.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    @PostMapping
    // add annotation to ensure that only an ADMIN can call this function
    @Secured("ADMIN")
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        return categoryDao.create(category);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    @PutMapping
    // add annotation to ensure that only an ADMIN can call this function
    @Secured("ADMIN")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id
        category.setCategoryId(id);
        categoryDao.update(id, category);
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    @DeleteMapping
    // add annotation to ensure that only an ADMIN can call this function
    @Secured("ADMIN")
    public void deleteCategory(@PathVariable int id)
    {
        // delete the category by id
        categoryDao.delete(id);
    }
}
