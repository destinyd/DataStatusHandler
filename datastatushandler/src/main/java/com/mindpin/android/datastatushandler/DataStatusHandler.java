package com.mindpin.android.datastatushandler;

import android.os.AsyncTask;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;

/**
 * Created by dd on 14-6-30.
 */
public class DataStatusHandler {
    HashMap<String, DataState> status = new HashMap<String, DataState>();
    DataState currentState = null;
    DataState targetState = null;

    DataStatusToggleListener mDataStatusToggleListener = null;
    String cookie = "";

    public interface DataStatusToggleListener {
        // 请求成功时执行
        void success(DataStatusHandler handler, DataState state);

        // 请求失败时执行
        void error(DataStatusHandler handler, DataState state);
    }

    public void add_state(DataState dataState) {
        status.put(dataState.name, dataState);
    }

    public void set_current(String dataStateName) {
        if (in_status(dataStateName))
            currentState = status.get(dataStateName);
    }

    public String get_current() {
        if (currentState != null)
            return currentState.name;
        return null;
    }

    public void toggle(String dataStateName) {
        DataState dataState = null;
        if (in_status(dataStateName)) {
            dataState = status.get(dataStateName);
            targetState = dataState;
            handle(dataState);
        } else {
            error(dataState);
        }
    }

    public void set_cookie(String cookie) {
        this.cookie = cookie;
    }

    public void set_toggle_listener(DataStatusToggleListener listener) {
        mDataStatusToggleListener = listener;
    }

    //private
    private void handle(DataState dataState) {
        HttpRequest request = new HttpRequest(dataState.url, dataState.method);
        request.header("Cookie", cookie);
        new RequestTask().execute(request);
    }

    private boolean in_status(String dataStateName) {
        if (status.containsKey(dataStateName)) {
            return true;
        } else {
            throw new Error("no this state");
        }
    }

    private void success() {
        if (mDataStatusToggleListener != null) {
            mDataStatusToggleListener.success(this, targetState);
        }
    }

    private void error(DataState dataState) {
        if (mDataStatusToggleListener != null) {
            mDataStatusToggleListener.error(this, dataState);
        }
    }

    private class RequestTask extends AsyncTask<HttpRequest, Long, Boolean> {
        HttpRequest request;

        @Override
        protected Boolean doInBackground(HttpRequest... params) {
            if (params.length > 0) {
                request = params[0];
                return request.ok();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                currentState = targetState;
                success();
            } else {
                error(targetState);
            }
        }
    }
}
