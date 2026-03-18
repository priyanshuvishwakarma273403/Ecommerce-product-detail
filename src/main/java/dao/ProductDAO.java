package dao;

import model.Product;
import util.DBUtil;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (name, description, category, price, quantity, active) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setString(3, product.getCategory());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getQuantity());
            pstmt.setBoolean(6, product.isActive());

            int rows = pstmt.executeUpdate();

            if(rows > 0) {
                try(ResultSet rs = pstmt.getGeneratedKeys()){
                    if(rs.next()){
                        product.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        catch(Exception e)
        {
            throw  new RuntimeException("Failed to add product",e);
        }
        return false;
    }

    // READ ALL
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY id DESC";

        try(Connection con = DBUtil.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e){
            System.out.println("Failed to Read All Products");
        }
        return products;
    }

    // READ ONE
    public Optional<Product> getProductById(int id) {
    String sql = "SELECT * FROM products WHERE id = ?";

    try(Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setInt(1, id);
        try(ResultSet rs = pstmt.executeQuery()){
            if(rs.next()){
                return Optional.of(mapResultSetToProduct(rs));
            }
        }
    } catch (SQLException e){
        System.out.println("Failed to Read Product");
    }
    return Optional.empty();
    }

    // SEARCH
    public List<Product> searchProducts(String keyword){
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ? OR category LIKE ?";
        try(Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);

            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e){
            System.out.println("Failed to Search Product");
        }
        return products;
    }

    // UPDATE
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name=?, description=?, category=?, " + "price=?, quantity=?, active=? WHERE id=?";
        try(Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setString(3, product.getCategory());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getQuantity());
            pstmt.setBoolean(6, product.isActive());
            pstmt.setInt(7, product.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e){
            System.out.println("Failed to Update Product");
        }
        return false;
    }

    // DELETE
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try(Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e){
            System.out.println("Failed to Delete Product");
        }
        return false;
    }

    // PAGINATION
    public List<Product> getProducts(int page, int pageSize) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY id DESC LIMIT ? OFFSET ?";
        try(Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page- 1) * pageSize);

            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e){
            System.out.println("Failed to Pagination");
        }
        return products;
    }

    public int getTotalCount(){
        String sql = "SELECT COUNT(*) FROM products";
        try(Connection con = DBUtil.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next()) return rs.getInt(1);
        } catch (SQLException e){
            System.out.println("Failed to Get Total Count");
        }
        return 0;
    }

    // Helper: Map ResultSet to Product
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setCategory(rs.getString("category"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setActive(rs.getBoolean("active"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) product.setCreatedAt(ts.toLocalDateTime());
        return product;
    }

}
