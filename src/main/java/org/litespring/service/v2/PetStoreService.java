package org.litespring.service.v2;

import org.litespring.dao.v2.AccountDAO;
import org.litespring.dao.v2.ItemDAO;

public class PetStoreService {
    private AccountDAO accountDao;
    private ItemDAO itemDao;
    private String owner;
    private int version;

    public ItemDAO getItemDao() {
        return itemDao;
    }

    public void setItemDao(ItemDAO itemDao) {
        this.itemDao = itemDao;
    }


    public AccountDAO getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(AccountDAO accountDao) {
        this.accountDao = accountDao;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
