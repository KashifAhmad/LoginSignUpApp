package com.example.attaurrahman.e_complaint.genralUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attaurrahman.e_complaint.R;
import com.example.attaurrahman.e_complaint.configuration.Config;
import com.example.attaurrahman.e_complaint.fragment.Complaint_Fragment;
import com.example.attaurrahman.e_complaint.fragment.LoginTabFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.example.attaurrahman.e_complaint.configuration.Config.BASE_URL;

/**
 * Created by AttaUrRahman on 3/20/2018.
 */

public class API {



    private static AlertDialog alertDialogSpot;

    public static void SingUpVerifyCode(final Context context, final String strVerifyCode) {


        final StringRequest postRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "verifycode", new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("true")) {

                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();

                            Utilities.connectFragment(context, new LoginTabFragment());


                        } else {
                            Toast.makeText(context, "wrong number", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("code", strVerifyCode);


                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);


    }


    public static void RegisterApiCall(final Context context, final String strRegFullName, final String strRegEmail, final String strRegPassword) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL + "sign_up",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Verification code is sent to you via email.")) {

                            Toast.makeText(context, "Succesfull", Toast.LENGTH_SHORT).show();
                            Log.d("zama response", response.toString());
                            Utilities.connectFragmentForMyComplaint(context, new Complaint_Fragment());
                            Utilities.putValueInEditor(context).putBoolean("isLogin", true).commit();
                        } else if (response.contains("Already Registered")) {
                            Toast.makeText(context, "Already Registerd", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();

                        NetworkResponse networkResponse = error.networkResponse;

                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout";

                            } else if (error.getClass().equals(NoConnectionError.class)) {

                                errorMessage = "Failed to connect server";
                            }
                        } else {
                            String result = new String(networkResponse.data);
                            try {
                                JSONObject response = new JSONObject(result);
                                String status = response.getString("status");
                                String message = response.getString("message");

                                Log.e("Error Status", status);
                                Log.e("Error Message", message);

                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";


                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " Please login again";


                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";


                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("Error", errorMessage);
                        error.printStackTrace();
                    }
                }
        ) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("fullname", strRegFullName.toString());
                params.put("username", "user name");
                params.put("email", strRegEmail.toString());
                params.put("gender", "male");
                params.put("password", strRegPassword.toString());


                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

    }


    public static void RegisterSocialApiCall(final Context context, final String strPersonFullname, final String strProvider, final String strPersonUsername, final String strPersonEmail) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL + "sociallogin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if (response.contains("200")) {

                            Toast.makeText(context, "Succesfull", Toast.LENGTH_SHORT).show();
                            Log.d("zama response", response.toString());
                            Utilities.connectFragmentForMyComplaint(context, new Complaint_Fragment());
                            Utilities.putValueInEditor(context).putBoolean("isLogin", true).commit();

                        } else if (response.contains("202")) {
                            Toast.makeText(context, "Already Registerd", Toast.LENGTH_SHORT).show();
                            Utilities.connectFragmentWithOutBackStack(context, new Complaint_Fragment());
                            Utilities.putValueInEditor(context).putBoolean("isLogin", true).commit();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();

                        NetworkResponse networkResponse = error.networkResponse;

                        String errorMessage = "Unknown error";
                        if (networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                errorMessage = "Request timeout";

                            } else if (error.getClass().equals(NoConnectionError.class)) {

                                errorMessage = "Failed to connect server";
                            }
                        } else {
                            String result = new String(networkResponse.data);
                            try {
                                JSONObject response = new JSONObject(result);
                                String status = response.getString("status");
                                String message = response.getString("message");

                                Log.e("Error Status", status);
                                Log.e("Error Message", message);

                                if (networkResponse.statusCode == 404) {
                                    errorMessage = "Resource not found";


                                } else if (networkResponse.statusCode == 401) {
                                    errorMessage = message + " Please login again";


                                } else if (networkResponse.statusCode == 400) {
                                    errorMessage = message + " Check your inputs";


                                } else if (networkResponse.statusCode == 500) {
                                    errorMessage = message + " Something is getting wrong";

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("Error", errorMessage);
                        error.printStackTrace();
                    }
                }
        ) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("fullname", strPersonFullname.toString());
                params.put("provider", strProvider);
                params.put("provider_id", Utilities.getSharedPreferences(context).getString("android_id", ""));
                params.put("name", strPersonUsername);
                params.put("device_type", "android");
                params.put("device_token", "12345");
                params.put("email", strPersonEmail);
                params.put("gender", Utilities.getSharedPreferences(context).getString("gender", ""));


                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

    }

    public static void LoginApiCall(final Context context, final String strLogInEmail, final String strLogInPassword) {
        alertDialogSpot = new SpotsDialog(context, R.style.CustomLoading);

        alertDialogSpot.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, BASE_URL + "login", new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("true")) {

                            Toast.makeText(context, "Succesfull", Toast.LENGTH_SHORT).show();


                            Utilities.putValueInEditor(context).putBoolean("isLogin", true).commit();

                            Log.d("zma res log", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                                jsonObject1.getString("id");


                                // editor.putString("user_id", jsonObject1.getString("id")).commit();
                                //  Toast.makeText(getActivity(),  jsonObject1.getString("id"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Utilities.connectFragmentForMyComplaint(context, new Complaint_Fragment());
                            alertDialogSpot.dismiss();
                        } else {
                            Toast.makeText(context, "Wrong credentials", Toast.LENGTH_SHORT).show();
                            alertDialogSpot.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                        alertDialogSpot.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("identity", strLogInEmail);
                params.put("password", strLogInPassword);

                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);


    }
    public static void ChangePasswordApi(final Context context, final String strNewPassword) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"resetpassword", new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response).getJSONObject("data");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "Succesfull", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                //params.put("code", code);
                params.put("password", strNewPassword);


                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

    }

    public static void EmailVerifyApiCall(final View view, final Context context, final String strVerifyEmail) {

        Utilities.buttonDeclaration(R.id.btn_forgetPassword_submit,view).setEnabled(false);

        final StringRequest postRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+"forgotpassword", new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("true")) {
                            Toast.makeText(context, "Succesfull", Toast.LENGTH_SHORT).show();
                            Utilities.linearLayoutDeclaration(R.id.ll_verify_email,view).setVisibility(View.GONE);
                            Utilities.linearLayoutDeclaration(R.id.ll_verify_code,view).setVisibility(View.VISIBLE);
                            Utilities.buttonDeclaration(R.id.btn_forgetPassword_submit,view).setEnabled(true);

                        } else {
                            Toast.makeText(context, "Wrong Email", Toast.LENGTH_SHORT).show();
                            Utilities.buttonDeclaration(R.id.btn_forgetPassword_submit, view).setEnabled(true);
                        }
                        Log.d("zama respose chnage ", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        Utilities.buttonDeclaration(R.id.btn_forgetPassword_submit,view).setEnabled(true);
                       // btnForgetPasswordSubmit.setEnabled(true);
                        Log.d("zama error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("email", strVerifyEmail);


                return params;
            }
        };
        Volley.newRequestQueue(context).add(postRequest);

    }



}
