package org.litespring.service.v3;

import org.litespring.dao.v3.AccountDAO;
import org.litespring.dao.v3.ItemDAO;

public class PetStoreService {
    private AccountDAO accountDao;
    private ItemDAO itemDao;
    private int version;

    public PetStoreService(AccountDAO accountDao, ItemDAO itemDao) {
        this(accountDao, itemDao, -1);
    }

    public PetStoreService(AccountDAO accountDao, ItemDAO itemDao, int version) {
        this.accountDao = accountDao;
        this.itemDao = itemDao;
        this.version = version;
    }

    public AccountDAO getAccountDao() {
        return accountDao;
    }

    public ItemDAO getItemDao() {
        return itemDao;
    }

    public int getVersion() {
        return version;
    }
}
