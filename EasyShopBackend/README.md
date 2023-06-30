# EasyShop

The EasyShop Application aims to create an online store for a company named EasyShop. The project utilizes a 
Spring Boot API for the backend server and a MySQL database for data storage. This project aims to fix existing bugs 
and add new features for a new version of the EasyShop website.

## Table of Contents
- [Website View](#website_view)
- [Changes Made](#changes_made)
- [Testing](#testing)
- [Project Diagrams](#project-diagrams)



## [Website View](http://localhost:63342/capstone-client-web-application/capstone-client-web-application/index.html?_ijt=2um4u76jndl1b919acbskd98iu&_ij_reload=RELOAD_ON_SAVE)
### Preview Image
![Preview Image of EasyShop website](/README_Images/web_view.png)

## Changes Made & Interesting Pieces of Code
#### CategoriesController

    - Added annotations for REST controller and request mapping: These annotations (@RestController and @RequestMapping) define 
    that the CategoriesController class is a RESTful controller that handles requests at the specified URL (/categories).

    - Added annotation for cross-site origin requests: @CrossOrigin("*") enables cross-origin requests, allowing clients 
      from different domains to access this API.

    - Added @Autowired annotation for dependency injection: The CategoryDao and ProductDao are injected into the 
      CategoriesController using the @Autowired annotation.

    - Added annotations for HTTP methods: @GetMapping, @PostMapping, @PutMapping, and @DeleteMapping are used to specify 
      the HTTP methods (GET, POST, PUT, DELETE) for different controller methods.

    - Added @PreAuthorize annotation for role-based access control: The @PreAuthorize annotations ensure that only users 
      with the role of 'ROLE_ADMIN' can access certain endpoints (e.g., addCategory, updateCategory, deleteCategory).

    - Updated method implementations: The method implementations for getAll, getCategoryById, getProductsByCategoryId, 
      addCategory, updateCategory, and deleteCategory have been updated to perform their respective operations.

    - Introduced response entity: In the getCategoryById method, a ResponseEntity is returned, allowing for more 
      fine-grained control over the HTTP response, including setting status codes and headers.

    - Exception handling: In the new version, exception handling using ResponseStatusException has been added to 
      provide meaningful error responses for certain scenarios (e.g., when a category is not found).

    The purpose of these changes is to improve the functionality, security, and maintainability of the API by adding proper 
    annotations, role-based access control, and exception handling. Additionally, the new version provides more detailed 
    responses to clients and is better structured to handle the API requests effectively.


#### MySqlCategoryDao

    - Added SQL Queries: The new version contains complete SQL queries for the CRUD (Create, Read, Update, Delete) operations on 
    the categories table. These queries are parameterized and use PreparedStatement to avoid SQL injection vulnerabilities.

    - Implemented Methods: In the new version, the methods getAllCategories, getById, create, update, and delete are 
    implemented with actual logic to interact with the database.

    - Exception Handling: The new version includes proper exception handling using try-catch blocks, and for certain scenarios, 
    it throws a ResponseStatusException with appropriate HTTP status codes and error messages. This is to provide meaningful 
    error responses in case of database access failures or when a category is not found.

    - MapRow Method: The mapRow method is used to convert a ResultSet row into a Category object. This method is used when 
    fetching category records from the database.

    - Statement.RETURN_GENERATED_KEYS: In the create method, the Statement.RETURN_GENERATED_KEYS flag is used to retrieve the 
    auto-generated category ID after inserting a new category.

    - DataSource Constructor Injection: The MySqlCategoryDao class now accepts a DataSource object as a constructor parameter. 
    This allows for the connection to the MySQL database.

    - Autowired Annotation: The MySqlCategoryDao class is annotated with @Component, which makes it a Spring-managed bean and eligible
    for automatic dependency injection.

    The purpose of these changes is to implement the actual data access operations for categories in the MySQL database, improve code 
    maintainability, and handle database-related errors more gracefully. The new version makes the MySqlCategoryDao class functional,
    allowing it to perform CRUD operations on the categories table in the database.

#### ProductsController

    - PUT Request Handling: In the new version, the updateProduct method is fixed to handle the PUT request correctly. Previously, 
    it mistakenly used the create method instead of the update method, which would have created a new product instead of 
    updating an existing one.

    - Exception Handling: Both versions have exception handling to catch any unexpected exceptions that may occur during the data 
    access operations. In case of an exception, a ResponseStatusException with an appropriate HTTP status code and error message is 
    thrown. This helps in providing meaningful error responses to the client.

    - Security Annotation: The @PreAuthorize annotation is used to apply security checks on the controller methods. 
    In the new version, the addProduct, updateProduct, and deleteProduct methods are accessible only to users with the 
    'ROLE_ADMIN' authority, while the search and getById methods are accessible to all users, authenticated or not.

    - Autowired Annotation: Both versions use the @Autowired annotation for constructor injection of the ProductDao dependency, 
    allowing the controller to interact with the underlying data layer.

    - Cross-Origin (CORS) Configuration: The @CrossOrigin annotation is used to allow cross-origin requests to the controller, 
    enabling web clients from different domains to access the controller's endpoints.

    - Request Parameters: The controller's methods use @RequestParam annotations to specify optional query parameters 
    (categoryId, minPrice, maxPrice, color) that can be used to filter the search results for products.

    The purpose of these changes is to ensure proper handling of requests and responses, secure access to certain endpoints, 
    and improve the robustness of the controller's methods by handling potential exceptions gracefully. 
    Additionally, the new version correctly implements the PUT request for updating products, ensuring that it 
    uses the update method from the ProductDao.

#### MySqlProductsDao

    - Added an exception handling to the search method, listByCategoryId method, getById method, create method, update method, 
    and delete method now catch SQLException and throw a ResponseStatusException with an appropriate error message and status code.

    - Updated the getById method: Replaced the previous SQL query string with a multi-line string using Java's text block feature 
    for improved readability. Added a check for the existence of the product in the result set and throw a ResponseStatusException 
    with a "Product not found" message if it doesn't exist.

    - Updated the create method: Added a check for the number of rows affected by the INSERT statement. Retrieved the generated keys 
    from the statement and obtained the auto-incremented ID. Called the getById method to retrieve the newly inserted product using 
    the auto-incremented ID.

    - Updated the update method: Removed the check for the number of rows affected by the UPDATE statement. The method now executes 
    the update statement directly without any additional handling.

    - Updated the delete method: Removed the check for the number of rows affected by the DELETE statement. The method now executes 
    the delete statement directly without any additional handling. These changes mainly involve the addition of exception 
    handling using ResponseStatusException, improved SQL query readability.


#### Interesting Piece of Code: AuthenticationController
![Authentication Controller's Register Method Change](/README_Images/Auth_Ctrllr_EH_1.png)


## Testing
### Postman Tests
![Preview Image of Postman results](/README_Images/postman_results.png)

## Project Diagrams
#### UML Diagram
![Preview Image of EasyShop website](/EasyShopUML.png)
#### SQL Diagram
![Preview Image of EasyShop website](/EasyShopSQLDiagram.png)