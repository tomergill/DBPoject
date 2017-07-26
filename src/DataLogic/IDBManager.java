package DataLogic;

import java.io.IOException;
import java.util.List;

/**
 * Interface for querying a remote Database.
 *
 * The format of returned tables:
 * colname_1,colname_2,...colname_n
 * row_1col_1,row_1col_2,...,row_1col_n
 * .
 * .
 * .
 * row_ncol_1,row_ncol_2,...,row_ncol_n
 *
 * The format of number of returned rows:
 * Rows affected :n
 *
 * The formar for errors:
 * LOGICAL ERROR / WRONG QUERY STRUCTURE
 * exception's massage
 */
public interface IDBManager {

    /**
     * Executes a DML query/update to the DB.
     * @param query The query to execute on the DB.
     * @return Either a table, the number of rows affected or an error message, in the format
     *      specified in the interface's javadoc.
     */
    String  DMLQuery          (String query);

    /**
     * Executes a DDL query/update to the DB.
     * @param query The query to execute on the DB.
     * @return Either a table, the number of rows affected or an error message, in the format
     *      specified in the interface's javadoc.
     */
    String  DDLQuery          (String query);

    /**
     * Closes the connection to the DB.
     * @return true if the connection closed successfully, otherwise returns false.
     */
    boolean closeConnection   ();

    /**
     * Executes the script query by query.
     * @param scriptPath Path to script file to read from.
     * @return A list of answers, one answer to each query in the script file.
     * @throws IOException In case opening the script is unsuccessful.
     */
    List<String> executeScript(String scriptPath) throws IOException;
}
