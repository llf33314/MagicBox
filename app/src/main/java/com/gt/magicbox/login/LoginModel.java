package com.gt.magicbox.login;

/**
 * Description:
 * Created by jack-lin on 2017/7/14 0014.
 */

public class LoginModel implements ILoginModel{
    private ILoginPresenter presenter;
    public LoginModel(ILoginPresenter presenter){
        this.presenter=presenter;
    }
    @Override
    public void login(String name, String password) {
        presenter.loginSucceed();
    }
}
