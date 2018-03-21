package com.example.attaurrahman.e_complaint.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.attaurrahman.e_complaint.R;
import com.example.attaurrahman.e_complaint.activity.MainActivity;
import com.example.attaurrahman.e_complaint.configuration.CheckNetwork;
import com.example.attaurrahman.e_complaint.genralUtils.API;
import com.example.attaurrahman.e_complaint.genralUtils.Utilities;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;


public class LoginTabFragment extends Fragment implements View.OnClickListener{

    View parentView;
    @BindView(R.id.et_login_email)EditText etLogInEmail;
    @BindView(R.id.et_login_password)EditText etLogInPassword;
    @BindView(R.id.tv_register)TextView tvRegister;
    @BindView(R.id.tv_forgetPassword)TextView tvForgetPassword;
    @BindView(R.id.btn_login)Button btnLogIn;
    @BindView(R.id.btn_facebook)Button btnFacebook;
    @BindView(R.id.btn_google)Button btnGoogle;
    @BindView(R.id.btn_login_button)LoginButton btnFbLogIn;

    AlertDialog alertDialogSpot;
    private static final int RC_SIGN_IN = 1;

    GoogleSignInClient mGoogleSignInClient;

    String strPersonFullname,strPersonEmail,strPersonUsername,strPersonGender,strProvider;
    String strLoginEmail,strLogInPassword;

    AlertDialog alertDialog1;
    CharSequence[] values = {"Male"," Female"};




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_login_tab, container, false);
        ButterKnife.bind(this, parentView);

        alertDialogSpot  = new SpotsDialog(getActivity(),R.style.CustomLoading);


        tvRegister.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);

        etLogInEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etLogInEmail.setFocusableInTouchMode(true);
                etLogInPassword.setFocusableInTouchMode(true);
                return false;
            }
        });

        btnFbLogIn.setReadPermissions(Arrays.asList("email"));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUI(account);




        return parentView;
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()){

            case R.id.btn_login:
                if (!CheckNetwork.isInternetAvailable(getActivity())) {
                    noInternet();
                } else {
                    if (etLogInEmail.getText().length() <= 1) {
                        Toast.makeText(getActivity(), "Enter Your Email", Toast.LENGTH_SHORT).show();

                    } else if (!Utilities.isValidEmail(etLogInEmail.getText())) {
                        Toast.makeText(getActivity(), "Correct Format Email", Toast.LENGTH_SHORT).show();
                    } else


                    {
                        strLoginEmail = etLogInEmail.getText().toString();
                        strLogInPassword = etLogInPassword.getText().toString();
                        API.LoginApiCall(getActivity(),strLoginEmail,strLogInPassword);
                    }

                }

                break;

            case R.id.tv_register:

                Utilities.connectFragmentWithOutBackStack(getActivity(), new RegistrationTabFragment());
                break;

            case R.id.tv_forgetPassword:
                Utilities.connectFragment(getActivity(),new ForgetPasswordFargment());
                break;
            case R.id.btn_facebook:
                if (!CheckNetwork.isInternetAvailable(getActivity())) {
                    noInternet();
                }
                else {
                    strProvider = "facebook";
                    btnFbLogIn.performClick();
                    ((MainActivity) getActivity()).facebook();
                }
                break;
            case R.id.btn_google:

                if (!CheckNetwork.isInternetAvailable(getActivity())) {

                    noInternet();
                } else {
                    strProvider = "google";
                    CreateAlertDialogWithRadioButtonGroup();

                }


        }
    }

    private void noInternet() {
        new CDialog(getActivity()).createAlert("No Internet Connection",
                CDConstants.WARNING,   // Type of dialog
                CDConstants.LARGE)    //  size of dialog
                .setAnimation(CDConstants.SCALE_TO_TOP_FROM_BOTTOM)     //  Animation for enter/exit
                .setDuration(5000)   // in milliseconds
                .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                .show();
        // Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_SHORT).show();

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Toast.makeText(getActivity(),  Utilities.getSharedPreferences(getActivity()).getString("gender",""), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(getActivity(), "LogIn", Toast.LENGTH_SHORT).show();
            strPersonFullname = account.getDisplayName();
            strPersonUsername =account.getGivenName();
            strPersonEmail = account.getEmail();


            API.RegisterSocialApiCall(getActivity(),strPersonFullname,strProvider,strPersonUsername,strPersonEmail);









            // Signed in successfully, show authenticated UI.


            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {

    }



    public void CreateAlertDialogWithRadioButtonGroup(){


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Gender");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                        Utilities.putValueInEditor(getActivity()).putString("gender","Male").commit();
                        signIn();
                        break;
                    case 1:

                        Utilities.putValueInEditor(getActivity()).putString("gender","Female").commit();
                        signIn();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

}



