package com.example.attaurrahman.e_complaint.fragment;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.attaurrahman.e_complaint.activity.MainActivity;
import com.example.attaurrahman.e_complaint.configuration.CheckNetwork;
import com.example.attaurrahman.e_complaint.genralUtils.API;
import com.example.attaurrahman.e_complaint.genralUtils.Utilities;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.facebook.login.Login;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.example.attaurrahman.e_complaint.configuration.Config.BASE_URL;


public class RegistrationTabFragment extends Fragment {

    @RequiresApi(api = Build.VERSION_CODES.M)


    @BindView(R.id.et_register_fullname)
    EditText etRegisterFullName;
    @BindView(R.id.et_registration_email)
    EditText etRegistrationEmail;
    @BindView(R.id.et_registration_password)
    EditText etRegistrationPassword;
    @BindView(R.id.et_registration_retypePassword)
    EditText etRetypeRegistrationPassword;

    @BindView(R.id.tv_login)
    TextView tvLogIn;
    @BindView(R.id.btn_register)
    Button btnRegister;
    View parentView;

    String strRegFullName, strRegEmail, strRegPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, parentView);



        tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.connectFragmentWithOutBackStack(getActivity(), new LoginTabFragment());
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (!CheckNetwork.isInternetAvailable(getActivity())) {

                    new CDialog(getActivity()).createAlert("No Internet Connection",
                            CDConstants.WARNING,   // Type of dialog
                            CDConstants.LARGE)    //  size of dialog
                            .setAnimation(CDConstants.SCALE_TO_TOP_FROM_BOTTOM)     //  Animation for enter/exit
                            .setDuration(5000)   // in milliseconds
                            .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                            .show();
                } else {

                    String strNewPassword = etRegistrationPassword.getText().toString();
                    String strConforimPassword = etRetypeRegistrationPassword.getText().toString();
                    if (etRegisterFullName.getText().length() <= 2) {
                        etRegisterFullName.setError("Enter Full Name");

                    } else if (etRegistrationEmail.getText().length() <= 0) {
                        etRegistrationEmail.setError("Enter Email");

                    } else if (!Utilities.isValidEmail(etRegistrationEmail.getText())) {
                        etRegistrationEmail.setError("Correct Format Email");
                    } else if (strNewPassword.length() <= 6) {
                        etRegistrationPassword.setError("Please more then 6 digit password");

                    } else if (strNewPassword.equals(strConforimPassword)) {


                        strRegFullName = etRegisterFullName.getText().toString();
                        strRegEmail = etRegistrationEmail.getText().toString();
                        strRegPassword = etRetypeRegistrationPassword.getText().toString();

                        API.RegisterApiCall(getActivity(),strRegFullName,strRegEmail,strRegPassword);


                    } else etRetypeRegistrationPassword.setError("Password does'nt match ");
                }

            }
        });


        etRetypeRegistrationPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String strNewPassword = etRegistrationPassword.getText().toString();
                    String strConforimPassword = etRetypeRegistrationPassword.getText().toString();
                    if (etRegisterFullName.getText().length() <= 2) {
                        etRegisterFullName.setError("Enter Full Name");

                    } else if (etRegistrationEmail.getText().length() <= 0) {
                        etRegistrationEmail.setError("Enter Email");

                    } else if (!Utilities.isValidEmail(etRegistrationEmail.getText())) {
                        etRegistrationEmail.setError("Correct Format Email");
                    } else if (strNewPassword.length() <= 6) {
                        etRegistrationPassword.setError("Please more then 6 digit password");

                    } else if (strNewPassword.equals(strConforimPassword)) {

                        strRegFullName = etRegisterFullName.getText().toString();
                        strRegEmail = etRegistrationEmail.getText().toString();
                        strRegPassword = etRetypeRegistrationPassword.getText().toString();

                        API.RegisterApiCall(getActivity(),strRegFullName,strRegEmail,strRegPassword);


                    } else etRetypeRegistrationPassword.setError("Password does'nt match ");

                    handled = true;
                }
                return handled;
            }
        });

        return parentView;
    }





}
