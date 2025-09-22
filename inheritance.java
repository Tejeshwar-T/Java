import java.util.Scanner;

class Employee {
    String name;
    double basicPay;

    Employee(String name, double bp) {
        this.name = name;
        this.basicPay = bp;
    }

    void calculate(double da, double hra, double pf, double staff, String desg) {
        double gross = basicPay + (da*basicPay) + (hra*basicPay);
        double net = gross - ((pf*basicPay) + (staff*basicPay));

        System.out.println("\n--- Salary Slip ---");
        System.out.println("Name        : " + name);
        System.out.println("Designation : " + desg);
        System.out.println("Gross Salary: " + gross);
        System.out.println("Net Salary  : " + net);
    }
}

class Programmer extends Employee {
    Programmer(String n, double bp) { super(n, bp); }
    void show() { calculate(0.97, 0.10, 0.12, 0.01, "Programmer"); }
}

class AssistantProfessor extends Employee {
    AssistantProfessor(String n, double bp) { super(n, bp); }
    void show() { calculate(1.10, 0.20, 0.12, 0.05, "Assistant Professor"); }
}

class AssociateProfessor extends Employee {
    AssociateProfessor(String n, double bp) { super(n, bp); }
    void show() { calculate(1.20, 0.30, 0.12, 0.05, "Associate Professor"); }
}

class Professor extends Employee {
    Professor(String n, double bp) { super(n, bp); }
    void show() { calculate(1.40, 0.40, 0.12, 0.10, "Professor"); }
}

public class EmployeeSalary {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Employee Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Basic Pay: ");
        double bp = sc.nextDouble();

        System.out.println("1. Programmer");
        System.out.println("2. Assistant Professor");
        System.out.println("3. Associate Professor");
        System.out.println("4. Professor");
        System.out.print("Enter choice: ");
        int ch = sc.nextInt();

        if(ch==1) new Programmer(name, bp).show();
        else if(ch==2) new AssistantProfessor(name, bp).show();
        else if(ch==3) new AssociateProfessor(name, bp).show();
        else if(ch==4) new Professor(name, bp).show();
        else System.out.println("Invalid choice!");

        sc.close();
    }
}

