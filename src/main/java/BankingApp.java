import dao.AccountDao;
import dao.UserDao;
import model.Account;
import model.User;
import service.IOService;

import java.util.List;
import java.util.Random;

public class BankingApp {

    private IOService ioService;
    private UserDao userDao;
    private AccountDao accountDao;

    public BankingApp() {
        ioService = new IOService();
        userDao = new UserDao();
        accountDao  = new AccountDao();
    }

    public void start() {

        while (true) {
            ioService.displayUnauthenticatedUserMenu();
            String userInput = ioService.getUserInput();

            process(userInput);
        }
    }

    private void process(String userInput) {

        if (userInput.equalsIgnoreCase("1")) {
            register();
        } else if (userInput.equalsIgnoreCase("2")) {
            User loggedInUser = login();
            if (loggedInUser != null) {
                loggedInUser.setLoggenIn(true);
                while (loggedInUser.isLoggenIn()) {
                    ioService.displayAuthenticatedUserMenu();
                    userInput = ioService.getUserInput();

                    processLoggedIn(userInput, loggedInUser);
                }
            }
        } else if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("3")) {
            breakOperation();
        }
    }

    private void breakOperation() {
        System.exit(0);
    }

    private void processLoggedIn(String userInput, User user) {
        System.out.println("Processing logged-in user input! ");
        switch (userInput) {
            case "1": {
                showAccounts(user);
                break;
            }
            case "2": {
                transferMoneyFrom(user);
                break;
            }
            case "3": {
                depositCash(user);
                break;
            }
            case "4": {
                createDebitAccountFlow(user);
                break;
            }
            case "5": {
                createCreditAccountFlow(user);
                break;
            }
            case "6": {
                transferMoneyWithinAccountsOf(user);
                break;
            }
            case "0": {
                logout(user);
            }
        }

    }

    private void transferMoneyWithinAccountsOf(User user) {
        // 1- identificam contul sursa din care vrem sa transferam
        ioService.displayAccounts(user.getAccountList());
        String sourceAccountIndex = ioService.getUserInput();

        // 2- introducem suma transferului
        String amountToTransfer = ioService.getField("amount to transfer");

        // 3- introducem IBAN destinatar
        ioService.displayAccounts(user.getAccountList());
        String destinationAccountIndex = ioService.getUserInput();

        // 4- validam IBAN, SUMA, currency
        if(!validateSameUserTransfer(sourceAccountIndex, amountToTransfer, destinationAccountIndex, user.getAccountList())) {
            ioService.displayValidationFieldError();
            return;
        }
        //    4.a- Different currency => INFORM USER ABOUT fees
        // 5- perform transfer
        performTransfer(user, amountToTransfer, sourceAccountIndex, destinationAccountIndex);
    }

    private void performTransfer(User user, String amountToTransfer, String sourceAccountIndex, String destinationAccountIndex) {
        long amount = Long.parseLong(amountToTransfer);
        Account source      = user.getAccountByUserIndex(sourceAccountIndex);
        Account destination = user.getAccountByUserIndex(destinationAccountIndex);

        destination.increaseBy(amount);
        source.decreaseBy(amount);

        userDao.updateEntity(user);
        ioService.displayConfirmation();
    }

    private boolean validateSameUserTransfer(String sourceAccountIndex, String amountToTransfer, String destinationAccountIndex, List<Account> accountList) {
        try {
        int sourceIndex      = Integer.parseInt(sourceAccountIndex);
        int destinationIndex = Integer.parseInt(destinationAccountIndex);
        int transferAmount   = Integer.parseInt(amountToTransfer);


        if(sourceIndex<=0 || sourceIndex>accountList.size() || destinationIndex<=0 || destinationIndex>accountList.size() || sourceIndex==destinationIndex) {
            ioService.errorMessageField("account index");
            return  false;
        }
        Account sourceAccount      = accountList.get(sourceIndex-1);
        Account destinationAccount = accountList.get(destinationIndex-1);

        if (transferAmount > sourceAccount.getAmount() || transferAmount > destinationAccount.getAmount() || transferAmount<0) {
            ioService.errorMessageField("transfer amount");
            return false;
        }

    } catch (Exception exception) {
        System.out.println("Inserted index is not valid (insert numbers with digits between 0-9)");
        return false;
    }
        return true;
    }

    private void createCreditAccountFlow(User user) {
        String accountName     = ioService.getField("account name");
        String currency        = ioService.getField("currency");
        String creditLimit     = ioService.getField("credit limit");
        long creditLimitAsLong = Long.parseLong(creditLimit);

        Account account = new Account();
        account.setAccountName(accountName);
        account.setCurrency(currency);
        account.setAmount(creditLimitAsLong);
        account.setCreditAccount(true);
        account.setCreditLimitAmount(creditLimitAsLong);
        account.setIban(generatedIban());
        account.setUser(user);
        user.addAccount(account);
        accountDao.saveEntity(account);
    }

    private void logout(User user) {
        user.setLoggenIn(false);
        ioService.logoutMessage();
    }

    private void transferMoneyFrom(User user) {
        // 1- identificam contul sursa din care vrem sa transferam
        ioService.displayAccounts(user.getAccountList());
        String userAccountIndex = ioService.getUserInput();

        // 2- introducem suma transferului
        String amountToTransfer = ioService.getField("amount to transfer");

        // 3- introducem IBAN destinatar
        String recipientIban = ioService.getField("recipient IBAN");

        // 4- validam IBAN, SUMA, currency
        if(!validate(userAccountIndex, amountToTransfer, recipientIban, user.getAccountList())) {
            ioService.displayValidationFieldError();
            return;
        }
        //    4.a- Different currency => INFORM USER ABOUT fees
        // 5- perform transfer
        performTransfer(user.getAccountByUserIndex(userAccountIndex), amountToTransfer, recipientIban);
    }

    private void performTransfer(Account sourceAccount, String amountToTransfer, String destinatarIban) {
        Account destinationAccount = accountDao.findByIban(destinatarIban);
        long amount = Integer.parseInt(amountToTransfer);

        destinationAccount.increaseBy(amount);
        sourceAccount.decreaseBy(amount);

        accountDao.updateEntity(destinationAccount, sourceAccount);
        ioService.displayConfirmation();
    }

    private boolean validate(String userAccountIndex, String amountToTransfer, String receiverIban, List<Account> accountList) {
        try {
            int transferAccountIndex = Integer.parseInt(userAccountIndex);
            int transferAmount = Integer.parseInt(amountToTransfer);
            Account accountFromWhichWeTransfer = accountList.get(transferAccountIndex-1) ;

            if(transferAccountIndex<=0 || transferAccountIndex>accountList.size()) {
                ioService.errorMessageField("account index");
                return  false;
            } else if (transferAmount > accountFromWhichWeTransfer.getAmount() || transferAmount<0) {
                ioService.errorMessageField("transfer amount");
                return false;
            }
            Account accountByIban = accountDao.findByIban(receiverIban);
            ioService.errorMessageField("IBAN");
            if(accountByIban == null) {
                return false;
            }

        } catch (Exception exception) {
            System.out.println("Inserted index is not valid (insert numbers with digits between 0-9)");
            return false;
        }
        return true;
    }

    private void showAccounts(User user) {
        ioService.displayAccountInformation(user);
    }

    private void createDebitAccountFlow(User user) {
        String accountName = ioService.getField("account name");
        String currency = ioService.getField("currency");

        Account account = new Account();
        account.setAccountName(accountName);
        account.setCurrency(currency);
        account.setIban(generatedIban());
        account.setUser(user);
        user.addAccount(account);
        accountDao.saveEntity(account);

    }

    private String generatedIban() {
        String iban = "RO12INGB";
        Random random = new Random();
        for(int index = 0; index < 16; index ++){
            iban = iban + random.nextInt(10);
        }
        return iban;
    }

    private void depositCash(User user) {
        ioService.displayAccounts(user.getAccountList());
        String bankAccountIndex = ioService.getUserInput();
        String amount           = ioService.getField("amount");
        System.out.println("User-ul vrea sa introduca " + amount + " in contul de pe indexul " + bankAccountIndex + "-1");
        addAmount(amount, user.getAccountByUserIndex(bankAccountIndex));
    }

    private void addAmount(String amount, Account account) {
        long currentAmount = account.getAmount();
        long newAmount = currentAmount + Integer.parseInt(amount);
        account.setAmount(newAmount);

        accountDao.updateEntity(account);
    }

    private User login() {
        String username = ioService.getField("username");
        String password = ioService.getField("password");
        System.out.println("Trying to log in with: " + username + " & " + password);
        User user = userDao.findByUsername(username);
        if (user == null) {
            System.out.println("Username does not exist! ");
            return null;
        }

        System.out.println(user.getPassword() + " it's the password for user ");
        System.out.println("Username attending log-in using password: " + password);
        if (user.getPassword().equals(password)) {
            System.out.println("You are now loged-in! ");
            return user;
        } else {
            System.out.println("The Username and password are invalid, please try again!");
            return null;

        }
    }

    private void register() {
        String firstName = ioService.getField("first name");
        String lastName = ioService.getField("last name");
        String cnp = ioService.getField("cnp");
        String email = ioService.getField("email");
        String username = ioService.getField("username");
        String password = ioService.getField("password");

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCNP(cnp);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        System.out.println("You have successfully been registered! Please log-in!");
        System.out.println(user);
        userDao.saveEntity(user);

    }
}
