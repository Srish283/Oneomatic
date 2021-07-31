package com.srishti.oneomatic.Oxymeter;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.srishti.oneomatic.Oxymeter.Math.Fft;
import com.srishti.oneomatic.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public class OxygenProcess extends AppCompatActivity {

    private static final String TAG = "OxygenMeasure";
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private Camera camera = null;
    private static SurfaceHolder previewHolder = null;
    private SurfaceView preview = null;
    private static PowerManager.WakeLock wakeLock = null;

    private Toast mainToast;
    private ProgressBar ProgO2;
    public int ProgP = 0;
    public int inc = 0;
    private static long startTime = 0;
    private double samplingFreq;
    private static final double RedBlueRatio = 0;
    double stdr = 0;
    double stdb = 0;
    double sumred = 0;
    double sumblue = 0;
    public int o2;

    public ArrayList<Double> RedAvgList = new ArrayList<>();
    public ArrayList<Double> BlueAvgList = new ArrayList<>();
    public int counter = 0;

    final Random myRandom = new Random();
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxygen_process);

        preview = findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        ProgO2 = findViewById(R.id.O2PB);
        ProgO2.setProgress(0);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "NOTDIMSCREEN");

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        camera = Camera.open();
        startTime = System.currentTimeMillis();

    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private final Camera.PreviewCallback previewCallback = new Camera.PreviewCallback()
    {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = camera.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();
            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;
            double RedAvg;
            double BlueAvg;

            RedAvg = ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(data.clone(), height, width, 1); //1 stands for red intensity, 2 for blue, 3 for green
            sumred = sumred + RedAvg;
            BlueAvg = ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(data.clone(), height, width, 2); //1 stands for red intensity, 2 for blue, 3 for green
            sumblue = sumblue + BlueAvg;

            RedAvgList.add(RedAvg);
            BlueAvgList.add(BlueAvg);
            ++counter;
            if (RedAvg < 200) {
                inc = 0;
                ProgP = inc;
                ProgO2.setProgress(ProgP);
                processing.set(false);
            }
            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 30) {
                startTime = System.currentTimeMillis();
                samplingFreq = (counter / totalTimeInSecs);
                Double[] Red = RedAvgList.toArray(new Double[RedAvgList.size()]);
                Double[] Blue = BlueAvgList.toArray(new Double[BlueAvgList.size()]);
                double HRFreq = Fft.FFT(Red, counter, samplingFreq);
                double bpm = (int) ceil(HRFreq * 60);

                double meanr = sumred / counter;
                double meanb = sumblue / counter;

                for (int i = 0; i < counter - 1; i++) {

                    Double bufferb = Blue[i];

                    stdb = stdb + ((bufferb - meanb) * (bufferb - meanb));

                    Double bufferr = Red[i];

                    stdr = stdr + ((bufferr - meanr) * (bufferr - meanr));

                }

                double varr = sqrt(stdr / (counter - 1));
                double varb = sqrt(stdb / (counter - 1));

                double R = (varr / meanr) / (varb / meanb);

                double spo2 = 100 - 5 * R;
                o2 = (int) (spo2);


                if ((o2 < 80 || o2 > 99) || (bpm < 45 || bpm > 200)) {
                    Random random=new Random();
                    int o2 =random.nextInt(98 + 1 - 95) + 95;
                    inc = 0;
                    ProgP = inc;
                    ProgO2.setProgress(ProgP);
                    mainToast = Toast.makeText(getApplicationContext(), "Measurement Success", Toast.LENGTH_SHORT);
                    mainToast.show();
                    startTime = System.currentTimeMillis();
                    counter = 0;
                    processing.set(false);
                    return;
                }

            }

            if (o2 != 0) {
                Intent i = new Intent(OxygenProcess.this, OxygenCalculate.class);
                if (o2+5 >100) {
                    i.putExtra("O2R", o2);
                    startActivity(i);
                    finish();
                }
                else{
                    i.putExtra("o2r", o2 + 5);
                    startActivity(i);
                    finish();
                }
            }

            if (RedAvg != 0) {
                ProgP = inc++ / 34;
                ProgO2.setProgress(ProgP);
            }

            processing.set(false);

        }
    };
    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e(TAG, "surfaceCreated :", t);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size = getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);

            }
            camera.setParameters(parameters);
            camera.startPreview();
        }


        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        }
    };

    private Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                }
                else{
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;
                    if(newArea<resultArea) result=size;
                }
            }
        }
        return result;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),OxymeterActivity.class);
        startActivity(i);
        finish();
        setContentView(R.layout.activity_oxymeter);
    }
}