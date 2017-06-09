package org.lpw.ranch.account.type;

/**
 * @author lpw
 */
public interface AccountTypes {
    /**
     * 存入。
     */
    String DEPOSIT = "deposit";
    /**
     * 取出。
     */
    String WITHDRAW = "withdraw";
    /**
     * 奖励。
     */
    String REWARD = "reward";
    /**
     * 盈利。
     */
    String PROFIT = "profit";
    /**
     * 消费。
     */
    String CONSUME = "consume";

    /**
     * 获取账户操作类型。
     *
     * @param name 类型名称。
     * @return 操作类型。
     */
    AccountType get(String name);
}