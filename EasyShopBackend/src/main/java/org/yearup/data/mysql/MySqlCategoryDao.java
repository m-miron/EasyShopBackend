package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        // get all categories
        List<Category> categories = new ArrayList<>();
        try(PreparedStatement statement = getConnection().prepareStatement("""
                    SELECT *
                    FROM categories
                    """);
            ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()){
                Category category = mapRow(resultSet);
                categories.add(category);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        Category category = null;
        try(PreparedStatement statement = getConnection().prepareStatement("""
                    SELECT *
                    FROM categories
                    WHERE category_id = ?
                    """);
            ResultSet resultSet = statement.executeQuery()) {
            statement.setInt(1, categoryId);
            if (resultSet.next()){
                category = mapRow(resultSet);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        try(PreparedStatement statement = getConnection().prepareStatement("""
                    INSERT INTO categories (name, description) 
                    VALUES (?,?)
                    """)) {
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
        } catch (SQLException e){
            e.printStackTrace();
        } return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        try (PreparedStatement statement = getConnection().prepareStatement("""
            UPDATE categories 
            SET name = ?, description = ? 
            WHERE category_id = ?
            """)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated <= 0) {
                // Handle the case where no rows were updated
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        try (PreparedStatement statement = getConnection().prepareStatement("""
            DELETE FROM categories
            WHERE category_id = ?
            """)) {
            statement.setInt(1, categoryId);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted <= 0) {
                // Handle the case where no rows were deleted
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
