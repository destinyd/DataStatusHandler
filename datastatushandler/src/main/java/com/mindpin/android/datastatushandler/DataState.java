package com.mindpin.android.datastatushandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-6-30.
 */
public class DataState {
    //in(GET, POST, PUT, DELETE)
    public String method;
    public String name, desc, url;
    public int icon_res_id;
    List<String> _methods = null;

    public DataState(String name, String desc, int icon_res_id, String url, String method) {
        this.name = name;
        this.desc = desc;
        this.icon_res_id = icon_res_id;
        this.url = url;
        if (in_methods(method))
            this.method = method;
        else
            throw new Error("Not RESTful");
    }

    private List<String> getMethods() {
        if (_methods == null) {
            _methods = new ArrayList<String>();
            _methods.add("GET");
            _methods.add("POST");
            _methods.add("PUT");
            _methods.add("DELETE");
        }
        return _methods;
    }

    private boolean in_methods(String method) {
        return getMethods().contains(method);
    }
}
