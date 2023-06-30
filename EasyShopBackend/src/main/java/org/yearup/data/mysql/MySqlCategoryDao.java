package org.yearup.data.mysql;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        // get all categories
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement("""
                SELECT *
                FROM categories
                """);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Category category = mapRow(resultSet);
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while retrieving categories.", e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        try (PreparedStatement statement = getConnection().prepareStatement("""
                SELECT *
                FROM categories
                WHERE category_id = ?
                """)) {
            statement.setInt(1, categoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
                }
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while retrieving category.", e);
        }
    }

    @Override
    public Category create(Category category) {
        // create a new category
        try (PreparedStatement statement = getConnection().prepareStatement("""
                INSERT INTO categories (name, description)
                VALUES (?,?)
                """, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int categoryID = generatedKeys.getInt(1);
                    category.setCategoryId(categoryID);
                }
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while creating category.", e);
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
        try (PreparedStatement statement = getConnection().prepareStatement("""
                UPDATE categories
                SET name = ?, description = ?
                WHERE category_id = ?
                """)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, category.getCategoryId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated <= 0) {
                // Handle the case where no rows were updated
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found for update.");
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while updating category.", e);
        }

    }

    @Override
    public void delete(int categoryId) {
        // delete category
        String sql = "DELETE FROM Categories WHERE Category_Id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred during category deletion.", e);
        }
    }


    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        return new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};
    }

}