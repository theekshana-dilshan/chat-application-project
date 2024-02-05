package org.example.model;

import org.example.db.DBConnection;
import org.example.dto.UserDTO;

import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;

public class UserModel {

    public static boolean saveUser(UserDTO userDTO) throws SQLException {
        Connection connection= DBConnection.getInstance().getConnection();
        String sql="INSERT INTO user(userId, username,image) VALUES (?,?,?)";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, userDTO.getUserId());
        pstm.setString(2, userDTO.getUsername());
        pstm.setBytes(3, userDTO.getImage());

        return pstm.executeUpdate() > 0;
    }

    public static boolean searchUser(String username) throws SQLException {
        Connection connection= DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM user WHERE username=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, username);
        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            return true;
        }
        return false;
    }

    public static UserDTO getImage(String username) throws SQLException {
        Connection connection= DBConnection.getInstance().getConnection();
        String sql="SELECT * FROM user WHERE username=?";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, username);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(resultSet.getString(1));
            userDTO.setUsername(resultSet.getString(2));
            userDTO.setImage(resultSet.getBytes(3));
            return userDTO;
        }
        return null;
    }
}
