package Database;

import Database.Data.Employee;
import Database.Data.TemplateFinger;
import Helper.TKHelper;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author STN-COM-01
 */
public class DBConnect implements Database {

    private String db_host;
    private String db_name;
    private String db_staff;
    private String db_username;
    private String db_password;
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public DBConnect(String db_host, String db_name, String db_staff, String db_username, String db_password) {
        this.db_host = db_host;
        this.db_name = db_name;
        this.db_username = db_username;
        this.db_password = db_password;
        this.db_staff = db_staff;
    }

    @Override
    public boolean openConnection(boolean is_for_staff) {
        try {
            Class.forName(JDBC_DRIVER);
            String db_type = is_for_staff ? db_staff : db_name;
            String db_URL = "jdbc:mysql://" + db_host + "/" + db_type;
            connection = DriverManager.getConnection(db_URL, db_username, db_password);
            return connection != null ? true : false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean closeConnection() {
        try {
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkMySQLConnection(boolean is_for_staff) {
        boolean successful = true;
        try {
            successful = is_for_staff ? openConnection(true) : openConnection(false);
            if (successful) {
                closeConnection();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            successful = false;
        }
        return successful;
    }

    @Override
    public boolean execute_query(boolean is_for_staff, String query) {
        try {
            if (is_for_staff) {
                openConnection(true);
            } else {
                openConnection(false);
            }
            statement = connection.createStatement();
            statement.executeUpdate(query);
            closeConnection();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean checkSignIn(String username, String password) {
        try {
            if (openConnection(true)) {
                // fetch data user by username first and then compare the password from database
                String query = "select id, password, salt from users where username = '" + username + "' limit 1";
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                String salt = "", hashedPasswordFromDB = "";
                while (resultSet.next()) {
                    salt = resultSet.getString("salt");
                    hashedPasswordFromDB = resultSet.getString("password");
                }
                closeConnection();

                // compare password if it's match
                String hashedPassword = TKHelper.encryptStringToSHA512Format(password + salt);
                return hashedPassword.equals(hashedPasswordFromDB);
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public Employee get_data_employee(String NIK) {
        Employee employee = null;
        if (!NIK.isEmpty()) {
            try {
                if (openConnection(false)) {
                    String query = "select id, nik, emp_firstName as firstName, emp_lastName as lastName from hr_employee where nik = " + NIK + " limit 1";
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        int employee_id = resultSet.getInt("id");
                        String employee_name = resultSet.getString("firstName") + resultSet.getString("lastName");
                        String employee_NIK = resultSet.getString("nik");

                        employee = new Employee(employee_id, employee_name, employee_NIK);
                    }
                    closeConnection();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return get_data_template_finger(employee);
    }

    private Employee get_data_template_finger(Employee employee) {
        if (employee.getId() != -1) {
            List<TemplateFinger> template = new ArrayList();
            try {
                if (openConnection(false)) {
                    String query = "select id, template_len, template_index from hr_template where employee_id = " + employee.getId();
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        int template_length = resultSet.getInt("template_len");
                        int index_finger = resultSet.getInt("template_index");
                        TemplateFinger templateFinger = new TemplateFinger(template_length, index_finger);
                        template.add(templateFinger);
                    }
                    employee.setTemplate(template);
                    closeConnection();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return employee;
    }
}
