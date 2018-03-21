package com.example.attaurrahman.e_complaint.fragment;

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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SignUpVerifyCodeFragment extends Fragment {

    View parentView;
    String strVerifyCode, strNum1, strNum2, strNum3, strNum4, strNum5, strNum6;

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

    @BindView(R.id.btn_verify_submit)Button btnVerifySubmit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_sign_up_verify_code, container, false);

        ButterKnife.bind(this, parentView);



        btnVerifySubmit.setOnClickListener(new View.OnClickListener() {
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
                    API.SingUpVerifyCode(getActivity() ,strVerifyCode);

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


    private void emailVerifyApiCall() {


        String url = "http://techeasesol.com/postcard/PostCard_apis/forgotpassword";

        Utilities.buttonDeclaration(R.id.btn_forgetPassword_submit, parentView).setEnabled(false);

        final StringRequest postRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "verifycode", new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.contains("true")) {
                            Toast.makeText(getActivity(), "Succesfull", Toast.LENGTH_SHORT).show();

                            Utilities.linearLayoutDeclaration(R.id.ll_verify_email, parentView).setVisibility(View.GONE);
                            Utilities.linearLayoutDeclaration(R.id.ll_verify_code, parentView).setVisibility(View.VISIBLE);
                            Utilities.buttonDeclaration(R.id.btn_forgetPassword_submit, parentView).setEnabled(true);


                        } else {
                            Toast.makeText(getActivity(), "Wrong Email", Toast.LENGTH_SHORT).show();
                            Utilities.buttonDeclaration(R.id.btn_forgetPassword_submit, parentView).setEnabled(true);
                        }


                        Log.d("zama respose chnage ", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        Utilities.buttonDeclaration(R.id.btn_forgetPassword_submit, parentView).setEnabled(true);
                        Log.d("zama error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("email",strVerifyCode);


                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(postRequest);

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
