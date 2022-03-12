package com.example.rider.database;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rider.Tools.Tools;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DBConnection {
    private RequestQueue mRequestQueue;
    private JSONObject res;
    public DBConnection(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void postData(final VolleyCallback callback, String operation, HashMap<String, String> data) {
        String file = getFile(operation);
        String url = Tools.IP + file;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccess(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return data;
            }
        };
        mRequestQueue.add(jsonObjectRequest);

    }

    public void getData(final VolleyCallback callback, String operation, HashMap<String, String> data) {
        String file = getFile(operation);
        String url = Tools.IP + file;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccess(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error.toString());
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }

    public void uploadImage(final VolleyCallback callback, String operation,String imagePath) {
        String file = getFile(operation);
        String url = Tools.IP + "upload/driver/"+imagePath ;
        System.out.println(url);
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                // Picture received correctly
                try {
                    callback.onSuccessImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // driverProfile.setImageBitmap(bitmap);//Pictures to be accepted Bitmap Object passed into our imageview Among
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            //The first two parameters of 0 and 0 represent the maximum width and height of the loaded image, and the following parameters Bitmap.Config.RGB_565 Indicates the quality of the picture
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mRequestQueue.add(imageRequest);
    }

    public String setQueryString(HashMap<String, String> data) {
        String queryString = "?";
        int keyCounter = data.size();
        int counter = 0;
        for (String key : data.keySet()) {
            queryString += key + "=" + data.get(key);
            if (keyCounter-counter >1 ) {
                queryString += "&";
            }
            counter++;
        }
        return queryString;
    }

    private String getFile(String operation) {
        String file = "";

                file = Tools.READ_FILE + operation;



        return file;
    }


}
