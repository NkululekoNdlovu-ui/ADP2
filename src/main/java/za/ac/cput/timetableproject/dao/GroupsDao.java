/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.timetableproject.dao;
import za.ac.cput.timetableproject.connection.DatabaseConnection;
import za.ac.cput.timetableproject.domain.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Asanda Ndhlela
 */
public class GroupsDao {

    private Connection con;

    //FOR CONNECTION 
    public GroupsDao() {
        try {
            this.con = DatabaseConnection.createConnection();
            createTable();
        } catch (Exception error) {
            System.out.println("Error " + error);
        }
    }
    
    //creating a method to create a db table 
    public void createTable(){
        
        String sql = "CREATE TABLE Groups(" +
                "group_id INT PRIMARY KEY, "+
                "group_name VARCHAR(20) NOT NULL)";
        
        try(Statement sqlStatement = this.con.createStatement()){
            
            sqlStatement.execute(sql);
            System.out.println("DBTable created!");
            
        }catch(SQLException error){
            if(error.getSQLState().equals("X0Y32")){
                System.out.println("BBTable already exist");
            }else{
                System.out.println("Error creating table " + error.getMessage());
            }
        }catch(Exception error){
            System.out.println("Error "+ error);
        }
    }

    //creating a method to save (INSERT) to the database and also display the same data to the JTable
    public void addNew(Group group) {

        System.out.println("Checking the sql prepareStatement ");
        String sql = "INSERT INTO Groups (group_id, group_name) VALUES(?,?)";
        System.out.println("Checking complete");

        try (PreparedStatement prepStatement = this.con.prepareStatement(sql)) {
            
            prepStatement.setInt(1, group.getGroupId());
            prepStatement.setString(2, group.getGroupName());
            
            System.out.println("Checking the number of rows affected ");
            int numberOfRowsAdded = prepStatement.executeUpdate();
            System.out.println("Number of rows affected is "+ numberOfRowsAdded );

            if(numberOfRowsAdded > 0){
                JOptionPane.showMessageDialog(null, "Data Successfully Added!!");
            }else{
                JOptionPane.showMessageDialog(null, "Data Not Added!!");
            }
            
            readFromDB();
            
        } catch (SQLException error) {
            System.out.println("Error "+ error);
        } catch (Exception error){
            System.out.println("Error "+error);
        }
    }
    
    //creating a method to read and retrive (RETREIVE) the predefined database table
    public ArrayList<Group> readFromDB(){
        
        ArrayList<Group> groups = new ArrayList<>();
        
        String sql = "SELECT group_id, group_name FROM Groups";
        
        try(PreparedStatement prepStatement = this.con.prepareStatement(sql)){
            
            ResultSet result = prepStatement.executeQuery();
            
            while(result.next()){
                int groupId = result.getInt("group_id");
                String groupName = result.getString("group_name");
                
                Group group = new Group(groupId, groupName);
                
                groups.add(group);
            }          
        }catch(SQLException error){
            System.out.println("Error " + error);
        }catch(Exception error){
            System.out.println("Error " + error);
        }
        return groups;
    }
    
    //creating a method that will populate the JTable
    public void populateTable(JTable table){
        
        ArrayList<Group> groups = readFromDB();
        
        String[] columnNames = {"Group ID", "Group Name"};
        Object[][] data = new Object[groups.size()][2];
        
        for(int i = 0; i < groups.size(); i++){
            data[i][0] = groups.get(i).getGroupId();
            data[i][1] = groups.get(i).getGroupName();
        }
        
        DefaultTableModel model = new DefaultTableModel(data,columnNames);
        table.setModel(model);   
    } 
    
    //creating a method to change (UPDATE) the predefined data from the JTable and Database table
    public void update(Group group){
        
        String sql = "UPDATE Groups SET group_name = ? WHERE group_id = ? ";
        
        try(PreparedStatement prepStatement = this.con.prepareStatement(sql)){
            
            prepStatement.setString(1, group.getGroupName());
            prepStatement.setInt(2,group.getGroupId());
            
            int numberOfRowsChanged = prepStatement.executeUpdate();
            
            if(numberOfRowsChanged > 0){
                JOptionPane.showMessageDialog(null, "Update success!!");
            }else{
                JOptionPane.showMessageDialog(null, "Update not successful");
            }
            
        }catch(SQLException error){
            System.out.println("Error " + error);
            
        }catch(Exception error){
            System.out.println("Error "+ error);
        }
    }
    
    //creating a method to remove (DELETE) a selected and predefined data from both JTable and the DATABASE
    public void delete(String groupId){
        
        String sql = "DELETE FROM Groups WHERE group_id = ?";
        
        try(PreparedStatement prepStatement = this.con.prepareStatement(sql)){
            
            prepStatement.setString(1, groupId);
            
            int numberOfRowsRemoved = prepStatement.executeUpdate();
            
            if(numberOfRowsRemoved > 0){
                JOptionPane.showMessageDialog(null, "Data Delected Successfully");
            }else{
                JOptionPane.showMessageDialog(null, "Delection Failed");
            }
        }catch(SQLException error){
            System.out.println("Error "+ error);
        }catch(Exception error){
            System.out.println("Error "+ error);
        }
    }
    
    

}
