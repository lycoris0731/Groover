package com.syfm.groover.controller.usecases;

import android.util.Log;

import com.syfm.groover.controller.entities.AppController;
import com.syfm.groover.model.api.ApiClient;
import com.syfm.groover.model.api.LoginApi;
import com.syfm.groover.model.databases.SharedPreferenceHelper;

import org.jdeferred.android.AndroidDeferredManager;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by lycoris on 2015/09/27.
 */
public class LoginUseCase {

    private AndroidDeferredManager deferred = new AndroidDeferredManager();

    public class LoginEvent {
        public final boolean success;

        public LoginEvent(boolean success) {
            this.success = success;
        }
    }

    public void checkLogin(final String serial, final String pass) {
        if (loggedin()) {
            return;
        }
        LoginApi api = new LoginApi(serial, pass);
        deferred.when(() -> {
            try {
                return api.tryLogin();
            } catch (IOException|ApiClient.InvalidResponseException e) {
                Log.d("TryLogin Failed", e.toString());
                return false;
            }
        }).done(loggedin -> {
            // ログインしていたら
            if (loggedin) {
                SharedPreferenceHelper.setLoginInfo(serial, pass);
                EventBus.getDefault().post(new LoginEvent(true));
            } else {
                // ゴミCookieを削除
                AppController.clearCookies();
                EventBus.getDefault().post(new LoginEvent(false));
            }
        }).fail(callback -> {
            Log.d("ktr", "Login failed");
            // ゴミCookieを削除
            AppController.clearCookies();
            EventBus.getDefault().post(new LoginEvent(false));
        });
    }

    private boolean loggedin() {
        return AppController.getInstance().checkLoginCookie();
    }
}
