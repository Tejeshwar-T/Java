import java.util.*;

class contactsystem1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> contacts = new ArrayList<>();
        int ch;
        do {
            System.out.println("\n1. Add  2. View  3. Search  4. Delete  5. Exit  6. Update");
            System.out.print("Choose an option: ");
            ch = sc.nextInt();
            sc.nextLine(); 
            if (ch == 1) {
                System.out.print("Enter name to add: ");
                contacts.add(sc.nextLine());
                System.out.println("Contact added.");
            } else if (ch == 2) {
                if (contacts.isEmpty()) {
                    System.out.println("No contacts found.");
                } else {
                    System.out.println("Contacts:");
                    for (String c : contacts) {
                        System.out.println(c);
                    }
                }
            } else if (ch == 3) {
                System.out.print("Enter name to search: ");
                String name = sc.nextLine();
                System.out.println(contacts.contains(name) ? "Found" : "Not Found");
            } else if (ch == 4) {
                System.out.print("Enter name to delete: ");
                String nameToDelete = sc.nextLine();
                if (contacts.remove(nameToDelete)) {
                    System.out.println("Contact deleted.");
                } else {
                    System.out.println("Contact not found.");
                }
            } else if (ch == 6) {
                System.out.print("Enter name to update: ");
                String oldName = sc.nextLine();
                if (contacts.contains(oldName)) {
                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine();
                    int index = contacts.indexOf(oldName);
                    contacts.set(index, newName);
                    System.out.println("Contact updated.");
                } else {
                    System.out.println("Contact not found.");
                }
            } else if (ch != 5) {
                System.out.println("Invalid option. Try again.");
            }
        } while (ch != 5);
        System.out.println("Exiting program.");
        sc.close();
    }
}
