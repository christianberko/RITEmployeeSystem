// ISTE-338-Group-Project
// Authors-Anderson Cardenas,Andrew Apollo,Jacob Bier,Nick Berko,Lauren Carver
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class DataLayer {
 
    //Store the Connection 
    private Connection conn;
    private boolean connected;

    //Default MYSQL Driver for Java 
    final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

    public boolean connect(){
        conn = null;
        String userName = "root"; //User name for MYSQL
        String password = "student"; //Password for MYSQL
        String url = "jdbc:mysql://localhost/Professor_Match?serverTimezone=UTC"; //Database we are connecting to
  
        try {
          Class.forName(DEFAULT_DRIVER);
          conn = DriverManager.getConnection(url, userName, password);
          System.out.println("\n Created Connection - Group 1");
          connected = true;
          return connected;
        } catch (ClassNotFoundException cnfe) {
           System.out.println("ERROR, CAN NOT CONNECT!!");
           System.out.println("Class");
           System.out.println("ERROR MESSAGE-> "+cnfe);
           System.exit(0);
           connected = false;
           return connected;
        } catch (SQLException sqle){
           System.out.println("ERROR SQLExcepiton in connect()");
           System.out.println("ERROR MESSAGE -> "+sqle);
           sqle.printStackTrace();
           System.exit(0);
           connected = false;
           return connected;
        }
      }

    /*
     * This Will insert a new account to the accounts table 
     * 
     * Paramaters: 
     *  Email - email of the new account 
     *  Password - password of new account 
     *  Type - Type of account 
     * 
     *  returns the id of the newly created account 
     */
    public int insertAccount(String email, String password, String type) {

        //Hold the ID of the account
        int id = 0;

        try {

            //SQL to insert new account 
            String sql = "INSERT INTO Account (email, password, type) VALUES (?, ?, ?);";

            //SQL to get id of the last inputed account 
            String idSQL = "SELECT LAST_INSERT_ID();";

            //Set a prepared statements 
            PreparedStatement stmt = conn.prepareStatement(sql);

            //Hash the password
            password = toHexString(getSHA(password));

            //Set Prepared statement paramaters and execute query
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, type);
            stmt.executeUpdate();

            //Clear the paramaters 
            stmt.clearParameters();

            //Get last account id
            ResultSet rs = stmt.executeQuery(idSQL);

            //Set the id to the id we got above 
            while(rs.next()){
                id = rs.getInt(1);
            }
            return id; //Return the ID 
        } 
        catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return id;
    }

    /*
     * Insert a new Major into the system
     * 
     * Paramaters: 
     *  Name - name of the major 
     */
    public void insertMajor(String name) {
        try {
            //SQL to insert a major 
            String sql = "INSERT INTO major (name) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            //set prepared statement paramaters 
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Insert a new Interest into the Database 
     * 
     * Paramaters 
     *  Name - name of the interest being inserted 
     *  InterestDescription - description of the interest 
     */
    public void insertInterest(String name, String interestDescription) {
        try {
            
            //Sql to insert a new interest 
            String sql = "INSERT INTO interests (name, interestDescription) VALUES (?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            //Set prepared statement paramaters and execute
            stmt.setString(1, name);
            stmt.setString(2, interestDescription);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Update An interest
     * 
     * Paramaters
     *  InterestID - Id of the interest being updated 
     *  Name - new name of the interest 
     *  InterestDescription - new description of the interest
     */
    public void updateInterest(int interestID,String name, String interestDescription){
        try {

            //SQL to update the selected interest 
            String sql = "UPDATE Interest SET name = ?, interestDescription=? WHERE interestID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                //Set the preparedstatement paramaters and execute 
                stmt.setString(1,name );
                stmt.setString(2,interestDescription);
                stmt.setInt(3,interestID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Delete a Interest 
     * 
     * Paramaters
     *  InterestId - Id of the interest being deleted 
     */
    public void deleteInterest(int interestID){
        try{
            
            //SQL to delete the specified interest 
            String sql  = "DELETE FROM Interest WHERE interestID = ?";

            try(PreparedStatement stmt = conn.prepareStatement(sql)){

                //Set the prepared statement paramaters and execute 
                stmt.setInt(1, interestID);
                stmt.executeUpdate();

                //Print that the deletion was sucessful 
                System.out.println("Delete was successful");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /*
     * Delete a Faculty's interest 
     * 
     * Paramaters
     *  FacultyID - Id of the faculty 
     *  InterestID - Id of the interest being removed 
     */
    public void deleteFacultyInterest(int facultyID, int interestID){
        try{
            String sql  = "DELETE FROM FacultyInterest WHERE facultyID = ? AND interestID = ?";
            try(PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1, facultyID);
                stmt.setInt(2, interestID);
                stmt.executeUpdate();

                System.out.println("Delete was successful");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /*
     * Insert in a new Faculty Interest 
     * 
     * Paramaters 
     *  FacultyID - ID of the faculty 
     *  InterestID - id of the interest beign added 
     */
    public void insertFacultyInterest (int facultyID, int interestID) {
        try {
            
            //SQL to insert the faculty interest 
            String sql = "INSERT INTO FacultyInterest (facultyID, interestID) VALUES (?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                //Set the paramaters for the prepared satement and execute
                stmt.setInt(1, facultyID);
                stmt.setInt(2, interestID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Insert a Student Interest 
     * 
     * Paramaters 
     *  StudentID - id of the student 
     *  InterestId - id of the interes 
     */
    public void insertStudentInterest (int studentID, int interestID) {
        try {

            //SQL to add a student interest
            String sql = "INSERT INTO StudentInterest (studentID, interestID) VALUES (?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                //Set the paramaters for the prepared satement and execute
                stmt.setInt(1, studentID);
                stmt.setInt(2, interestID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Insert a new abstract
     * 
     * Paramaters 
     *  AbstractFile - File of the Abstarct 
     */
    public void insertAbstract (String abstractFile) {
        try {

            //SQL to add the abstract
            String sql = "INSERT INTO Abstract (abstract) VALUES (?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                //Set the paramaters for the prepared satement and execute
                stmt.setString(1, abstractFile);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Update an abstract 
     * 
     * Paramaters
     *  AbstarctID - ID of the abstract
     *  AbstractFile - file of the abstarct 
     */
    public void updateAbstract(int abstractID,String abstractFile){
        try {

            //SQL to update the abstract
            String sql = "UPDATE Abstract SET abstractFile = ? WHERE abstractID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                //Set the paramaters for the prepared satement and execute
                stmt.setString(1, abstractFile);
                stmt.setInt(2, abstractID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Delete an abstract 
     * 
     * Paramaters
     *  AbstractID - id of the abstarct being deleted 
     */
    public void deleteAbstract(int abstractID){
        try{

            //SQL to delete the abstract from faculty abstract
            String DeleteFacultyAbstractSQL = "DELETE FROM Faculty_Abstract WHERE abstractID = ?";
            try(PreparedStatement stmt = conn.prepareStatement(DeleteFacultyAbstractSQL)){

                //Set the paramaters for the prepared satement and execute
                stmt.setInt(1, abstractID);
                stmt.executeUpdate();
                System.out.println("Delete was successful");
            }

            //SQL to delete the abstarct from abstarct 
            String DeleteAbstractSQL = "DELETE FROM Abstract WHERE abstractID = ?";
            try(PreparedStatement stmt2 = conn.prepareStatement(DeleteAbstractSQL)){

                //Set the paramaters for the prepared satement and execute
                stmt2.setInt(1, abstractID);
                stmt2.executeUpdate();
                System.out.println("Delete was successful");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    /*
     * Insert a new Faculty 
     * 
     * Paramaters 
     *  FirstName - First name of the new faculty 
     *  LastName - last name of the new faculty 
     *  PhoneNum - phone number of the new faculty
     *  email - email of the new faculty 
     *  building - building of the new faculty 
     *  office - office of the new faculty 
     * 
     * return number of rows affected 
     */
    public int insertFaculty(String firstName, String lastName, String phoneNum, String email, String building, String office){
        
        //SQL to insert the new faculty 
        String query = "INSERT INTO Faculty (firstName, lastName, phone, email, building, office) VALUES (?,?,?,?,?,?);";
        int numUpdated = 0; 

        //Only enter a new faculty if the data is formated properly 
        if(phoneNum.length() == 10 && email.contains("@") && building.length() <= 4){
            try {

                //Set the paramaters for the prepared satement and execute
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, phoneNum);
                stmt.setString(4, email);
                stmt.setString(5, building);
                stmt.setString(6, office);
                numUpdated = stmt.executeUpdate();

            } catch (SQLException sqle){
                System.out.println("There was an error with the sql: " + sqle);
            } 
            catch (Exception e) {
                System.out.println("There was an error: " + e);
            }
        }else{
            System.out.println("Incorect Format");
            return 0;
        }
        return numUpdated; //send back the number of rows updated 
    }

    /*
     * Insert a new student 
     * 
     * Parmaters 
     *  FirstName - First name of the student being added 
     *  LastName - Last name of the student beign added 
     *  PhoneNum - Phone number of the new student 
     *  Email - eamil of the new student being added 
     *  MajorID - id of the sudents major 
     */
    public int insertStudent(String firstName, String lastName, String phoneNum, String email, int majorID){

        //SQL to insert a Student 
        String query = "INSERT INTO Student (firstName, lastName, phone, email, majorID) VALUES (?,?,?,?,?);";
        int numUpdated = 0;

        //Only insert the information 
        if(phoneNum.length() == 10 && email.contains("@")){
            try {

                //Set the paramaters for the prepared satement and execute
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, phoneNum);
                stmt.setString(4, email);
                stmt.setInt(5, majorID);
                numUpdated = stmt.executeUpdate();
            
            }
            catch (SQLException sqle){
                System.out.println("There was an error with the sql: " + sqle);
            }
            
            catch (Exception e) {
                System.out.println("There was an error: " + e);
            }
        }
        else {
            System.out.println("Incorrect Format");
            return 0;
        }

        return numUpdated;
    }
    
    public void insertFacultyAbstract (int facultyID,int abstractID) {
        try {
            String sql = "INSERT INTO FacultyAbstract (facultyID,abstractID) VALUES (?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, facultyID);
                stmt.setInt(2, abstractID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<List<Object>> searchAbstract (String searchTerm){

        List<List<Object>> facultyList = new ArrayList<>();

        String query = "Select Faculty_Abstract.facultyID AS FacultyId, CONCAT(Faculty.firstName, ' ', Faculty.lastName) AS FacultyName FROM Faculty_Abstract";
                query += " JOIN Faculty USING(facultyID)";
                query += " JOIN Abstract USING(abstractID) WHERE abstractFile LIKE ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchTerm + "%");

            ResultSet rs =  stmt.executeQuery();

            while (rs.next()){
                int facId = rs.getInt(1);
                String facName = rs.getString(2);

                List<Object> faculty = new ArrayList<>();
                faculty.add(facId);
                faculty.add(facName);

                facultyList.add(faculty);
            }

        }
        catch (SQLException sqle){
            System.out.println("There was an error with the sql: " + sqle);
        }
        
        catch (Exception e) {
            System.out.println("There was an error: " + e);
        }

        return facultyList;
    }

    public List<List<Object>> SearchStudentInterest(int userId) {
        List<List<Object>> studentList = new ArrayList<>();
        String query = "SELECT DISTINCT " +
                   "CONCAT(Student.firstName, ' ', Student.lastName) AS StudentName, " +
                   "Student.email, Student.phone, " +
                   "Major.name AS MajorName, " +
                   "Interest.name AS InterestName " +
                   "FROM Student " +
                   "JOIN StudentInterest ON Student.studentID = StudentInterest.studentID " +
                   "JOIN Interest ON StudentInterest.interestID = Interest.interestID " +
                   "JOIN Major ON Student.majorID = Major.majorID " +
                   "WHERE Interest.interestID IN (" +
                   "    SELECT FacultyInterest.interestID " +
                   "    FROM FacultyInterest " +
                   "    WHERE FacultyInterest.facultyID = ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    List<Object> student = new ArrayList<>();
                    student.add(rs.getObject("StudentName"));
                    student.add(rs.getObject("email"));
                    student.add(rs.getObject("phone"));
                    student.add(rs.getObject("MajorName"));
                    student.add(rs.getObject("InterestName"));
                    studentList.add(student);
                }
            }
        } catch (SQLException sqle) {
            System.err.println("SQL error: " + sqle);
            throw new RuntimeException("Database error occurred", sqle);
        } catch (Exception e) {
            System.err.println("Error: " + e);
            throw new RuntimeException("Error processing data", e);
        }
        return studentList;
    }

    public List<List<Object>> SearchFacultyInterest(int userId) {
        List<List<Object>> facultyList = new ArrayList<>();
        String query = "SELECT DISTINCT "
        + "CONCAT(Faculty.firstName, ' ', Faculty.lastName) AS FacultyName, "
        + "Faculty.email, Faculty.phone, Faculty.building, Faculty.office, "
        + "Interest.name AS InterestName "
        + "FROM Faculty " 
        + "JOIN FacultyInterest ON Faculty.facultyID = FacultyInterest.facultyID "
        + "JOIN Interest ON FacultyInterest.interestID = Interest.interestID " 
        + "WHERE Interest.interestID IN ("
        + "SELECT StudentInterest.interestID "
        + "FROM StudentInterest " 
        + "WHERE StudentInterest.studentID = ?)";
    
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    List<Object> faculty = new ArrayList<>();
                    faculty.add(rs.getObject("FacultyName"));
                    faculty.add(rs.getObject("email"));
                    faculty.add(rs.getObject("phone"));
                    faculty.add(rs.getObject("building"));
                    faculty.add(rs.getObject("office"));
                    faculty.add(rs.getObject("InterestName"));
                    facultyList.add(faculty);
                }
            }
        } catch (SQLException sqle) {
            System.err.println("SQL error: " + sqle);
            throw new RuntimeException("Database error occurred", sqle);
        } catch (Exception e) {
            System.err.println("Error: " + e);
            throw new RuntimeException("Error processing data", e);
        }
        return facultyList;
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
     
    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
 
        StringBuilder hexString = new StringBuilder(number.toString(16));
 
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }

    public String login(String email, String password){

        String query = "SELECT accountID, type, password FROM ACCOUNT WHERE email=?";
        String accountType = "";
        String storedPass = "";
        String accountID = "";

        try {
            String hashedPass = toHexString(getSHA(password));
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs =  stmt.executeQuery();

            while (rs.next()) {
                accountID = rs.getString(1);
                accountType = rs.getString(2);
                storedPass = rs.getString(3);
            }

            if(hashedPass.equals(storedPass)){
                return accountID + "," + accountType;
            }
            else{
                return "Incorrect password for account " + email;
            }
        } 
        catch (SQLException sqle){
            System.out.println("There was an error with the sql: " + sqle);
        }
        catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    public int getLastInsertedAbstractID() {
        int id = 0;
        try {
            String idSQL = "SELECT LAST_INSERT_ID();";
            PreparedStatement stmt = conn.prepareStatement(idSQL);

            ResultSet rs = stmt.executeQuery(idSQL);

            

            while(rs.next()){
                id = rs.getInt(1);
            }
            return id;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<Object> getUserInterests(int accountID, String userType) {
        List<Object> interests = new ArrayList<>();

        String query = "";
        if (userType.equals("Student")) {
            query = "SELECT Interest.name FROM StudentInterest " +
                    "JOIN Interest ON StudentInterest.interestID = Interest.interestID " +
                    "WHERE StudentInterest.studentID = ?";
        } else if (userType.equals("Faculty")) {
            query = "SELECT Interest.name FROM FacultyInterest " +
                    "JOIN Interest ON FacultyInterest.interestID = Interest.interestID " +
                    "WHERE FacultyInterest.facultyID = ?";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, accountID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                interests.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interests;
    }

    public List<Object> getFacultyAbstracts(int accountID) {
        List<Object> abstracts = new ArrayList<>();

       String query = "SELECT Abstract.abstractFile FROM Faculty_Abstract " +
                    "JOIN Abstract ON Faculty_Abstract.abstractID = Abstract.abstractID " +
                    "WHERE Faculty_Abstract.facultyID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, accountID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                abstracts.add(rs.getString("abstractFile"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return abstracts;
    }
    public int insertOutside(String orgName, String phoneNum, String email){

        //SQL to insert an Outside
        String query = "INSERT INTO Outside(orgName, phone, email) VALUES (?,?,?);";
        int numUpdated = 0;

        //Only insert the information 
        if(phoneNum.length() == 10 && email.contains("@")){
            try {

                //Set the paramaters for the prepared satement and execute
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, orgName);
                stmt.setString(2, phoneNum);
                stmt.setString(3, email);
                numUpdated = stmt.executeUpdate();
            
            }
            catch (SQLException sqle){
                System.out.println("There was an error with the sql: " + sqle);
            }
            
            catch (Exception e) {
                System.out.println("There was an error: " + e);
            }
        }
        else {
            System.out.println("Incorrect Format");
            return 0;
        }

        return numUpdated;
    }
    public static void main(String[] args) {
        DataLayer dl = new DataLayer();
        dl.connect();

        List<List<Object>> results = dl.SearchStudentInterest(1);
        System.out.println(results.toString());
    }
}