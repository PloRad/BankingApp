package service;
import model.Account;
import model.User;

import java.util.List;
import java.util.Scanner;

public class IOService {

    private Scanner scanner = new Scanner(System.in);

    public void displayUnauthenticatedUserMenu(){

        System.out.println("Hello, please type one of the following options: ");
        System.out.println("1 - Register ");
        System.out.println("2 - Login ");
        System.out.println("3 - Exit Application ");

        System.out.println("Your answer: ");

    }

    public String getUserInput(){
        return scanner.nextLine();
    }

    public String getField(String field) {
        System.out.print("Please insert "+ field + ": ");
        return scanner.nextLine();
    }

    public void displayAccounts(List<Account> accounts) {
        System.out.println("\nPlease select one of the following accounts: ");
        for (int index = 0; index < accounts.size(); index++) {
            System.out.println((index+1) + " - " + accounts.get(index).getAccountName());
        }
        System.out.print("\nEnter selection: ");
    }

    public void displayAuthenticatedUserMenu() {
        System.out.println("\nPlease select one of the following options: ");

        System.out.println("1 - View portfolio and balance ");
        System.out.println("2 - Transfer money ");
        System.out.println("3 - Deposit cash at ATM ");
        System.out.println("4 - Create debit account ");
        System.out.println("5 - Create credit account ");
        System.out.println("6 - Transfer money between your accounts ");
        System.out.println("0 - Logout ");

        System.out.println("Your answer: ");
    }

    public void displayAccountInformation(User user) {
        System.out.println("\nAccount information for user: " + user.getFullName() + "\n---------------------------------------------------------");
        System.out.println("Account name\tAmount\t\tIBAN\t\t\t\t\t\tType");

        for(Account account: user.getAccountList()) {
            String nameSpace = "\t\t\t";
            String amountSpace = "\t\t";
            String accountType = account.isCreditAccount()?"credit":"debit";   //conditie ? -> daca este true, prima conditie ------ daca nu conditia 2
            if(account.getAccountName().length() > 7){
                nameSpace = "\t\t";
            } else if(String.valueOf(account.getAmount()).length()<2) {
                amountSpace = "\t\t\t";
            }
            System.out.println(account.getAccountName() + nameSpace + account.getAmount() + amountSpace + account.getIban() + "\t" + accountType);
        }
        System.out.println("------------------------------------------------------\n");
    }

    public void displayValidationFieldError() {
        System.out.println("One or more conditions are not met. Transfer cancelled.");
    }

    public void errorMessageField(String fieldName) {
        System.out.println("Operation failed!\n---The " + fieldName + " is not valid---");
    }

    public void displayConfirmation() {
        System.out.println("SUCCESS = = = = = = = = = = = = = = =");
    }

    public void logoutMessage() {
        System.out.println("---------Logged Out---------");
        System.out.println("      Have a nice Day       ");
    }
}
