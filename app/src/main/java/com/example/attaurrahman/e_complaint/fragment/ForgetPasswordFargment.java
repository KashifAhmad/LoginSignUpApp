package com.example.attaurrahman.e_complaint.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.attaurrahman.e_complaint.R;
import com.example.attaurrahman.e_complaint.configuration.CheckNetwork;
import com.example.attaurrahman.e_complaint.configuration.Config;
import com.example.attaurrahman.e_complaint.genralUtils.API;
import com.example.attaurrahman.e_complaint.genralUtils.Utilities;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgetPasswordFargment extends Fragment {

    View parentView;
    String strVerifyCode, strNum1, strNum2, strNum3, strNum4, strNum5, strNum6,strEmailVerify;

    @BindView(R.id.et_code_num1)
    EditText etCodeNum1;
    @BindView(R.id.et_code_num2)
    EditText etCodeNum2;
    @BindView(R.id.et_code_num3)
    EditText etCodeNum3;
    @BindView(R.id.et_code_num4)
    EditText etCodeNum4;
    @BindView(R.id.et_code_num5)
    EditText etCodeNum5;
    @BindView(R.id.et_code_num6)
    EditText etCodeNum6;
    @BindView(R.id.et_email_verify)
    EditText etEmailVerify;
    @BindView(R.id.btn_forgetPassword_submit)
    Button btnForgetPasswordSubmit;
    @BindView(R.id.btn_forget_verify_submit)
    Button btnForgetVerifySubmit;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_forget_password_fargment, container, false);

        ButterKnife.bind(this, parentView);

        btnForgetPasswordSubmit.setOnClickListener(new View.OnClickListener() {
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

                    if (etEmailVerify.getText().length() <= 1) {

                        etEmailVerify.setError("Enter Email");

                    } else if (!Utilities.isValidEmail(etEmailVerify.getText())) {

                        etEmailVerify.setError("Correct Format Email");

                    } else
                        strEmailVerify = etEmailVerify.getText().toString();
                        API.EmailVerifyApiCall(parentView,getActivity(), strEmailVerify);
                }
            }
        });

        btnForgetVerifySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strNum1 = etCodeNum1.getText().toString();
                strNum2 = etCodeNum2.getText().toString();
                strNum3 = etCodeNum3.getText().toString();
                strNum4 = etCodeNum4.getText().toString();
                strNum5 = etCodeNum5.getText().toString();
                strNum6 = etCodeNum6.getText().toString();

                strVerifyCode = strNum1 + strNum2 + strNum3 + strNum4 + strNum5 + strNum6;

                if (strVerifyCode.length() == 6) {
                    API.SingUpVerifyCode(getActivity(), strVerifyCode);

                } else {
                    Toast.makeText(getActivity(), "Fill Verify Code", Toast.LENGTH_SHORT).show();
                }


            }
        });


        etCodeNum1.addTextChangedListener(genraltextWatcher);
        etCodeNum2.addTextChangedListener(genraltextWatcher);
        etCodeNum3.addTextChangedListener(genraltextWatcher);
        etCodeNum4.addTextChangedListener(genraltextWatcher);
        etCodeNum5.addTextChangedListener(genraltextWatcher);
        etCodeNum6.addTextChangedListener(genraltextWatcher);




        return parentView;
    }





    private TextWatcher genraltextWatcher = new TextWatcher() {
        private View view;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (etCodeNum1.length() == 1) {

                etCodeNum2.requestFocus();

            }
            if (etCodeNum2.length() == 1) {

                etCodeNum3.requestFocus();

            }
            if (etCodeNum3.length() == 1) {

                etCodeNum4.requestFocus();

            }
            if (etCodeNum4.length() == 1) {

                etCodeNum5.requestFocus();

            }
            if (etCodeNum5.length() == 1) {

                etCodeNum6.requestFocus();
            }
            if (etCodeNum6.length() == 1) {

            }


        }

        @Override
        public void afterTextChanged(Editable editable) {


        }

    };


}
