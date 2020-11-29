package service;

import model.Account;

/**
 * @author lkxed
 */
public interface AccountService {
    Account select(String username);

    void spend(String username, double amount);
}
