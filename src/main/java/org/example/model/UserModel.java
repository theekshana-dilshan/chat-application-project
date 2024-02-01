package org.example.model;

import org.example.db.DBConnection;
import org.example.dto.UserDTO;

import java.io.InputStream;
import java.sql.*;

public class UserModel {

    public static boolean saveUser(UserDTO userDTO) throws SQLException {
        Blob imgBlob = new javax.sql.rowset.serial.SerialBlob(userDTO.getImage());

        Connection connection= DBConnection.getInstance().getConnection();
        String sql="INSERT INTO user(userId, username,image) VALUES (?,?,?)";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, userDTO.getUserId());
        pstm.setString(2, userDTO.getUsername());
        pstm.setString(3, String.valueOf(imgBlob));

        return pstm.executeUpdate() > 0;
    }

    public static boolean searchClient(String username) throws SQLException {
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
}
