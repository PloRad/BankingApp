package model;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String  accountName;
    private String  iban;
    private String  currency;
    private long    amount;
    private long    creditLimitAmount;
    private boolean isCreditAccount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;

    public long getCreditLimitAmount() {
        return creditLimitAmount;
    }

    public void setCreditLimitAmount(long creditLimitAmount) {
        this.creditLimitAmount = creditLimitAmount;
    }

    public boolean isCreditAccount() {
        return isCreditAccount;
    }

    public void setCreditAccount(boolean creditAccount) {
        isCreditAccount = creditAccount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void increaseBy(long amount) {
        this.amount += amount;
    }

    public void decreaseBy(long amount) {
        this.amount -= amount;
    }
}
