package dao;

import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class UserDAO {
    public Optional<User> authenticate(String username, String password){
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND active = true";

        try(Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In production: use BCrypt hashing!

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    return Optional.of(mapUser(rs));
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return Optional.empty();
    }


    public boolean registerUser(User user){
        String sql = "INSERT INTO users (username, password, email, full_name, role) "
                + "VALUES (?, ?, ?, ?, ?)";

        try(Connection con = DBUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword()); // Hash in production!
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getName());
            pstmt.setString(5, user.getRole());

            return pstmt.executeUpdate() > 0;
        }
        catch (SQLException e){
            if(e.getErrorCode() == 1062){
                return false;
            }
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id";
        try(Connection con = DBUtil.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                users.add(mapUser(rs));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return users;
    }

    private User mapUser(ResultSet rs) throws SQLException{
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        return user;
    }

}

