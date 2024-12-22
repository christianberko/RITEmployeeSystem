import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PresentationLayer {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataLayer dataLayer = new DataLayer();

        if (dataLayer.connect()) {
            System.out.println("Connected to the database.");
        }

        boolean loginSuccess = false;
        boolean quit = false;
        String accountType = null;
        String searchTerm;
        String userName = ""; 
        String password;
        int loginChoice;
        int quitChoice;

        while (!quit) {   
            while (!loginSuccess && !quit) {
                System.out.println("Choose an option:");
                System.out.println("1 - Login");
                System.out.println("2 - Create new account");
                System.out.println("3 - Quit");
                System.out.print("Make a Selection: ");
                try {
                    loginChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline left-over
                    switch (loginChoice) {
                        case 1:
                            System.out.print("Enter Email: ");
                            String loginEmail = scanner.nextLine(); // Changed to nextLine()
                            System.out.print("Enter password: ");
                            password = scanner.nextLine(); // Changed to nextLine()
                            String login = dataLayer.login(loginEmail, password);
                            userName = login.split(",")[0];
                            accountType = login.split(",")[1];
                            if ("Student".equals(accountType) || "Faculty".equals(accountType) || "Outside".equals(accountType)) {
                                loginSuccess = true;
                            } else {
                                System.out.println("Account Information Incorrect");
                            }
                            break;
                        case 2:
                            System.out.print("Enter Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Enter password: ");
                            String newPassword = scanner.nextLine();
                            System.out.println("Select Account Type:");
                            System.out.println("1 - Student | 2 - Faculty | 3 - Outside Org");
                            int accountTypeNum = scanner.nextInt();
                            int id = 0;
                            switch(accountTypeNum){
                                case 1:
                                    id = dataLayer.insertAccount(email, newPassword, "Student");
                                    System.out.println("Please insert your first name");
                                    String fname=scanner.nextLine();
                                    System.out.println("Please insert your last name");
                                    String lname=scanner.nextLine();
                                    System.out.println("Please insert your phone number");
                                    String phNum=scanner.nextLine();
                                    System.out.println("Please insert your major ID from list below");
                                    //input Majorlist print here <--
                                    int major=scanner.nextInt();
                                    
                                    dataLayer.insertStudent( fname,lname,phNum,email, major);
                                    break;
                                case 2:
                                    id = dataLayer.insertAccount(email, newPassword, "Faculty");
                                    System.out.println("Please insert your first name");
                                    fname=scanner.nextLine();
                                    System.out.println("Please insert your last name");
                                    lname=scanner.nextLine();
                                    System.out.println("Please insert your phone number");
                                    phNum=scanner.nextLine();
                                    System.out.println("Please insert your building name up to 4 characters");
                                    String building=scanner.nextLine();
                                    System.out.println("Please insert your room number");
                                    String office=scanner.nextLine();
                                    dataLayer.insertFaculty(fname,lname,phNum,email, building, office);
                                    break;
                                case 3:
                                    id = dataLayer.insertAccount(email, newPassword, "Outside");
                                    System.out.println("Please insert your first name and last name");
                                    String fullName=scanner.nextLine();
                                    System.out.println("Please insert your phone number");
                                    phNum=scanner.nextLine();
                                    dataLayer.insertOutside(fullName,phNum,email);
                                default:
                                    System.out.println("Please select a valid selection (1-3)");
                                    break;

                            }
                            System.out.println("Your id is " + id + '\n');
                            break;
                        case 3:
                            System.out.println("Are you sure?");
                            System.out.println("1 - yes");
                            System.out.println("2 - no");
                            quitChoice = scanner.nextInt();
                            System.out.println(quitChoice);
                            if (quitChoice == 1) {
                                quit = true;
                            }
                            break;
                        default:
                            System.out.println("Please select a valid selection (1-3)");
                            break;
                    }
                } catch(InputMismatchException e) {
                    System.out.println("Please enter an integer." + '\n');
                    scanner.nextLine();
                } 
            }

            while (loginSuccess && !quit) {
                try {
                    switch(accountType){
                        case ("Student"):
                            System.out.println("Welcome, Student");
                            System.out.println();
                            System.out.println("Choose an option:");
                            System.out.println("1 - Search Faculty Interest");
                            System.out.println("2 - Search Faculty Abstracts");
                            System.out.println("3 - Add Interest");
                            System.out.println("4 - See Own Interests");
                            System.out.println("5 - Quit");
                            int actionChoice = scanner.nextInt();
                            scanner.nextLine();
                            switch (actionChoice) {
                                case 1:
                                    List<List<Object>> interests = dataLayer.SearchFacultyInterest(Integer.parseInt(userName));
                                    System.out.println("Here are Faculty that have similar interests" + '\n');
                                    for (List<Object> record : interests) {
                                        String facultyName = record.get(0).toString();
                                        String facultyEmail = record.get(1).toString();
                                        String facultyPhone = record.get(2).toString();
                                        String facultyBuilding = record.get(3).toString();
                                        String facultyRoom = record.get(4).toString();
                                        String commonInterest = record.get(5).toString();

                                        System.out.println("Faculty: " + facultyName + " | Email: " + facultyEmail + " | Phone: " + facultyPhone + " | Building: " + facultyBuilding + " | Room: " + facultyRoom);
                                        System.out.println("Common Interest: " + commonInterest + '\n');
                                    }
                                    break;
                                case 2:
                                    System.out.print("Enter search term: ");
                                    searchTerm = scanner.nextLine();
                                    List<List<Object>> data = dataLayer.searchAbstract(searchTerm);
                                    for (List<Object> record : data) {
                                        System.out.println(record.get(0) + ", " + record.get(1));
                                    }
                                    break;
                                case 3:
                                    System.out.print("Enter interest term ID: ");
                                    int interest = scanner.nextInt();
                                    scanner.nextLine(); // Consume the newline left-over
                                    dataLayer.insertStudentInterest(Integer.parseInt(userName), interest);
                                    break;
                                case 4:
                                    System.out.println(dataLayer.getUserInterests(Integer.parseInt(userName), "Student"));
                                    break;
                                case 5:
                                    System.out.println("Are you sure?");
                                    System.out.println("1 - yes");
                                    System.out.println("2 - no");
                                    quitChoice = scanner.nextInt();
                                    if (quitChoice == 1) {
                                        quit = true;
                                    }
                                    break;
                                default:
                                    System.out.println("Please select a valid choice (1-4)");
                                    break;
                            }
                            break;
                        case ("Faculty"):
                            System.out.println("Welcome, Faculty");
                            System.out.println();
                            System.out.println("Choose an option:");
                            System.out.println("1 - Search Student Interests");
                            System.out.println("2 - Insert Abstracts or Interests");
                            System.out.println("3 - Update Abstracts");
                            System.out.println("4 - Delete Abstracts or Interests");
                            System.out.println("5 - See Own Interests");
                            System.out.println("6 - See Own Abstracts");
                            System.out.println("7 - Quit");
                            int actionChoiceFac = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline left-over
                            switch (actionChoiceFac) {
                                case 1:
                                    List<List<Object>> data = dataLayer.SearchStudentInterest(Integer.parseInt(userName));
                                    System.out.println("Here are Students that have similar interests" + '\n');
                                    for (List<Object> studentList : data) {
                                        String studentName = studentList.get(0).toString();
                                        String studentEmail = studentList.get(1).toString();
                                        String studentPhone = studentList.get(2).toString();
                                        String studentMajor= studentList.get(3).toString();
                                        String commonInterest = studentList.get(4).toString();

                                        System.out.println("Student: " + studentName + " | Email: " + studentEmail + " | Phone: " + studentPhone + " | Major: " + studentMajor);
                                        System.out.println("Common Interest: " + commonInterest + '\n');
                                    }
                                    break;
                                case 2:
                                    System.out.print("Enter what you want to insert (Abstract or Interest): ");
                                    String typeInsert = scanner.nextLine();
                                    if(typeInsert.equalsIgnoreCase("Abstract")) {
                                        System.out.println("Enter Abstract:");
                                        String insertedValue = scanner.nextLine();
                                        dataLayer.insertAbstract(insertedValue);
                                        int abstractID = dataLayer.getLastInsertedAbstractID();
                                        dataLayer.insertFacultyAbstract(Integer.parseInt(userName), abstractID);
                                    } else if(typeInsert.equalsIgnoreCase("Interest")) {
                                        System.out.print("Enter interest term ID: ");
                                        int interest = scanner.nextInt();
                                        scanner.nextLine(); // Consume the newline left-over
                                        dataLayer.insertFacultyInterest(Integer.parseInt(userName), interest);
                                    } else {
                                        System.out.println("Please enter a valid answer: (Abstract/Interest)");
                                    }
                                    break;
                                case 3:
                                    System.out.println("Enter abstractID: ");
                                    int id = scanner.nextInt();
                                    System.out.println("Enter updated abstract: ");
                                    String abs = scanner.nextLine();
                                    dataLayer.updateAbstract(id, abs);
                                    break;
                                case 4:
                                    System.out.print("Enter what you want to delete (Abstract or Interest): ");
                                    String typeDelete = scanner.nextLine();
                                    if(typeDelete.equalsIgnoreCase("Abstract")) {
                                        System.out.println("Enter Abstract id:");
                                        int absid = scanner.nextInt();
                                        dataLayer.deleteAbstract(absid);
                                    } else if(typeDelete.equalsIgnoreCase("Interest")) {
                                        System.out.print("Enter interest term ID: ");
                                        int interest = scanner.nextInt();
                                        scanner.nextLine(); // Consume the newline left-over
                                        dataLayer.deleteFacultyInterest(Integer.parseInt(userName), interest);
                                    } else {
                                        System.out.println("Please enter a valid answer: (Abstract/Interest)");
                                    }
                                    break;
                                case 5:
                                        System.out.println(dataLayer.getUserInterests(Integer.parseInt(userName), "Faculty"));
                                        break;
                                case 6:
                                        System.out.println(dataLayer.getFacultyAbstracts(Integer.parseInt(userName)));
                                        break;
                                case 7:
                                    System.out.println("Are you sure?");
                                    System.out.println("1 - yes");
                                    System.out.println("2 - no");
                                    quitChoice = scanner.nextInt();
                                    scanner.nextLine(); // Consume the newline left-over
                                    if (quitChoice == 1) {
                                        quit = true;
                                    }
                                    break;
                                default:
                                    System.out.println("Please select a valid choice (1-5)");
                                    break;
                            }
                            break;
                        case ("Outside"):
                            System.out.println("Welcome, Outside Organization");
                            System.out.println();
                            System.out.println("Choose an option:");
                            System.out.println("1 - Search Student Interests");
                            System.out.println("2 - Search Faculty Interests");
                            System.out.println("3 - Quit");
                            int actionChoiceOut = scanner.nextInt();
                            switch(actionChoiceOut){
                                case 1:
                                List<List<Object>> data = dataLayer.SearchStudentInterest(Integer.parseInt(userName));
                                System.out.println("Here are Students that have similar interests" + '\n');
                                for (List<Object> studentList : data) {
                                    String studentName = studentList.get(0).toString();
                                    String studentEmail = studentList.get(1).toString();
                                    String studentPhone = studentList.get(2).toString();
                                    String studentMajor= studentList.get(3).toString();
                                    String commonInterest = studentList.get(4).toString();

                                    System.out.println("Student: " + studentName + " | Email: " + studentEmail + " | Phone: " + studentPhone + " | Major: " + studentMajor);
                                    System.out.println("Common Interest: " + commonInterest + '\n');
                                }
                                break;
                                case 2:
                                List<List<Object>> interests = dataLayer.SearchFacultyInterest(Integer.parseInt(userName));
                                    System.out.println("Here are Faculty that have similar interests" + '\n');
                                    for (List<Object> record : interests) {
                                        String facultyName = record.get(0).toString();
                                        String facultyEmail = record.get(1).toString();
                                        String facultyPhone = record.get(2).toString();
                                        String facultyBuilding = record.get(3).toString();
                                        String facultyRoom = record.get(4).toString();
                                        String commonInterest = record.get(5).toString();

                                        System.out.println("Faculty: " + facultyName + " | Email: " + facultyEmail + " | Phone: " + facultyPhone + " | Building: " + facultyBuilding + " | Room: " + facultyRoom);
                                        System.out.println("Common Interest: " + commonInterest + '\n');
                                    }
                                    break;
                                case 3:
                                    System.out.println("Are you sure?");
                                    System.out.println("1 - yes");
                                    System.out.println("2 - no");
                                    quitChoice = scanner.nextInt();
                                    scanner.nextLine(); // Consume the newline left-over
                                    if (quitChoice == 1) {
                                        quit = true;
                                    }
                                break;
                                default:
                                    System.out.println("Please select a valid selection (1-3)");
                                    break;
                            }
                        default:
                            System.out.println("Invalid Account Type");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Please enter an integer." + '\n');
                    scanner.nextLine();
                }
            }
        }
        scanner.close();
    }
}
