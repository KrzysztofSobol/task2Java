package demo.task1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class BankImpl implements Bank {

    private AccountRepository accountRepository;

    public BankImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Long createAccount(String name, String address) {
        Optional<Account> account = accountRepository.findByNameAndAddress(name, address);
        if (account.isPresent()) { return account.get().getId(); }
        return accountRepository.create(name,address,BigDecimal.ZERO).getId();
    }

    @Override
    public Long findAccount(String name, String address) {
        Optional<Account> account = accountRepository.findByNameAndAddress(name, address);
        return account.isPresent() ? account.get().getId() : null;
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        try {
            Optional<Account> account = accountRepository.findById(id);
            if(account.isEmpty()) {
                throw new AccountIdException();
            }

            Account ac = account.get();

            if(amount == null){
                amount = BigDecimal.ZERO;
            }

            ac.setBalance(ac.getBalance().add(amount));
            accountRepository.save(ac);
        } catch(IllegalArgumentException e) {
            throw new AccountIdException();
        }
    }

    @Override
    public BigDecimal getBalance(Long id) {
        try {
            Optional<Account> account = accountRepository.findById(id);
            if(account.isEmpty()) {
                throw new AccountIdException();
            }

            return account.get().getBalance();
        } catch (IllegalArgumentException e) {
            throw new AccountIdException();
        }
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        try {
            Optional<Account> account = accountRepository.findById(id);
            if(account.isEmpty()) {
                throw new AccountIdException();
            }

            Account ac = account.get();
            BigDecimal currentBalance = ac.getBalance();

            if(amount == null){
                amount = BigDecimal.ZERO;
            }

            if(currentBalance.compareTo(amount) < 0) {
                throw new InsufficientFundsException();
            }

            ac.setBalance(currentBalance.subtract(amount));
            accountRepository.save(ac);
        } catch (IllegalArgumentException e) {
            throw new AccountIdException();
        }
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) {
        try {
            Optional<Account> sourceAccount = accountRepository.findById(idSource);
            Optional<Account> destinationAccount = accountRepository.findById(idDestination);

            if(sourceAccount.isEmpty() || destinationAccount.isEmpty()) {
                throw new AccountIdException();
            }

            Account sourceAc = sourceAccount.get();
            Account destAc = destinationAccount.get();

            if(sourceAc.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException();
            }

            sourceAc.setBalance(sourceAc.getBalance().subtract(amount));
            destAc.setBalance(destAc.getBalance().add(amount));

            accountRepository.save(sourceAc);
            accountRepository.save(destAc);
        } catch (IllegalArgumentException e) {
            throw new AccountIdException();
        }
    }
}
