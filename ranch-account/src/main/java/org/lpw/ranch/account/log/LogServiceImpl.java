package org.lpw.ranch.account.log;

import org.lpw.ranch.account.AccountModel;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(LogModel.NAME + ".service")
public class LogServiceImpl implements LogService {
    @Inject
    private DateTime dateTime;
    @Inject
    private LogDao logDao;

    @Override
    public void create(AccountModel account, String type, int amount, State state) {
        LogModel log = new LogModel();
        log.setUser(account.getUser());
        log.setAccount(account.getId());
        log.setType(type);
        log.setAmount(amount);
        log.setBalance(account.getBalance());
        log.setState(state.ordinal());
        log.setTime(dateTime.now());
        logDao.save(log);
    }
}