package DataLogic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.sql.DriverManager;

/**
 * Created by Tomer Gill on 06-Jul-17.
 */
public class DBManager implements IDBManager {
    private Connection con;
    private static List<String> DDLReturnAnswer = null;
    private static List<String> DMLAffectingRows = null;

    public DBManager(String path) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String url = reader.readLine();
        String uname = reader.readLine();
        String pass = reader.readLine();
        this.con = DriverManager.getConnection(url, uname, pass);

        if (DDLReturnAnswer == null) {
            DDLReturnAnswer = new LinkedList<>();
            DDLReturnAnswer.add("SHOW");
            DDLReturnAnswer.add("DESCRIBE");
        }
        if (DMLAffectingRows == null) {
            DMLAffectingRows = new LinkedList<>();
            DMLAffectingRows.add("UPDATE");
            DMLAffectingRows.add("INSERT");
            DMLAffectingRows.add("DELETE");

        }
    }

    @Override
    public String DMLQuery(String query) {

        boolean answer = false;
        for (String command : DMLAffectingRows) {
            if (answer = query.startsWith(command))
                break;
        }


        ResultSet result;
        try {
            Statement statement = this.con.createStatement();
            result = statement.executeQuery(query);

            if (!answer) { //returns a table
                StringBuilder table = new StringBuilder("");
                int colNum = result.getMetaData().getColumnCount();

                //put columns headers
                int i;
                for (i = 1; i < colNum; i++) {
                    table.append(result.getMetaData().getColumnName(i));
                    table.append(",");
                }
                if (i == colNum) {
                    table.append(result.getMetaData().getColumnName(i));
                    table.append("\n");
                }

                //get each row's data
                while (result.next()) {
                    for (i = 1; i < colNum; i++) {
                        table.append(result.getString(i));
                        table.append(",");
                    }
                    if (i == colNum){
                        table.append(result.getString(i));
                        table.append("\n");
                    }
                }

                return table.toString();
            } else {
                return "Rows affected :" + statement.executeUpdate(query) + "\n";
            }
        } catch (SQLException sql) {
            return errMsgFromException(sql);
        }
    }

    @Override
    public String DDLQuery(String query) {

        boolean returnsAnswer = false;
        for (String command : DDLReturnAnswer) {
            if (returnsAnswer = query.startsWith(command))
                break;
        }

        try {
            Statement statement = this.con.createStatement();

            if (returnsAnswer) {
                ResultSet result = statement.executeQuery(query);
                StringBuilder info = new StringBuilder("");
                int colNum = result.getMetaData().getColumnCount();

                //get each row's data
                int i;
                while (result.next()) {
                    for (i = 1; i < colNum; i++) {
                        info.append(result.getString(i));
                        info.append(",");
                    }
                    if (i == colNum){
                        info.append(result.getString(i));
                        info.append("\n");
                    }
                }

                return info.toString();
            } else {
                return "Rows affected :" + statement.executeUpdate(query) + "\n";
            }
        } catch (SQLException sql) {
            return errMsgFromException(sql);
        }
    }

    @Override
    public boolean closeConnection() {
        try {
            con.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error disconnecting from SQL server:");
            System.out.println(e.toString());
            return false;
        }
    }

    private String errMsgFromException(SQLException e) {
        String result;
        if (e.toString().contains("syntax"))
            result = "WRONG QUERY STRUCTURE";
        else
            result = "LOGICAL ERROR";

        result += "\n" + e.toString();
        return result;
    }

    /*public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = con.createStatement();
        return statement.executeQuery(query);
    }

    public int executeCommand(String query) throws SQLException {
        Statement statement = con.createStatement();
        return statement.executeUpdate(query);
    }*/
}
