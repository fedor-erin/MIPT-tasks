package ru.ncedu.java.tasks;

public class EmployeeImpl implements Employee {
    private int salary;   
    private String firstName;
    private String lastName;
    private Employee manager;
    public EmployeeImpl() {
        this.salary = 1000;
    }
    public int getSalary(){
        return salary;
    }
    public void increaseSalary(int value){
        this.salary += value;
    }
    public String getFirstName(){
        return firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getFullName(){
        return getFirstName() + " " + getLastName();
    }
    public void setManager(Employee manager){
        this.manager = manager;
    }
    public String getManagerName(){
        if (this.manager == null){
        return "No manager";
        }
        else{
        return manager.getFullName();
        }
    }
    public Employee getTopManager(){
        if (this.manager == null){
            return this;
        }
        else{
            return manager.getTopManager();
        }
    }
}