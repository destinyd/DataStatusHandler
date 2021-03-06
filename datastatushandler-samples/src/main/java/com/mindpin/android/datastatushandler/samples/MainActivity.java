package com.mindpin.android.datastatushandler.samples;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import android.view.View;
import android.widget.ImageButton;
import com.github.kevinsawicki.http.HttpRequest;
import com.mindpin.android.datastatushandler.DataState;
import com.mindpin.android.datastatushandler.DataStatusHandler;

import static com.github.kevinsawicki.http.HttpRequest.post;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    DataStatusHandler handler;
    DataState state_followed, state_notfollow;
    ImageButton ib_toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ib_toggle = (ImageButton) findViewById(R.id.ib_toggle);

        handler = new DataStatusHandler();

        state_followed = new DataState("followed", "已关注", R.drawable.unfollow, "http://kc-alpha.4ye.me/api/nets", "GET");
        state_notfollow = new DataState("notfollow", "未关注", R.drawable.follow, "http://kc-alpha.4ye.me/api/nets", "GET");
// 添加状态
        handler.add_state(state_followed);
        handler.add_state(state_notfollow);

        // 设置当前状态
        handler.set_current("followed"); // 设置 name 为 followed 的 State 为当前状态，并不发起 HTTP 请求
        Log.d(TAG, handler.get_current().name); // -> "followed"
        ib_toggle.setImageResource(state_followed.icon_res_id);

        handler.set_toggle_listener(new DataStatusHandler.DataStatusToggleListener() {
            // 请求成功时执行
            public void success(DataStatusHandler handler, DataState state) {
                Log.d(TAG, "success");
                // 请求成功后..
                Log.d(TAG, handler.get_current().name); // -> "notfollow"
                ib_toggle.setImageResource(state.icon_res_id);
            }

            // 请求失败时执行
            public void error(DataStatusHandler handler, DataState state) {
                Log.d(TAG, "error");
            }
        });


        ib_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strings = new String[2];
                strings[0] = "user1";
                strings[1] = "1234";
                new SignInTask().execute(strings);
            }
        });
    }


    String get_login_param() {
        return "user[login]";
    }

    String get_password_param() {
        return "user[password]";
    }

    String cookie = "";

    class SignInTask extends AsyncTask<String, Long, Void> {
        String login, password, result = null;
        boolean isOK;


        @Override
        protected Void doInBackground(String... params) {
            login = params[0];
            password = params[1];
            try {
                HttpRequest request = post("http://kc-alpha.4ye.me/account/sign_in.json").
                        part(get_login_param(), login).part(get_password_param(), password);
                isOK = request.ok();
                if (isOK) {
                    result = request.body();
                    cookie = request.header("Set-Cookie");
                    cookie = cookie.replace("; path=/", "");
                    cookie = cookie.replace("; HttpOnly", "");
                } else {
                    //throw error?
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (isOK) {
                handler.set_cookie(cookie);
                DataState current = handler.get_current();
                if (current.name.equals("notfollow"))
                    handler.toggle("followed"); // 发起 HTTP 请求，如果请求成功，则切换状态到 followed
                else
                    handler.toggle("notfollow"); // 发起 HTTP 请求，如果请求成功，则切换状态到 notfollow
            }
        }
    }
}
