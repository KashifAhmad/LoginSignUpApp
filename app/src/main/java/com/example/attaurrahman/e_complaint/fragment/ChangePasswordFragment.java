package com.example.attaurrahman.e_complaint.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attaurrahman.e_complaint.R;
import com.example.attaurrahman.e_complaint.configuration.Config;
import com.example.attaurrahman.e_complaint.genralUtils.API;
import com.example.attaurrahman.e_complaint.genralUtils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChangePasswordFragment extends Fragment {

    View parentView;
    String strNewPassword,strConfirmPassword;
    @BindView(R.id.et_newPassword)
    EditText etNewPassword;
    @BindView(R.id.et_forgetPassword_retypePassword)
    EditText etForgetPasswordRetypePassword;
    @BindView(R.id.btn_change_password_submit)
    Button btnChangePasswordSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, parentView);

        btnChangePasswordSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 strNewPassword = etNewPassword.getText().toString();
                 strConfirmPassword = etForgetPasswordRetypePassword.getText().toString();

                if (etNewPassword.getText().equals("") || etNewPassword.getText().length() < 6) {
                    etNewPassword.setError("Please more then 6 digit password");

                } else if (strNewPassword.equals("") || !strNewPassword.equals(strConfirmPassword)) {

                    etNewPassword.setError("Password Not Match");

                } else
                    API.ChangePasswordApi(getActivity(),strNewPassword);


            }
        });


        return parentView;
    }


}
