package JavaBankAccountBSTSystem;

import java.io.*;
import java.util.Scanner;

class BankAccount {
    int accountId;
    String customerName;
    String branchName;
    double balance;

    public BankAccount(int accountId, String customerName, String branchName, double balance) {
        this.accountId = accountId;
        this.customerName = customerName;
        this.branchName = branchName;
        this.balance = balance;
    }
}

class Node {
    BankAccount account;
    Node left, right;

    public Node(BankAccount account) {
        this.account = account;
        this.left = this.right = null;
    }
}
//C:\Users\Oktay Can\Desktop\221805001_OktayCanSevimgin
class BinarySearchTree {
    Node root;
    String filePath = "C:\\Users\\Oktay Can\\Desktop\\221805001_OktayCanSevimgin\\inputBst.txt";

    public BinarySearchTree() {
        root = null;
    }

    public void insert(int accountId, String customerName, String branchName, double balance) {
        root = insertRec(root, accountId, customerName, branchName, balance);
        updateFile(); // Dosyayı güncelle
    }

    private Node insertRec(Node root, int accountId, String customerName, String branchName, double balance) {
        if (root == null) {
            root = new Node(new BankAccount(accountId, customerName, branchName, balance));
            return root;
        }
        if (accountId < root.account.accountId)
            root.left = insertRec(root.left, accountId, customerName, branchName, balance);
        else if (accountId > root.account.accountId)
            root.right = insertRec(root.right, accountId, customerName, branchName, balance);
        return root;
    }

    public BankAccount search(int accountId) {
        return searchRec(root, accountId);
    }

    private BankAccount searchRec(Node root, int accountId) {
        if (root == null || root.account.accountId == accountId)
            return root == null ? null : root.account;
        if (accountId < root.account.accountId)
            return searchRec(root.left, accountId);
        return searchRec(root.right, accountId);
    }

    public void inOrderTraversal() {
        inOrderRec(root);
    }

    private void inOrderRec(Node root) {
        if (root != null) {
            inOrderRec(root.left);
            System.out.println("AccountId: " + root.account.accountId + ", Name: " + root.account.customerName +
                    ", Branch: " + root.account.branchName + ", Balance: " + root.account.balance);
            inOrderRec(root.right);
        }
    }

    public boolean deposit(int accountId, double amount) {
        BankAccount account = search(accountId);
        if (account != null) {
            account.balance += amount;
            updateFile(); // Dosyayı güncelle
            System.out.println("Updated Account: AccountId: " + account.accountId + ", Name: " + account.customerName +
                    ", Balance: " + account.balance);
            return true;
        }
        return false;
    }

    public boolean withdraw(int accountId, double amount) {
        BankAccount account = search(accountId);
        if (account != null && account.balance >= amount) {
            account.balance -= amount;
            updateFile(); // Dosyayı güncelle
            System.out.println("Updated Account: AccountId: " + account.accountId + ", Name: " + account.customerName +
                    ", Balance: " + account.balance);
            return true;
        }
        return false;
    }

    private void updateFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writeInOrderToFile(root, writer);
        } catch (IOException e) {
            System.out.println("Error updating the file: " + e.getMessage());
        }
    }

    private void writeInOrderToFile(Node root, BufferedWriter writer) throws IOException {
        if (root != null) {
            writeInOrderToFile(root.left, writer);
            writer.write(root.account.accountId + ", " + root.account.customerName + ", " +
                    root.account.branchName + ", " + root.account.balance);
            writer.newLine();
            writeInOrderToFile(root.right, writer);
        }
    }
}

public class BankSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BinarySearchTree bst = new BinarySearchTree();

        loadInitialData(bst);

        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter AccountId:");
                    int accountId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter Customer Name:");
                    String customerName = scanner.nextLine();
                    System.out.println("Enter Branch Name:");
                    String branchName = scanner.nextLine();
                    System.out.println("Enter Initial Balance:");
                    double balance = scanner.nextDouble();
                    bst.insert(accountId, customerName, branchName, balance);
                    break;

                case 2:
                    System.out.println("Enter AccountId to search:");
                    accountId = scanner.nextInt();
                    BankAccount account = bst.search(accountId);
                    if (account != null) {
                        System.out.println("AccountId: " + account.accountId + ", Name: " + account.customerName +
                                ", Branch: " + account.branchName + ", Balance: " + account.balance);
                    } else {
                        System.out.println("Account not found in the binary search tree.");
                    }
                    break;

                case 3:
                    System.out.println("Enter AccountId to deposit:");
                    accountId = scanner.nextInt();
                    System.out.println("Enter Deposit Amount:");
                    double depositAmount = scanner.nextDouble();
                    if (!bst.deposit(accountId, depositAmount)) {
                        System.out.println("Account not found.");
                    }
                    break;

                case 4:
                    System.out.println("Enter AccountId to withdraw:");
                    accountId = scanner.nextInt();
                    System.out.println("Enter Withdrawal Amount:");
                    double withdrawalAmount = scanner.nextDouble();
                    if (!bst.withdraw(accountId, withdrawalAmount)) {
                        System.out.println("Account not found or insufficient balance.");
                    }
                    break;

                case 5:
                    bst.inOrderTraversal();
                    break;

                case 6:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice, please select a valid option.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n--- Bank System ---");
        System.out.println("1. Add New Account");
        System.out.println("2. Search Account");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Display All Accounts");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void loadInitialData(BinarySearchTree bst) {
        String filePath = "C:\\Users\\Oktay Can\\Desktop\\221805001_OktayCanSevimgin\\inputBst.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int accountId = Integer.parseInt(data[0].trim());
                String customerName = data[1].trim();
                String branchName = data[2].trim();
                double balance = Double.parseDouble(data[3].trim());
                bst.insert(accountId, customerName, branchName, balance);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file from: " + filePath);
        }
    }
}
