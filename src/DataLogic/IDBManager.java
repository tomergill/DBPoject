package DataLogic;

import java.io.IOException;
import java.util.List;

public interface IDBManager {
    String  DMLQuery          (String query);
    String  DDLQuery          (String query);
    boolean closeConnection   ();
    List<String> executeScript(String scriptPath) throws IOException;
}
