/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplicationfx.server;

import chatapplicationfx.Models.UsersMessages;
import chatapplicationfx.Models.Messages;
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
    
    public ArrayList<UsersMessages> getUsersMessages(int userId){
        ArrayList<UsersMessages> list = new ArrayList<>();
        String query = "SELECT f.umId, .u.userId, u.fname, u.mname, u.lname FROM tbl_usermessage AS f\n" +
                       "INNER JOIN tbl_users AS u ON u.userId = f.senreceiver WHERE f.userId = " + userId;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                UsersMessages user = new UsersMessages();
                user.umId = rs.getInt(1);
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
    
    public void saveMessage(int senderId, int receiverId, String message){
        String query = "insert into tbl_messages(receiverId, senderId, message) values ('"+ receiverId+"',"
                + "'"+ senderId +"', '"+ message +"')";
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            
            String query2 = "select count(userId) from tbl_usermessage where userId = " + senderId + " and senreceiver = " + receiverId; 
            stmt = con.createStatement();
            rs = stmt.executeQuery(query2);
            rs.first();
            if(rs.getInt(1) == 0){
                rs.close();
                
                String query3 = "insert into tbl_usermessage(userId, senreceiver) values ("+ senderId+","
                    + ""+ receiverId +")";
                
                stmt = con.createStatement();
                stmt.executeUpdate(query3);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ArrayList<Messages> getMessages(int senderId, int receiverId){
        ArrayList<Messages> list = new ArrayList<>();
        String query = "SELECT m.senderId, m.message, CONCAT(u.fname, ' ', SUBSTRING(u.mname, 0, 1), '. ', u.lname) AS fullname FROM tbl_messages AS m \n" +
                       "INNER JOIN tbl_users AS u ON u.userId = m.senderId WHERE m.senderId = "+senderId+" AND m.receiverId = "+receiverId+" "
                     + "OR m.senderId= "+receiverId+" AND m.receiverId = "+senderId;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            while(rs.next()){
                Messages mess = new Messages();
                mess.senderId = rs.getInt(1);
                mess.message = rs.getString(2);
                mess.fullname = rs.getString(3);
                list.add(mess);
            }
           return list;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
            
}
