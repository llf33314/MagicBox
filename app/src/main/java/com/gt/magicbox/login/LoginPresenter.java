package com.gt.magicbox.login;

/**
 * Description:
 * Created by jack-lin on 2017/7/14 0014.
 */

public class LoginPresenter implements ILoginPresenter {
    private ILoginModel loginModel;
    private ILoginView loginView;
    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModel(this);
    }
    @Override
    public void loginToServer(String userName, String password) {
        loginView.showProgress(true);
        loginModel.login(userName,password);
    }

    @Override
    public void loginSucceed() {
        loginView.showProgress(false);
        loginView.showLoginView();
    }
}
