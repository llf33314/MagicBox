package com.gt.magicbox.login;

/**
 * Description:
 * Created by jack-lin on 2017/7/14 0014.
 */

public interface ILoginPresenter {
    void loginToServer(String userName,String password);
    void loginSucceed();
}
