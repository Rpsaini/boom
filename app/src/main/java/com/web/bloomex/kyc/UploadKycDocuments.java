//package com.web.bloomex.kyc;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.content.FileProvider;
//
//import android.Manifest;
//import android.annotation.TargetApi;
//import android.app.ProgressDialog;
//import android.content.ClipData;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.app.dialogsnpickers.DialogCallBacks;
//import com.web.bloomex.BaseActivity;
//import com.web.bloomex.LoginActivity;
//import com.web.bloomex.R;
//import com.web.bloomex.fileupload.AddEventInterface;
//import com.web.bloomex.fileupload.ApiProduction;
//import com.web.bloomex.fileupload.RxAPICallHelper;
//import com.web.bloomex.fileupload.RxAPICallback;
//import com.web.bloomex.fileupload.ServerResponse;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import io.reactivex.Observable;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Response;
//
//public class UploadKycDocuments extends BaseActivity
//  {
//    private ImageView iv_nationid_front,iv_nationalid_back,iv_address_proof_front,iv_address_proof_back;
//    private String iv_nationid_front_str="",iv_nationalid_back_str="",iv_address_proof_front_str="",iv_address_proof_back_str="";
//    private String selectedPath="";
//    private int selectImageIndex=1,browseMode=0;
//    //0 for camera,1 for gallery
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_kyc_documents);
//        getSupportActionBar().hide();
//        initiateObj();
//        init();
//    }
//    private void init()
//    {
//        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//
//        iv_nationid_front= findViewById(R.id.iv_nationid_front);
//        iv_nationalid_back= findViewById(R.id.iv_nationalid_back);
//        iv_address_proof_front=findViewById(R.id.iv_address_proof_front);
//        iv_address_proof_back= findViewById(R.id.iv_address_proof_back);
//
//        iv_nationid_front.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImageIndex=1;
//                browseImage();
//
//            }
//        });
//
//
//        iv_nationalid_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImageIndex=2;
//                browseImage();
//            }
//        });
//
//        iv_address_proof_front.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImageIndex=3;
//                browseImage();
//            }
//        });
//
//        iv_address_proof_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImageIndex=4;
//                browseImage();
//            }
//        });
//    }
//
//
//    private void browseImage()
//    {
//        alertDialogs.alertDialog(UploadKycDocuments.this, getResources().getString(R.string.app_name), "Choose image from", "Camera", "Gallery", new DialogCallBacks() {
//            @Override
//            public void getDialogEvent(String buttonPressed)
//            {
//                if(buttonPressed.equalsIgnoreCase("Camera"))
//                {
//                    browseMode=0;
//                    selectImage(browseMode);
//                }
//                else
//                {
//                    browseMode=1;
//                    selectImage(browseMode);
//                }
//
//            }
//        });
//    }
//
//
//      @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//      private void selectImage(int actionCode) {
//          if (checkAndRequestPermissions() == 0)
//           {
//              if (actionCode == 0) {
//                  dispatchTakePictureIntent();
//              } else if (actionCode == 1)
//              {
//                  Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                          android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                  startActivityForResult(pickPhoto, actionCode);
//              }
//          }
//      }
//
//
//
//
//      public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//          super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//
//          if (resultCode != RESULT_CANCELED) {
//              switch (requestCode) {
//                  case 0://camera
//                       try {
//                          Bitmap bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
//                          Uri uri = getImageUri(this, bmap);
//                           selectedPath = getRealPathFromURI(uri);
//
//                          if(selectImageIndex==0)
//                          {
//                              iv_nationid_front.setImageBitmap(bmap);
//                          }
//                          else if(selectImageIndex==0)
//                          {
//                              iv_nationalid_back.setImageBitmap(bmap);
//                          }
//                          else if(selectImageIndex==0)
//                          {
//                              iv_address_proof_front.setImageBitmap(bmap);
//                          }
//                          else if(selectImageIndex==0)
//                          {
//                              iv_address_proof_back.setImageBitmap(bmap);
//                          }
//
//
//                      } catch (Exception e) {
//                          e.printStackTrace();
//                      }
//
//                  break;
//                  case 1://gallery
//                      if (resultCode == RESULT_OK) {
//                          if (imageReturnedIntent != null) {
//                              try {
//                                  Uri selectedImage = imageReturnedIntent.getData();
//                                  selectedPath = getRealPathFromURI(selectedImage);
//                                  InputStream image_stream = getContentResolver().openInputStream(selectedImage);
//                                  Bitmap bmap = BitmapFactory.decodeStream(image_stream);
//                                  if(selectImageIndex==0)
//                                  {
//                                      iv_nationid_front.setImageBitmap(bmap);
//                                  }
//                                  else if(selectImageIndex==0)
//                                  {
//                                      iv_nationalid_back.setImageBitmap(bmap);
//                                  }
//                                  else if(selectImageIndex==0)
//                                  {
//                                      iv_address_proof_front.setImageBitmap(bmap);
//                                  }
//                                  else if(selectImageIndex==0)
//                                  {
//                                      iv_address_proof_back.setImageBitmap(bmap);
//                                  }
//
//
//                              } catch (Exception e) {
//                                  e.printStackTrace();
//                              }
//                          }
//                      }
//                      break;
//              }
//
//              // new ConvertImage().execute();
//          }
//      }
//
//      static final int REQUEST_TAKE_PHOTO = 0;
//      Uri photoURI;
//
//      private void dispatchTakePictureIntent() {
//          Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//          if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//              File photoFile = null;
//              try {
//                  photoFile = createImageFile();
//              } catch (IOException ex) {
//                  System.out.println("inside exception===" + ex.getMessage());
//
//              }
//              if (photoFile != null) {
//                  photoURI = FileProvider.getUriForFile(this,
//                          getPackageName()+".fileprovider",
//                          photoFile);
//                  takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                  if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
//                      takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
//                      takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                  }
//                  startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//              }
//          }
//      }
//
//      String mCurrentPhotoPath;
//
//      private File createImageFile() throws IOException {
//          String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//          String imageFileName = "JPEG_" + timeStamp + "_";
//          File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//          File image = File.createTempFile(
//                  imageFileName,  /* prefix */
//                  ".jpg",         /* suffix */
//                  storageDir      /* directory */
//          );
//          mCurrentPhotoPath = image.getAbsolutePath();
//          return image;
//      }
//
//      public String getRealPathFromURI(Uri uri) {
//          String[] projection = {MediaStore.Images.Media.DATA};
//          @SuppressWarnings("deprecation")
//          Cursor cursor = managedQuery(uri, projection, null, null, null);
//          int column_index = cursor
//                  .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//          cursor.moveToFirst();
//          return cursor.getString(column_index);
//      }
//
//
//      private int checkAndRequestPermissions() {
//
//          int permissionCAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
//          int readExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//          int writeExternal = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//          List<String> listPermissionsNeeded = new ArrayList<>();
//          if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
//              listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
//          }
//          if (readExternal != PackageManager.PERMISSION_GRANTED) {
//              listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//          }
//          if (writeExternal != PackageManager.PERMISSION_GRANTED) {
//              listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//          }
//          if (!listPermissionsNeeded.isEmpty()) {
//              ActivityCompat.requestPermissions(this,
//                      listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
//              return 1;
//          }
//
//          return 0;
//      }
//
//
//      public Uri getImageUri(Context inContext, Bitmap inImage) {
//          ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//          inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//          String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//          return Uri.parse(path);
//      }
//
//
//
//
//      private void senAndUploadFile()
//       {
//
//          Observable<Response<ServerResponse>> responseObservable = null;
//          AddEventInterface contestService = ApiProduction.getInstance(this).provideService(AddEventInterface.class);
//
//         // RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), getRestParamsName(AppSettings.user_id));
//
//          Map<String, String> heade = new HashMap<>();
//      //    heade.put("token", savePreferences.reterivePreference(this, AppSettings.token) + "");
//
//          File file = new File(selectedPath);
//          RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
//          MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
//          RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//
//            // responseObservable = contestService.ticketReply(user_id, query_id, message, token, body, filename);
//
//          ProgressDialog progressDialog = new ProgressDialog(this);
//          progressDialog.setTitle("Please wait..");
//          progressDialog.show();
//
//          RxAPICallHelper.call(responseObservable, new RxAPICallback<Response<ServerResponse>>() {
//              @Override
//              public void onSuccess(Response<ServerResponse> t) {
//                  progressDialog.dismiss();
//                  System.out.println("message===" + t.body());
//                  selectedPath="";
//
//                  if (t.body().isStatus()) {
//
//                  }
//                  else {
//
//                      try
//                      {
//                          alertDialogs.alertDialog(UploadKycDocuments.this, getResources().getString(R.string.app_name), t.body().getMsg(), "ok", "", new DialogCallBacks() {
//                              @Override
//                              public void getDialogEvent(String buttonPressed) {
//                                  if (buttonPressed.equalsIgnoreCase("ok")) {
//
//                                  }
//                              }
//                          });
//
//                      }
//                      catch (Exception e) {
//                          e.printStackTrace();
//                      }
//                  }
//
//
//              }
//
//              @Override
//              public void onFailed(Throwable throwable) {
//                  progressDialog.dismiss();
//                  alertDialogs.alertDialog(UploadKycDocuments.this, "ok", "Message not sent.", "ok", "", new DialogCallBacks() {
//                      @Override
//                      public void getDialogEvent(String buttonPressed) {
//                          if (buttonPressed.equalsIgnoreCase("ok")) {
//                          }
//                      }
//                  });
//              }
//          });
//
//      }
//
//  }