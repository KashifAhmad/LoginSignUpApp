package com.example.attaurrahman.e_complaint.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;


import com.alirezaahmadi.progressbutton.ProgressButtonComponent;
import com.example.attaurrahman.e_complaint.genralUtils.HTTPMultiPartEntity;
import com.example.attaurrahman.e_complaint.R;
import com.example.attaurrahman.e_complaint.configuration.CheckNetwork;
import com.example.attaurrahman.e_complaint.configuration.Config;
import com.example.attaurrahman.e_complaint.genralUtils.Utilities;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.nks.dropdownwarninglibrary.DropDownWarning;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.tapadoo.alerter.Alerter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class Complaint_Fragment extends Fragment implements View.OnClickListener {

    View parentView;
    MaterialSpinner spComplaint;
    String strComplaintImage, strComplaintVideo;
    @BindView(R.id.btn_complaint_submit)
    Button btnComplaintSubmit;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.iv_pic_cam)
    ImageView ivPicCamera;
    @BindView(R.id.iv_video_cam)
    ImageView ivVideoCamera;
    @BindView(R.id.iv_pic_cross)
    ImageView ivPicCross;
    @BindView(R.id.iv_video_cross)
    ImageView ivVideoCross;
    @BindView(R.id.iv_get_pic_cam)
    ImageView ivGetPicCamera;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_pause)
    ImageView ivPause;
    @BindView(R.id.iv_logout)
    ImageView ivLogOut;
    @BindView(R.id.vv_video_cam)
    VideoView vvVideoCamera;
    @BindView(R.id.fl_image)
    FrameLayout flImage;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;


    Uri image_uri, mVideoURI;
    MediaController mc;
    int intStopPosition;

    File comlaint_file;
    Boolean flag_image, flag_video, flag_image_video_check;
    AlertDialog alertDialogSpot;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        parentView = inflater.inflate(R.layout.fragment_complaint_, container, false);
        ButterKnife.bind(this, parentView);

        alertDialogSpot = new SpotsDialog(getActivity(), R.style.CustomUploading);


        flag_image = true;
        flag_video = true;
        flag_image_video_check = false;


        String[] ITEMS = {"Complaint Type", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spComplaint = parentView.findViewById(R.id.sp_complaint);
        spComplaint.setAdapter(adapter);

        etDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etDescription.setFocusableInTouchMode(true);
                return false;
            }
        });


        flImage.setOnClickListener(this);
        flVideo.setOnClickListener(this);
        ivPicCamera.setOnClickListener(this);
        ivVideoCamera.setOnClickListener(this);
        ivPicCross.setOnClickListener(this);
        ivVideoCross.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivPause.setOnClickListener(this);
        btnComplaintSubmit.setOnClickListener(this);
        ivLogOut.setOnClickListener(this);


        return parentView;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pic_cam:


                if (flag_image == true) {

                    new FancyAlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.e_complaint))
                            .setMessage("Choose Image From ")
                            .setNegativeBtnText("Gallery")
                            .setPositiveBtnText("Camera")
                            .setPositiveBtnBackground(Color.parseColor("#F6C569"))
                            .setNegativeBtnBackground(Color.parseColor("#F6C569"))
                            .setAnimation(Animation.POP)
                            .setBackgroundColor(Color.parseColor("#50B8A6"))
                            .setIcon(R.drawable.imageicon, Icon.Visible)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {

                                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                                }
                            })
                            .OnNegativeClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
                                }
                            })
                            .build();

                } else {

                    Alerter.create(getActivity())
                            .setTitle(getString(R.string.e_complaint))
                            .setText("Already Video Select")
                            .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                            .show();
                    //Toast.makeText(getActivity(), "Already Video Select", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_video_cam:

                if (flag_video == true) {


                    new FancyAlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.e_complaint))
                            .setMessage("Choose Video From")
                            .setNegativeBtnText("Gallery")
                            .setPositiveBtnText("Camera")
                            .setPositiveBtnBackground(Color.parseColor("#F6C569"))
                            .setNegativeBtnBackground(Color.parseColor("#F6C569"))
                            .setBackgroundColor(Color.parseColor("#50B8A6"))
                            .setIcon(R.drawable.add_video, Icon.Visible)
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {

                                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    startActivityForResult(takeVideoIntent, 2);
                                }
                            })
                            .OnNegativeClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {

                                    Intent intent = new Intent();
                                    intent.setType("video/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), 2);
                                }
                            })
                            .build();
                } else {

                    Alerter.create(getActivity())
                            .setTitle(getString(R.string.e_complaint))
                            .setText("Already Image Select")
                            .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                            .show();

                    // Toast.makeText(getActivity(), "Already Video Select", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_pic_cross:

                ivPicCross.setVisibility(View.GONE);
                ivPicCamera.setImageResource(R.mipmap.cam);
                ivGetPicCamera.setImageResource(0);
                ivPicCamera.setEnabled(true);
                ivPicCamera.setVisibility(View.VISIBLE);
                flag_image = true;
                flag_video = true;
                flag_image_video_check = false;

                break;
            case R.id.iv_video_cross:
                ivVideoCross.setVisibility(View.GONE);
                vvVideoCamera.setVisibility(View.GONE);
                ivVideoCamera.setImageResource(R.mipmap.vid_cam);
                ivPlay.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
                ivVideoCamera.setEnabled(true);
                flag_video = true;
                flag_image = true;
                flag_image_video_check = false;


                break;


            case R.id.iv_play:


                onResume();
                ivPause.setVisibility(View.VISIBLE);
                ivPlay.setVisibility(View.GONE);

                break;

            case R.id.iv_pause:
                onPause();
                ivPause.setVisibility(View.GONE);
                ivPlay.setVisibility(View.VISIBLE);
                break;


            case R.id.btn_complaint_submit:
                if (!CheckNetwork.isInternetAvailable(getActivity())) {
                    new CDialog(getActivity()).createAlert("No Internet Connection",
                            CDConstants.WARNING,   // Type of dialog
                            CDConstants.LARGE)    //  size of dialog
                            .setAnimation(CDConstants.SCALE_TO_TOP_FROM_BOTTOM)     //  Animation for enter/exit
                            .setDuration(5000)   // in milliseconds
                            .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                            .show();
                    // Toast.makeText(getActivity(), "Check Network Connection", Toast.LENGTH_SHORT).show();
                } else {

                    if (spComplaint.getSelectedItem() == null) {
                        //Toast.makeText(getActivity(), "Select Complaint Type", Toast.LENGTH_SHORT).show();

                        Alerter.create(getActivity())
                                .setTitle(getString(R.string.e_complaint))
                                .setText("Select Complaint Type...")
                                .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                                .show();


                    } else if (flag_image_video_check == false) {

                        Alerter.create(getActivity())
                                .setTitle(getString(R.string.e_complaint))
                                .setText("Chose Image And Video")
                                .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                                .show();

                        // Toast.makeText(getActivity(), "Chose Image And Video", Toast.LENGTH_SHORT).show();

                    } else if (etDescription.length() <= 1) {

                        Alerter.create(getActivity())
                                .setTitle(getString(R.string.e_complaint))
                                .setText("Enter Desription")
                                .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                                .show();

                        //Toast.makeText(getActivity(), "Enter Desription", Toast.LENGTH_SHORT).show();

                    } else {
                        if (flag_image == true) {
                            comlaint_file = new File(strComplaintImage);

                        } else if (flag_video == true) {

                            comlaint_file = new File(strComplaintVideo);
                        } else {


                        }
                        new UploadFileToServer().execute();
                        alertDialogSpot.show();
                    }

                }
                break;
            case R.id.iv_logout:
                Utilities.putValueInEditor(getActivity()).putBoolean("isLogin", false).commit();
                LoginManager.getInstance().logOut();
                Utilities.connectFragmentForMyComplaint(getActivity(), new LoginTabFragment());


        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {


            case 0:
                if (resultCode == RESULT_OK) {


                    Bitmap bm = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File sourceFile = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        sourceFile.createNewFile();
                        fo = new FileOutputStream(sourceFile);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ivGetPicCamera.setImageBitmap(bm);

                    strComplaintImage = sourceFile.getAbsolutePath().toString();
                    ivPicCross.setVisibility(View.VISIBLE);
                    ivPicCamera.setVisibility(View.GONE);
                    ivGetPicCamera.setEnabled(false);

                    flag_video = false;
                    flag_image_video_check = true;


                } else {

                    Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
                    flag_video = true;
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    image_uri = imageReturnedIntent.getData();
                    ivGetPicCamera.setImageURI(image_uri);

                    strComplaintImage = getImagePath(image_uri);
                    ivPicCross.setVisibility(View.VISIBLE);
                    ivPicCamera.setVisibility(View.GONE);
                    ivGetPicCamera.setEnabled(false);
                    flag_video = false;
                    flag_image_video_check = true;
                } else {
                    Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
                    flag_video = true;
                }
                break;

            case 2:


                if (resultCode != RESULT_CANCELED) {
                    if (requestCode == 2) {
                        flag_image = false;
                        flag_image_video_check = true;
                        ivVideoCross.setVisibility(View.VISIBLE);
                        vvVideoCamera.setVisibility(View.VISIBLE);
                        vvVideoCamera.setEnabled(false);
                        ivVideoCamera.setEnabled(false);
                        mVideoURI = imageReturnedIntent.getData();
                        strComplaintVideo = getVideoPath(mVideoURI);

                        // System.out.println("SELECT_VIDEO Path : " + selectedPath);
                        comlaint_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getActivity().getPackageName() + "/media/videos");
                        // uploadVideo(selectedPath);


                        vvVideoCamera.setMediaController(mc);
                        vvVideoCamera.setVideoURI(mVideoURI);
                        vvVideoCamera.requestFocus();
                        vvVideoCamera.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            // Close the progress bar and play the video
                            public void onPrepared(MediaPlayer mp) {
                                vvVideoCamera.start();

                                Handler handler = new Handler();
                                Runnable r = new Runnable() {
                                    public void run() {
                                        onPause();
                                        ivPlay.setVisibility(View.VISIBLE);
                                    }
                                };
                                handler.postDelayed(r, 300);
                            }
                        });
                    }
                } else {
                    Toast.makeText(getActivity(), "No Video Selected", Toast.LENGTH_SHORT).show();
                    flag_image = true;

                }


                break;
        }
    }

    public String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    public String getVideoPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause called");
        super.onPause();
        intStopPosition = vvVideoCamera.getCurrentPosition(); //stopPosition is an int
        vvVideoCamera.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        vvVideoCamera.seekTo(intStopPosition);
        vvVideoCamera.start(); //Or use resume() if it doesn't work. I'm not sure
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.BASE_URL + "generate_post_card");
            try {
                HTTPMultiPartEntity entity = new HTTPMultiPartEntity(
                        new HTTPMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) 100) * 100));


                            }
                        });
                // Adding file data to http body
                // Extra parameters if you want to pass to server
                entity.addPart("post_card_signature", new FileBody(comlaint_file));
                entity.addPart("post_card_img", new FileBody(comlaint_file));
                Looper.prepare();
                entity.addPart("location", new StringBody(spComplaint.getSelectedItem().toString()));
                entity.addPart("post_card_text", new StringBody(etDescription.getText().toString()));
                entity.addPart("user_id", new StringBody("58"));
//                     pDialog.dismiss();
                Bundle args = new Bundle();

                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                responseString = EntityUtils.toString(r_entity);

                Log.d("response string", responseString.toString());

                if (responseString.contains("true")) {
                    alertDialogSpot.dismiss();
                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
                    Utilities.connectFragmentWithOutBackStack(getActivity(),new Complaint_Fragment());

                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
//                    pDialog.dismiss();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                responseString = e.toString();
//                    pDialog.dismiss();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }

            Log.d("zma return string", responseString);
            return responseString;

        }
    }

}
