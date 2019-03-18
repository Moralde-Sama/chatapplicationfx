/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.server;

import chatapplicationfx.Models.Friends;
import chatapplicationfx.Models.UserDetails;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Moralde-Sama
 */
public class Database {
    private Connection con;
    private Statement stmt;
    private ResultSet rs;

    public Database() {
        try {
             con = DriverManager.getConnection("jdbc:mysql://localhost/chatapp?user=root&password=");
        } catch (SQLException ex) {
           JOptionPane.showConfirmDialog(null, "Please START the mysql in xampp", "Sql Error!", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public UserDetails signIn(String username, String Password){
        String query = "select * from tbl_users where username = '" + username + "' and password = '" + Password + "'";
        UserDetails user = new UserDetails();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            rs.first();
            user.userId = rs.getInt(1);
            user.fname = rs.getString(2);
            user.mname = rs.getString(3);
            user.lname = rs.getString(4);
            return user;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public void signUp(UserDetails user) {
        String query = "insert into tbl_users(fname, mname, lname, username, password) values ('"+ user.fname +"',"
                + "'"+ user.mname +"', '"+ user.lname +"', '"+ user.username +"', '"+ user.password +"')";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<UserDetails> searchUser(String key){
        ArrayList<UserDetails> list = new ArrayList<>();
        String query = "SELECT * FROM tbl_users WHERE fname LIKE '%"+key+"%' OR mname LIKE '%"+key+"%' OR lname LIKE '%"+key+"'";
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                UserDetails user = new UserDetails();
                user.userId = rs.getInt(1);
                user.fname = rs.getString(2);
                user.mname = rs.getString(3);
                user.lname = rs.getString(4);
                list.add(user);
            }
           return list;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public ArrayList<Friends> getFriends(int userId){
        ArrayList<Friends> list = new ArrayList<>();
        String query = "SELECT f.friendId, u.userId, u.fname, u.mname, u.lname FROM tbl_friends AS f \n" +
                       "INNER JOIN tbl_users AS u ON u.userId = f.userId WHERE ownerId = " + userId;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                Friends user = new Friends();
                user.friendId = rs.getInt(1);
                user.userId = rs.getInt(2);
                user.fname = rs.getString(3);
                user.mname = rs.getString(4);
                user.lname = rs.getString(5);
                list.add(user);
            }
           return list;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
            
}
