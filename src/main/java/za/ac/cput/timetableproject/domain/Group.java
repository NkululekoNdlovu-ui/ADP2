/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.timetableproject.domain;

/**
 *
 * @author hloni
 */
public class Group {
     private int groupId;
     private String groupName;

    public Group(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public Group() {
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return  groupId + " "+ groupName ;
    }
   
    
        
}
