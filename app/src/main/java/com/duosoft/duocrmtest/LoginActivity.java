/*
 * Copyright (C) 2017, Duo Software Pvt Ltd.
 *
 * Licensed under the Duo Software;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.duosoftworld.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.duosoft.duocrmtest;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.duosoft.duocrmtest.activity.BaseActivity;
import com.duosoft.duocrmtest.activity.TicketActivity;
import com.duosoft.duocrmtest.common.webevents.UserLoginMessage;
import com.duosoft.duocrmtest.model.request.UserLoginRequest;
import com.duosoft.duocrmtest.model.response.UserLoginResponse;
import com.duosoft.duocrmtest.network.DuoCrmAuthService;
import com.duosoft.duocrmtest.util.AlertUtil;
import com.duosoft.duocrmtest.util.Preference;
import com.duosoft.duocrmtest.util.Validation;

import org.json.JSONObject;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by mihira on 6/19/17.
 * <p>
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends BaseActivity implements UserLoginMessage, View.OnClickListener {

    private static final String TAG = "LoginActivity";
    JSONObject loginJson;
    boolean success = false;

    // UI references.
    private EditText etUserName;
    private EditText etPassword;
    private Button mSignInButton;
    private CoordinatorLayout coordinatorLayout;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    // UI initialization
    private void init() {

        // Set up the login form.
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        mSignInButton = (Button) findViewById(R.id.btn_login);
        mSignInButton.setOnClickListener(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        etUserName.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !Validation.isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a empty user name.
        if (TextUtils.isEmpty(username)) {
            etUserName.setError(getString(R.string.error_field_required));
            focusView = etUserName;
            cancel = true;
        }

        // Check for a valid user name.
        if (!TextUtils.isEmpty(username) && !Validation.isValidEmail(username)) {
            etUserName.setError(getString(R.string.error_invalid_username));
            focusView = etUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            viewSubscriberLogin();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.btn_login:
                attemptLogin();
                break;
        }
    }

    private boolean viewSubscriberLogin() {

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.userName = etUserName.getText().toString().trim();
        loginRequest.password = etPassword.getText().toString();

        loginRequest.scope = "all_all";
        loginRequest.console = "AGENT_CONSOLE";
        loginRequest.clientID = "e8ea7bb0-5026-11e7-a69b-b153a7c332b9";

        try {

            Observable<UserLoginResponse> loginResponseObservable = DuoCrmAuthService.getInstance().userLoginRequest(loginRequest);

            this.subscription = loginResponseObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserLoginResponse>() {
                        @Override
                        public void onCompleted() {

                            Log.d(TAG, "onCompleted");

                        }

                        @Override
                        public void onError(Throwable e) {
                            // failed login
                            onFailureLogin(e);
                        }

                        @Override
                        public void onNext(UserLoginResponse userLoginResponse) {
                            // success login
                            onSuccessLogin(userLoginResponse);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * Login success response
     *
     * @param userLoginResponse
     */
    @Override
    public void onSuccessLogin(UserLoginResponse userLoginResponse) {
        try {
            Log.d(TAG, "TOKEN : " + userLoginResponse.token);
            Log.d(TAG, "STATE : " + userLoginResponse.state);
            Preference.savePreference(getResources().getString(R.string.sp_login_token), userLoginResponse.token, LoginActivity.this);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        navigateActivity(TicketActivity.class);
        finish();
    }

    /**
     * Login failure response
     *
     * @param e
     */
    @Override
    public void onFailureLogin(Throwable e) {
        if (e instanceof HttpException) {
            HttpException response = (HttpException) e;
            Log.d("login", String.valueOf(response.code()));

            if (response.code() == 401) {
                AlertUtil.showSnackBarMessage(
                        getResources().getString(R.string.error_loginpage_unabletologin)
                        , coordinatorLayout);
            } else {
                AlertUtil.showSnackBarMessage(
                        getResources().getString(R.string.error_loginpage_loginfailed)
                        , coordinatorLayout);
            }
            success = false;
        }
    }
}
