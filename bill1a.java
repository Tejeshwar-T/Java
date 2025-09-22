import java.util.Scanner;
public class bill {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the Consumer no : ");
        int cno = scan.nextInt();
        scan.nextLine();
        System.out.print("Enter the name of the Consumer : ");
        String name = scan.nextLine();
        System.out.print("Enter the type of connection (1.Domestic|2.Commercial) (1/2) : ");
        int type = scan.nextInt();
        System.out.print("Enter the previous month reading : ");
        int prev = scan.nextInt();
        System.out.print("Enter the current month reading : ");
        int curr = scan.nextInt();
        int read;
        read =curr - prev;
        System.out.print("your current month readings is"+read);
        double amount=0;
        if (type == 1) {
            if (read >= 0 && read <= 100) {
                amount = read * 1;
            } else if (read >= 101 && read <= 200) {
                amount = read * 2.50;
            } else if (read >= 201 && read <= 500) {
                amount = read * 4;
            } else if (read >= 501) {
                amount = read * 6;
            } else {
                System.out.println("Invalid reading please check it again!");
            }
        } else if (type == 2) {
            if (read >= 0 && read <= 100) {
                amount = read * 2;
            } else if (read >= 101 && read <= 200) {
                amount = read * 4.50;
            } else if (read >= 201 && read <= 500) {
                amount = read * 6;
            } else if (read >= 501) {
                amount = read * 7;
            }
        }else{
            System.out.println("Invalid Type please check again!");
            scan.close();
            return;
           
        }
        System.out.println("--------Electricity Bill--------");
        System.out.println("Counsumer Name: "+name);
        System.out.println("Consumer no: "+cno);
        System.out.println("Current Month reading: ");
        System.out.println("Bill Amount:" +amount);
        scan.close();
    }

}
