public class Driver {
    public static void main(String[] args) {

        ManagerLayer dbm = new ManagerLayer();
        System.out.println("Running test");
        //dbm.InsertTest();
        //dbm.RetrieveAllTest();
        dbm.errorTest();
    }
}
