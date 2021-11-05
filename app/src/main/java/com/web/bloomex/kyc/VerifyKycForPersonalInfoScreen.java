//package com.web.bloomex.kyc;
//
//import android.Manifest;
//import android.annotation.TargetApi;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.ClipData;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.content.FileProvider;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.app.dialogsnpickers.DialogCallBacks;
//
//import com.app.dialogsnpickers.SimpleDialog;
//
//import com.app.vollycommunicationlib.CallBack;
//import com.app.vollycommunicationlib.ServerHandler;
//import com.google.gson.Gson;
//import com.web.bloomex.BaseActivity;
//
//import com.web.bloomex.BuildConfig;
//import com.web.bloomex.DefaultConstants;
//import com.web.bloomex.R;
//import com.web.bloomex.fileupload.AddEventInterface;
//import com.web.bloomex.fileupload.ApiProduction;
//import com.web.bloomex.fileupload.RxAPICallHelper;
//import com.web.bloomex.fileupload.RxAPICallback;
//import com.web.bloomex.fileupload.ServerResponse;
//
//import com.web.bloomex.kyc.adapter.SelectCategorySubCategoryAdapter;
//
//import com.web.bloomex.utilpackage.UtilClass;
//
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//import io.reactivex.Observable;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Response;
//
//
//public class VerifyKycForPersonalInfoScreen extends BaseActivity
//{
//    public static VerifyKycForPersonalInfoScreen mVerifyKycForPersonalInfoScreen;
//    private ImageView backIc = null;
//    private ImageView img_one;
//    private TextView dobET,lbl_one,btn_browse,selectCountryTV,select_typeTV,select_stateTV,select_DocTypeTV,adharNoTV,reDocTV,uploadFrontDocTitleTV,uploadBackDocTitleTV;
//    RecyclerView select_category_recycle;
//    private ImageView pancardImage,docImage,docBackImage,docSelfiImage;
//    private RelativeLayout panUploadRL,docUploadRL,docBackUploadRL,docSelfiUploadRL;
//    String imageType="pan";
//    private ImageView commonImage;
//    private String countryID;
//    private EditText firstNameET,middleNameET,lastNameET,addressET,cityET,pinCodeET,panNumberET,rePanNumberET,adharNumberET,reAdharNumberET;
//    private String panCardImage="",adharCardFrontImage="",adharCardBackImage="",selfiImage="";
//    private String docType="adhaar";
//    private int mYear,mMonth,mDay;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.verify_profile_details_screen);
//        mVerifyKycForPersonalInfoScreen=this;
//        initiateObj();
//        initView();
//        setOnClickListener();
//        actions();
//        setData();
//    }
//
//    private void initView() {
//        backIc = findViewById(R.id.backIC);
//        selectCountryTV=findViewById(R.id.select_countryTV);
//        select_typeTV=findViewById(R.id.select_typeTV);
//
//        select_stateTV=findViewById(R.id.select_stateTV);
//        select_DocTypeTV=findViewById(R.id.select_DocTypeTV);
//
//        firstNameET=findViewById(R.id.firstNameET);
//        middleNameET=findViewById(R.id.middleNameET);
//        lastNameET=findViewById(R.id.lastNameET);
//
//        dobET=findViewById(R.id.dobET);
//        addressET=findViewById(R.id.addressET);
//        cityET=findViewById(R.id.cityET);
//
//        pinCodeET=findViewById(R.id.pinCodeET);
//        panNumberET=findViewById(R.id.panNumberET);
//        rePanNumberET=findViewById(R.id.rePanNumberET);
//
//        adharNumberET=findViewById(R.id.adharNoET);
//        reAdharNumberET=findViewById(R.id.reDocET);
//
//        pancardImage=findViewById(R.id.pancardImage);
//        docImage=findViewById(R.id.docImage);
//        docBackImage=findViewById(R.id.docBackImage);
//        docSelfiImage=findViewById(R.id.docSelfiImage);
//
//        panUploadRL=findViewById(R.id.panUploadRL);
//        docUploadRL=findViewById(R.id.docUploadRL);
//        docBackUploadRL=findViewById(R.id.docBackUploadRL);
//        docSelfiUploadRL=findViewById(R.id.docSelfiUploadRL);
//
//        adharNoTV=findViewById(R.id.adharNoTV);
//        reDocTV=findViewById(R.id.reDocTV);
//        uploadFrontDocTitleTV=findViewById(R.id.uploadFrontDocTitleTV);
//        uploadBackDocTitleTV=findViewById(R.id.uploadBackDocTitleTV);
//
//        adharNoTV.setText("National ID Number*");
//        reDocTV.setText("Re-Enter National ID Number*");
//        adharNumberET.setHint("Enter National ID number");
//        reAdharNumberET.setHint("Enter National ID number");
//        uploadFrontDocTitleTV.setText("Upload front of National ID Card");
//        uploadBackDocTitleTV.setText("Upload back of National ID Card");
//        docImage.setImageDrawable(getDrawable(R.drawable.sample_aadhaar_card_front_1));
//        docBackImage.setImageDrawable(getDrawable(R.drawable.sample_aadhaar_card_back_1));
//
//
//
//        Intent intent =getIntent();
//        if(intent.hasExtra("country_name")){
//         selectCountryTV.setText(intent.getExtras().getString("country_name"));
//        }
//        if(intent.hasExtra("kyc_type")){
//            select_typeTV.setText(intent.getExtras().getString("kyc_type"));
//        }
//        if(intent.hasExtra("_id")){
//           countryID=intent.getExtras().getString("_id");
//        }
//    }
//
//    private void setOnClickListener() {
//        backIc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//       selectCountryTV.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               Intent intent =new Intent(VerifyKycForPersonalInfoScreen.this,SelectCountryScreen.class);
//               startActivityForResult(intent,101);
//           }
//       });
//        select_stateTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent(VerifyKycForPersonalInfoScreen.this,SelectStateScreen.class);
//                intent.putExtra("_id",countryID);
//                startActivityForResult(intent,102);
//            }
//        });
//
//        select_typeTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectTypeDialog();
//            }
//        });
//        select_DocTypeTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectDocTypeDialog();
//            }
//        });
//        findViewById(R.id.submitVerifyBT).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (firstNameET.getText().toString().length() == 0) {
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.firstname_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//                if (dobET.getText().toString().length() == 0) {
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.dob_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//                if (addressET.getText().toString().length() == 0) {
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.address_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//                if (select_stateTV.getText().toString().length() == 0) {
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.state_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//                if (cityET.getText().toString().length() == 0) {
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.city_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//                if (pinCodeET.getText().toString().length() == 0) {
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.pincode_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//
//
//                if (adharNumberET.getText().toString().length() == 0) {
//                    String msg="";
//                    if(docType.equals("adhaar")){
//                        msg="Enter National ID number";
//                    }
//
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required),msg, getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//
//                if (!adharNumberET.getText().toString().equals(reAdharNumberET.getText().toString())){
//
//                    String msg="";
//                    if(docType.equals("adhaar")){
//                        msg="National ID Number must be match.";
//                    }
//
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required),msg, getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//
//                    return;
//                }
//
//                if (adharCardFrontImage.length() == 0) {
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.national_id_front_image_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//
//                if (adharCardBackImage.length() == 0) {
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.national_id_back_image_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//
//                if (selfiImage.length() == 0) {
//                    alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Required), getResources().getString(R.string.selfie_image_warning), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//
//                        }
//                    });
//                    return;
//                }
//
//                saveKyc();
//            }
//        });
//        dobET.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                final DatePickerDialog datePickerDialog = new DatePickerDialog(VerifyKycForPersonalInfoScreen.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            String fmonth, fDate;
//                            int month;
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
////bjb
//                                try {
//                                    if (monthOfYear < 10 && dayOfMonth < 10) {
//
//                                        fmonth = "0" + monthOfYear;
//                                        month = Integer.parseInt(fmonth) + 1;
//                                        fDate = "0" + dayOfMonth;
//                                        String paddedMonth = String.format("%02d", month);
//                                        dobET.setText(fDate + "-" + paddedMonth + "-" + year);
//
//                                    } else {
//
//                                        fmonth = "0" + monthOfYear;
//                                        month = Integer.parseInt(fmonth) + 1;
//                                        String paddedMonth = String.format("%02d", month);
//                                        dobET.setText(dayOfMonth + "-" + paddedMonth + "-" + year);
//                                    }
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog.show();
//            }
//        });
//    }
//
//    private void actions() {
//        panUploadRL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageType="pan";
//                commonImage=pancardImage;
//                browseImage();
//            }
//        });
//        docUploadRL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageType="documnt_front";
//                commonImage=docImage;
//                browseImage();
//            }
//        });
//        docBackUploadRL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageType="document_back";
//                commonImage=docBackImage;
//                browseImage();
//            }
//        });
//        docSelfiUploadRL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageType="selfie";
//                commonImage=docSelfiImage;
//                browseImage();
//            }
//        });
//
//    }
//
//    //browse image
//    int browseMode = 0;
//
//    private void browseImage() {
//        alertDialogs.alertDialog(this, getResources().getString(R.string.app_name), "Choose Image from", "Camera", "Gallery", new DialogCallBacks() {
//            @Override
//            public void getDialogEvent(String buttonPressed) {
//                if (buttonPressed.equalsIgnoreCase("Camera")) {
//
//                    browseMode = 0;
//                    selectImage(browseMode);
//                } else {
//                    browseMode = 1;
//                    selectImage(browseMode);
//                }
//
//            }
//        });
//    }
//
//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private void selectImage(int actionCode) {
//        if (checkAndRequestPermissions() == 0) {
//            if (actionCode == 0) {
//                dispatchTakePictureIntent();
//            } else if (actionCode == 1) {
//                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pickPhoto, actionCode);
//            }
//        }
//    }
//
//
//    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//
//        if (resultCode != RESULT_CANCELED) {
//
//            switch (requestCode) {
//                case 0://camera
//                    try {
//                        Bitmap bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
//                        Uri uri = getImageUri(this, bmap);
//                        selectedPath = getRealPathFromURI(uri);
//
//                        if(fileSize(selectedPath))
//                        {
//                            commonImage.setImageBitmap(bmap);
//                            senAndUploadFile();
//                        }
//
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    break;
//                case 1://gallery
//                    if (resultCode == RESULT_OK) {
//                        if (imageReturnedIntent != null) {
//                            try {
//                                Uri selectedImage = imageReturnedIntent.getData();
//                                selectedPath = getRealPathFromURI(selectedImage);
//                                InputStream image_stream = getContentResolver().openInputStream(selectedImage);
//                                Bitmap bmap = BitmapFactory.decodeStream(image_stream);
//                                if(fileSize(selectedPath))
//                                {
//                                    commonImage.setImageBitmap(bmap);
//                                    senAndUploadFile();
//                                }
//
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                    break;
//                case 101://selectCountry
//                    if (imageReturnedIntent != null) {
//                        String countryName=  imageReturnedIntent.getExtras().getString("_name");
//                        countryID=imageReturnedIntent.getExtras().getString("_id");
//                        selectCountryTV.setText(countryName);
//                    }
//
//                    break;
//                case 102://selectState
//                    if (imageReturnedIntent != null) {
//                        String stateName=  imageReturnedIntent.getExtras().getString("_name");
//                        select_stateTV.setText(stateName);
//                    }
//
//                    break;
//            }
//
//            // new ConvertImage().execute();
//        }
//    }
//
//    private boolean fileSize(String  path)
//    {
//
//        try {
//            File file = new File(path);
//            long length = file.length();
//            length = length/1024;
//            System.out.println("File Path : " + file.getPath() + ", File size : " + length +" KB");
//
//            if(length<=5000)
//            {
//                return  true;
//            }
//            else
//            {
//                alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.app_name), getResources().getString(R.string.image_size), "ok", "", new DialogCallBacks() {
//                    @Override
//                    public void getDialogEvent(String buttonPressed) {
//                    }
//                });
//                return  false;
//
//            }
//
//
//        }
//        catch (Exception e)
//        {
//
//            e.printStackTrace();
//        }
//        return false;
//
//
//
//    }
//
//    static final int REQUEST_TAKE_PHOTO = 0;
//    Uri photoURI;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                System.out.println("inside exception===" + ex.getMessage());
//
//            }
//            if (photoFile != null) {
//                photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
//                        BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
//
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
//                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
//                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                }
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }
//
//
//    String mCurrentPhotoPath;
//
//
//    //    String mCurrentPhotoPath;
//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",  /* suffix */
//                storageDir      /* directory */
//        );
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    public String getRealPathFromURI(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        @SuppressWarnings("deprecation")
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        int column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
//
//
//    private int checkAndRequestPermissions() {
//
//        int permissionCAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
//        int readExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        int writeExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
//        }
//        if (readExternal != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//        if (writeExternal != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this,
//                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
//            return 1;
//        }
//
//        return 0;
//    }
//
//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    //end of browse image
//
//
//    private String selectedPath = "";
//
//    private void senAndUploadFile() {
//
//        Observable<Response<ServerResponse>> responseObservable = null;
//        AddEventInterface contestService = ApiProduction.getInstance(this).provideService(AddEventInterface.class);
//
//        File file = new File(selectedPath);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData(imageType, file.getName(), requestBody);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//
//        RequestBody DeviceToken = RequestBody.create(MediaType.parse("text/plain"), getDeviceToken());
//        RequestBody Version = RequestBody.create(MediaType.parse("text/plain"), getAppVersion());
//        RequestBody PlatForm = RequestBody.create(MediaType.parse("text/plain"), "Android");
//        RequestBody Timestamp = RequestBody.create(MediaType.parse("text/plain"), System.currentTimeMillis()+"");
//
//        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), savePreferences.reterivePreference(VerifyKycForPersonalInfoScreen.this, "token") + "");
//        responseObservable = contestService.uploadKyc(token, DeviceToken, Version, PlatForm,Timestamp, getXapiKey(), body, filename);
//
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Please wait..");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        RxAPICallHelper.call(responseObservable, new RxAPICallback<Response<ServerResponse>>() {
//            @Override
//            public void onSuccess(Response<ServerResponse> t) {
//                try {
//                    progressDialog.dismiss();
//                    selectedPath = "";
//                    if (t.body().isStatus()) {
//                        ServerResponse.Kyc_info kyc_info = t.body().getKyc_info();
//                        Gson gson = new Gson();
//                        String json = gson.toJson(kyc_info);
//
//                        if (imageType.equals("pan")){
//                           panCardImage="pan";
//                        }
//                        else if (imageType.equals("documnt_front")){
//                          adharCardFrontImage="documnt_front";
//                        }
//                        else if (imageType.equals("document_back")){
//                            adharCardBackImage="document_back";
//                        }
//                        else if (imageType.equals("selfie")){
//                             selfiImage="selfie";
//                        }
//
//
//
//                    } else {
//
//                        alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.app_name), t.body().getMsg(), "ok", "", new DialogCallBacks() {
//                            @Override
//                            public void getDialogEvent(String buttonPressed) {
//                            }
//                        });
//                    }
//
////                      {"status":true,"msg":"Your KYC details has been updated successfully","kyc_info":{"attempt":true,"status":"0","address_f_status":"1","address_b_status":"0","pan_status":"0"},"code":200}
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onFailed(Throwable throwable) {
//                progressDialog.dismiss();
//                System.out.println("error===" + throwable.getMessage());
//                alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, "ok", "Message document not updated.", "ok", "", new DialogCallBacks() {
//                    @Override
//                    public void getDialogEvent(String buttonPressed) {
//                        if (buttonPressed.equalsIgnoreCase("ok")) {
//
//                        }
//                    }
//                });
//            }
//        });
//
//    }
//
//    private void selectTypeDialog() {
//        try {
//
//            hideKeyboard(this);
//            SimpleDialog simpleDialog = new SimpleDialog();
//            final Dialog selectCategoryDialog = simpleDialog.simpleDailog(VerifyKycForPersonalInfoScreen.this, R.layout.select_category_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
//            select_category_recycle = selectCategoryDialog.findViewById(R.id.select_category_recycler);
//            ImageView img_hideview = selectCategoryDialog.findViewById(R.id.img_hideview);
//            final RelativeLayout ll_relativelayout = selectCategoryDialog.findViewById(R.id.ll_relativelayout);
//            final TextView select_title = selectCategoryDialog.findViewById(R.id.select_title);
//            final TextView select_sub_title = selectCategoryDialog.findViewById(R.id.select_sub_title);
//            final TextView tv_done = selectCategoryDialog.findViewById(R.id.tv_done);
//            animateUp(ll_relativelayout);
//            select_title.setText(getResources().getString(R.string.type));
//            select_sub_title.setText("");
//            JSONArray buisnessTypeAr = new JSONArray();
//            JSONObject type1 = new JSONObject();
//            type1.put("name", "personal");
//
//            JSONObject type2 = new JSONObject();
//            type2.put("name", "company");
//
//            buisnessTypeAr.put(type1);
//            buisnessTypeAr.put(type2);
//
//
//            initHomeCategory(buisnessTypeAr);
//
//
//
//            img_hideview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    downSourceDestinationView(ll_relativelayout, selectCategoryDialog);
//                }
//            });
//
//            tv_done.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    downSourceDestinationView(ll_relativelayout, selectCategoryDialog);
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    public void setKycType(String s){
//        if(!s.isEmpty()) {
//            select_typeTV.setText(s);
//        }
//        else {
//            select_typeTV.setText(getString(R.string.personal));
//
//        }
//
//    }
//    private void initHomeCategory(JSONArray dataAr) {
//        select_category_recycle.setNestedScrollingEnabled(false);
//        select_category_recycle.setLayoutManager(new LinearLayoutManager(this,
//                LinearLayoutManager.VERTICAL, false));
//        select_category_recycle.setHasFixedSize(true);
//        select_category_recycle.setItemAnimator(new DefaultItemAnimator());
//        SelectCategorySubCategoryAdapter horizontalCategoriesAdapter = new SelectCategorySubCategoryAdapter(dataAr, this);
//        select_category_recycle.setAdapter(horizontalCategoriesAdapter);
//    }
//    private void initHomeCategory(JSONArray dataAr,String type) {
//        select_category_recycle.setNestedScrollingEnabled(false);
//        select_category_recycle.setLayoutManager(new LinearLayoutManager(this,
//                LinearLayoutManager.VERTICAL, false));
//        select_category_recycle.setHasFixedSize(true);
//        select_category_recycle.setItemAnimator(new DefaultItemAnimator());
//        SelectCategorySubCategoryAdapter horizontalCategoriesAdapter = new SelectCategorySubCategoryAdapter(dataAr, this,type);
//        select_category_recycle.setAdapter(horizontalCategoriesAdapter);
//    }
//
//    private void saveKyc()
//    {
//        Map<String, String> m=new HashMap<>();
//        m.put("kyc_type", select_typeTV.getText().toString());
//        m.put("country_id", countryID);
//        m.put("first_name", firstNameET.getText().toString());
//        m.put("last_name", lastNameET.getText().toString());
//        m.put("dob", dobET.getText().toString());
//        m.put("address", addressET.getText().toString());
//        m.put("state", select_stateTV.getText().toString());
//        m.put("city", cityET.getText().toString());
//        m.put("postal_code", pinCodeET.getText().toString());
//        //m.put("pan_number", "pan");
//        m.put("document_type", docType);
//        m.put("id_number", adharNumberET.getText().toString());
//
//        m.put("token",savePreferences.reterivePreference(this, DefaultConstants.token)+"");
//        m.put("DeviceToken",getDeviceToken()+"");
//        m.put("Version",getAppVersion()+"");
//        m.put("PlatForm", "Android");
//        m.put("Timestamp", System.currentTimeMillis()+"");
//        Map<String,String> headerMap=new HashMap<>();
//        headerMap.put("X-API-KEY", UtilClass.xApiKey);
//        headerMap.put("Rtoken", getNewRToken()+"");
//
//
//
//        new ServerHandler().sendToServer(this, getApiUrl()+"submit-kyc-request", m, 0,headerMap, 20000, R.layout.progressbar, new CallBack() {
//            @Override
//            public void getRespone(String dta, ArrayList<Object> respons) {
//                try {
//
//                    JSONObject obj = new JSONObject(dta);
//                    Log.d("Fait",obj+"");
//                  /*  {"status":true,"msg":"Your KYC details have been submitted successfully","code":200}
//                 */
//                    if (obj.getBoolean("status")) {
//                        try
//                        {
//                            if(obj.has("token"))
//                            {
//                                savePreferences.savePreferencesData(VerifyKycForPersonalInfoScreen.this,obj.getString("token"),DefaultConstants.token);
//                                savePreferences.savePreferencesData(VerifyKycForPersonalInfoScreen.this,obj.getString("r_token"),DefaultConstants.r_token);
//                                savePreferences.savePreferencesData(VerifyKycForPersonalInfoScreen.this,"1",DefaultConstants.kyc_status);
//
//                            }
//
//                            alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                                @Override
//                                public void getDialogEvent(String buttonPressed) {
//                                    unauthorizedAccess(obj);
//                                    VerifyKycForPersonalInfoScreen.mVerifyKycForPersonalInfoScreen.finish();
//                                    VerifyKycAccountDetailsScreen.mVerifyKycAccountDetailsScreen.finish();
//                                    VerifyCompleteSubmitKycScreen.mVerifyCompleteSubmitKycScreen.finish();
//
//                                }
//                            });
//
//
//
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//
//                    } else {
//                        alertDialogs.alertDialog(VerifyKycForPersonalInfoScreen.this, getResources().getString(R.string.Response), obj.getString("msg"), getResources().getString(R.string.ok), "", new DialogCallBacks() {
//                        @Override
//                        public void getDialogEvent(String buttonPressed) {
//                            unauthorizedAccess(obj);
//                        }
//                    });
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//    }
//
//    private void selectDocTypeDialog() {
//        try {
//
//            hideKeyboard(this);
//            SimpleDialog simpleDialog = new SimpleDialog();
//            final Dialog selectCategoryDialog = simpleDialog.simpleDailog(VerifyKycForPersonalInfoScreen.this, R.layout.select_category_dialog, new ColorDrawable(getResources().getColor(R.color.translucent_black)), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, false);
//            select_category_recycle = selectCategoryDialog.findViewById(R.id.select_category_recycler);
//            ImageView img_hideview = selectCategoryDialog.findViewById(R.id.img_hideview);
//            final RelativeLayout ll_relativelayout = selectCategoryDialog.findViewById(R.id.ll_relativelayout);
//            final TextView select_title = selectCategoryDialog.findViewById(R.id.select_title);
//            final TextView select_sub_title = selectCategoryDialog.findViewById(R.id.select_sub_title);
//            final TextView tv_done = selectCategoryDialog.findViewById(R.id.tv_done);
//            animateUp(ll_relativelayout);
//            select_title.setText(getResources().getString(R.string.document_ty));
//            select_sub_title.setText("");
//            JSONArray buisnessTypeAr = new JSONArray();
//
//            JSONObject adhaar = new JSONObject();
//            adhaar.put("name", "National ID");
//
//         /*   JSONObject passport = new JSONObject();
//            passport.put("name", "passport");
//
//            JSONObject driving_license = new JSONObject();
//            driving_license.put("name", "driving-license");
//
//            JSONObject voter_id = new JSONObject();
//            voter_id.put("name", "voter-id");
//*/
//            buisnessTypeAr.put(adhaar);
//           // buisnessTypeAr.put(passport);
//           // buisnessTypeAr.put(driving_license);
//            //buisnessTypeAr.put(voter_id);
//
//
//
//            initHomeCategory(buisnessTypeAr,"doc");
//
//
//
//            img_hideview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    downSourceDestinationView(ll_relativelayout, selectCategoryDialog);
//                }
//            });
//
//            tv_done.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    downSourceDestinationView(ll_relativelayout, selectCategoryDialog);
//
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    public void setDocType(String s){
//        if(!s.isEmpty()) {
//            select_DocTypeTV.setText(s);
//
//          /*  adhaar
//                    passport
//            driving-license
//            voter-id*/
//            if(s.equals("National ID")){
//                docType="adhaar";
//                adharNoTV.setText("National ID Number*");
//                reDocTV.setText("Re-Enter National ID Number*");
//                adharNumberET.setHint("Enter National ID number");
//                reAdharNumberET.setHint("Enter National ID number");
//                uploadFrontDocTitleTV.setText("Upload front of National ID Card");
//                uploadBackDocTitleTV.setText("Upload back of National ID Card");
//                docImage.setImageDrawable(getDrawable(R.drawable.sample_aadhaar_card_front_1));
//                docBackImage.setImageDrawable(getDrawable(R.drawable.sample_aadhaar_card_back_1));
//
//            }
//         /*   if(s.equals("passport")){
//                adharNoTV.setText("Passport Number*");
//                reDocTV.setText("Re-Enter Passport Number*");
//                adharNumberET.setHint("Enter Passport number");
//                reAdharNumberET.setHint("Enter Passport number");
//                uploadFrontDocTitleTV.setText("Upload front of Passport Card");
//                uploadBackDocTitleTV.setText("Upload back of Passport Card");
//
//                docImage.setImageDrawable(getDrawable(R.drawable.sample_passport_front_1));
//                docBackImage.setImageDrawable(getDrawable(R.drawable.sample_passport_baclk_1));
//
//            }
//            if(s.equals("driving-license")){
//                adharNoTV.setText("Driving-License Number*");
//                reDocTV.setText("Re-Enter Driving-License Number*");
//                adharNumberET.setHint("Enter Driving-License number");
//                reAdharNumberET.setHint("Enter Driving-License number");
//                uploadFrontDocTitleTV.setText("Upload front of Driving-License Card");
//                uploadBackDocTitleTV.setText("Upload back of Driving-License Card");
//
//                docImage.setImageDrawable(getDrawable(R.drawable.sample_driving_licence_front_1));
//                docBackImage.setImageDrawable(getDrawable(R.drawable.sample_driving_licence_back_1));
//
//            }
//            if(s.equals("voter-id")){
//                adharNoTV.setText("Voter-ID Number*");
//                reDocTV.setText("Re-Enter Voter-ID Number*");
//                adharNumberET.setHint("Enter Voter-ID number");
//                reAdharNumberET.setHint("Enter Voter-ID number");
//                uploadFrontDocTitleTV.setText("Upload front of Voter-ID Card");
//                uploadBackDocTitleTV.setText("Upload back of Voter-ID Card");
//
//                docImage.setImageDrawable(getDrawable(R.drawable.sample_voter_card_front_1));
//                docBackImage.setImageDrawable(getDrawable(R.drawable.sample_voter_card_back_1));
//
//            }*/
//        }
//        else {
//            docType="adhaar";
//            select_DocTypeTV.setText(getString(R.string.personal));
//            adharNoTV.setText("Aadhaar Number*");
//            reDocTV.setText("Re-Enter Aadhaar Number*");
//            uploadFrontDocTitleTV.setText("Upload front of Aadhaar Card");
//            uploadBackDocTitleTV.setText("Upload back of Aadhaar Card");
//            docImage.setImageDrawable(getDrawable(R.drawable.sample_aadhaar_card_front_1));
//            docBackImage.setImageDrawable(getDrawable(R.drawable.sample_aadhaar_card_back_1));
//
//        }
//
//    }
//
//    private void setData()
//    {
//        try {
//
//            firstNameET=findViewById(R.id.firstNameET);
//            middleNameET=findViewById(R.id.middleNameET);
//            lastNameET=findViewById(R.id.lastNameET);
//
//            JSONObject data=new JSONObject(savePreferences.reterivePreference(this, DefaultConstants.login_detail).toString());
//
//
//           String dataSTR=data.getString("name");
//           if(dataSTR.contains(" "))
//           {
//                   String ar[]=dataSTR.split(" ");
//                   firstNameET.setText(ar[0].trim());
//                   lastNameET.setText(ar[1].trim());
//
//           }
//           else
//           {
//               firstNameET.setText(dataSTR);
//           }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//
//    }
//
//}