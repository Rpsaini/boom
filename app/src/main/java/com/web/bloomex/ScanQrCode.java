package com.web.bloomex;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Handler;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import androidx.core.app.ActivityCompat;

public class ScanQrCode extends BaseActivity {


    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private boolean stopScanning=true;
    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        getSupportActionBar().hide();
        initiateObj();
        stopScanning=true;
        initViews();
    }

    boolean isBlinked=true;
    private void initViews() {

        TextView txt_scanqrcode=findViewById(R.id.txt_scanqrcode);
        txt_scanqrcode.setText(getIntent().getStringExtra("title"));

        surfaceView = findViewById(R.id.surfaceView);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initialiseDetectorsAndSources()
    {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                try {
                    if(ActivityCompat.checkSelfPermission(ScanQrCode.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanQrCode.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                //   Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() != 0)
                {
                    if(barcodes.valueAt(0).displayValue.length()>0)
                    {
                        if(stopScanning)
                        {
                            stopScanning=false;
                            String displayUrl=barcodes.valueAt(0).displayValue;
                            if(displayUrl.length()>0) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("code", barcodes.valueAt(0).displayValue);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }

                        }

                    }


                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
        stopScanning=true;
        blinkView();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        stopScanning=false;
        initialiseDetectorsAndSources();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    private void blinkView()
    {
        final RelativeLayout rr_squarelayout =findViewById(R.id.rr_squarelayout);
        if(handler!=null)
        {
            handler.removeCallbacks(runnable);
        }
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                if(isBlinked)
                {
                    rr_squarelayout.setAlpha(.3f);
                    isBlinked=false;
                }
                else
                {
                    rr_squarelayout.setAlpha(1f);
                    isBlinked=true;
                }
                handler.postDelayed(this,200);
            }
        };
        handler.postDelayed(runnable,200);
    }
}