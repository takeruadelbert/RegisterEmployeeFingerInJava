/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author STN-COM-01
 */
public interface Database {

    boolean openConnection(boolean type);

    boolean closeConnection();

    boolean execute_query(boolean type, String query); // for insert, update & delete operation

    boolean checkMySQLConnection(boolean type);
}
