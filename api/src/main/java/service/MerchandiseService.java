package service;

import model.Merchandise;

/**
 * @author lkxed
 */
public interface MerchandiseService {
    Merchandise select(String name);

    void sell(String name, int number);
}
