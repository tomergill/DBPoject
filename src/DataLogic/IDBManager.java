package DataLogic;

public interface IDBManager {
    String  DMLQuery        (String query);
    String  DDLQuery        (String query);
    boolean closeConnection ();
}
