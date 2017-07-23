package DataLogic;

import java.io.IOException;

public interface IDBManager {
    String  DMLQuery        (String query);
    String  DDLQuery        (String query);
    boolean closeConnection ();
    String  executeScript   (String scriptPath) throws IOException;
}
