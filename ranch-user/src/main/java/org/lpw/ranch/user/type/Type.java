package org.lpw.ranch.user.type;

import org.lpw.ranch.user.UserModel;

/**
 * 认证类型。
 *
 * @author lpw
 */
public interface Type {
    /**
     * 获取类型KEY值。
     *
     * @return 类型KEY值。
     */
    int getKey();

    /**
     * 获取UID。
     *
     * @param uid      UID。
     * @param password 密码。
     * @return UID，如果获取失败则返回null。
     */
    String getUid(String uid, String password);

    /**
     * 注册。
     *
     * @param user     用户。
     * @param uid      UID。
     * @param password 密码。
     */
    void signUp(UserModel user, String uid, String password);
}