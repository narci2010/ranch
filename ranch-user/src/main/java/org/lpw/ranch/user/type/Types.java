package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;

/**
 * 认证类型处理器。
 *
 * @author lpw
 */
public interface Types {
    /**
     * 绑定。
     */
    int BIND = 0;
    /**
     * 自有。
     */
    int SELF = 1;
    /**
     * 微信公众号。
     */
    int WEIXIN = 2;
    /**
     * 微信小程序。
     */
    int WEIXIN_MINI = 3;
    /**
     * 类型最大值。
     */
    int MAX = 3;

    /**
     * 获取UID。
     *
     * @param uid      UID。
     * @param password 密码。
     * @param type     类型。
     * @return UID，如果获取失败则返回null。
     */
    String getUid(String uid, String password, int type);

    /**
     * 注册。
     *
     * @param user     用户。
     * @param uid      UID。
     * @param password 密码。
     * @param type     类型。
     */
    void signUp(UserModel user, String uid, String password, int type);

    /**
     * 获取第三方认证昵称。
     *
     * @param uid      UID。
     * @param password 密码。
     * @param type     类型。
     * @return 昵称，不存在则返回null。
     */
    String getNick(String uid, String password, int type);

    /**
     * 获取第三方认证信息。
     *
     * @param uid      UID。
     * @param password 密码。
     * @param type     类型。
     * @return 认证信息，不存在则返回null。
     */
    JSONObject getAuth(String uid, String password, int type);
}
