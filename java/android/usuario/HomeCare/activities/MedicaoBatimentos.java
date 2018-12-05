package android.usuario.HomeCare.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.usuario.HomeCare.R;
import android.usuario.HomeCare.helper.ProcessadorDeImagem;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import java.util.concurrent.atomic.AtomicBoolean;

public class MedicaoBatimentos extends Activity {
    private static final String TAG = "MedicaoBatimentos";
    private static final int[] averageArray = new int[4];
    private static final int averageArraySize = 4;
    private static int averageIndex = 0;
    private static double beats = 0.0d;
    private static final int[] beatsArray = new int[3];
    private static final int beatsArraySize = 3;
    private static int beatsIndex = 0;
    private static Camera camera = null;
    private static TYPE currentType = TYPE.GREEN;
    private static View image = null;
    private static SurfaceView preview = null;
    private static PreviewCallback previewCallback = new PreviewCallback() {

        public void onPreviewFrame(byte[] data, Camera cam) {

            if (data == null) {
                throw new NullPointerException();
            }
            Camera.Size size = cam.getParameters().getPreviewSize();

            if (size == null) {
                throw new NullPointerException();
            } else if (MedicaoBatimentos.processing.compareAndSet(false, true)) {

                int imgAvg = ProcessadorDeImagem.decodeYUV420SPtoRedAvg((byte[]) data.clone(), size.width, size.height);

                if (imgAvg == 0 || imgAvg == 255) {
                    MedicaoBatimentos.processing.set(false);
                    return;
                }
                int i;
                int averageArrayAvg = 0;
                int averageArrayCnt = 0;
                for (i = 0; i < MedicaoBatimentos.averageArray.length; i++) {
                    if (MedicaoBatimentos.averageArray[i] > 0) {
                        averageArrayAvg += MedicaoBatimentos.averageArray[i];
                        averageArrayCnt++;
                    }
                }

                int rollingAverage = averageArrayCnt > 0 ? averageArrayAvg / averageArrayCnt : 0;
                TYPE newType = MedicaoBatimentos.currentType;
                if (imgAvg < rollingAverage) {
                    newType = TYPE.RED;
                    if (newType != MedicaoBatimentos.currentType) {
                        MedicaoBatimentos.beats = MedicaoBatimentos.beats + 1.0d;
                    }
                } else if (imgAvg > rollingAverage) {
                    newType = TYPE.GREEN;
                }

                if (MedicaoBatimentos.averageIndex == 4) {
                    MedicaoBatimentos.averageIndex = 0;
                }
                MedicaoBatimentos.averageArray[MedicaoBatimentos.averageIndex] = imgAvg;
                MedicaoBatimentos.averageIndex = MedicaoBatimentos.averageIndex + 1;

                if (newType != MedicaoBatimentos.currentType) {
                    MedicaoBatimentos.currentType = newType;
                    //MedicaoBatimentos.image.postInvalidate();
                }

                double totalTimeInSecs = ((double) (System.currentTimeMillis() - MedicaoBatimentos.startTime)) / 1000.0d;
                int batimentos = (int) (60.0d * (MedicaoBatimentos.beats / totalTimeInSecs));
           //     MedicaoBatimentos.text.setText(String.valueOf(batimentos));

                if (totalTimeInSecs >= 5.0d) {
                    int dpm = (int) (60.0d * (MedicaoBatimentos.beats / totalTimeInSecs));
                    if (dpm < 30 || dpm > 180) {
                        MedicaoBatimentos.startTime = System.currentTimeMillis();
                        MedicaoBatimentos.beats = 0.0d;
                        MedicaoBatimentos.processing.set(false);
                        return;
                    }
                    if (MedicaoBatimentos.beatsIndex == 3) {
                        MedicaoBatimentos.beatsIndex = 0;
                    }

                    MedicaoBatimentos.beatsArray[MedicaoBatimentos.beatsIndex] = dpm;
                    MedicaoBatimentos.beatsIndex = MedicaoBatimentos.beatsIndex + 1;
                    int beatsArrayAvg = 0;
                    int beatsArrayCnt = 0;
                    for (i = 0; i < MedicaoBatimentos.beatsArray.length; i++) {
                        if (MedicaoBatimentos.beatsArray[i] > 0) {
                            beatsArrayAvg += MedicaoBatimentos.beatsArray[i];
                            beatsArrayCnt++;
                        }
                    }
                       MedicaoBatimentos.text.setText(String.valueOf(beatsArrayAvg / beatsArrayCnt));
                    MedicaoBatimentos.startTime = System.currentTimeMillis();
                    MedicaoBatimentos.beats = 0.0d;
                }
                MedicaoBatimentos.processing.set(false);
            }
        }
    };
    private static SurfaceHolder previewHolder = null;
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private static long startTime = 0;
    private static Callback surfaceCallback = new Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                MedicaoBatimentos.camera.setPreviewDisplay(MedicaoBatimentos.previewHolder);
                MedicaoBatimentos.camera.setPreviewCallback(MedicaoBatimentos.previewCallback);
            } catch (Throwable t) {
                Log.e("erro", "Exception in setPreviewDisplay()", t);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Parameters parameters = MedicaoBatimentos.camera.getParameters();
            parameters.setFlashMode("torch");
            Size size = MedicaoBatimentos.getSmallestPreviewSize(width, height, parameters);
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d(MedicaoBatimentos.TAG, "Using width=" + size.width + " height=" + size.height);
            }
            MedicaoBatimentos.camera.setParameters(parameters);
            MedicaoBatimentos.camera.startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };
    private static TextView text = null;
    private static WakeLock wakeLock = null;

    public enum TYPE {
        GREEN,
        RED
    }

    public static TYPE getCurrent() {
        return currentType;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicao);
        preview = (SurfaceView) findViewById(R.id.preview);
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(3);
        text = (TextView) findViewById(R.id.textBatimento);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(26, "DoNotDimScreen");
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onResume() {
        super.onResume();
        wakeLock.acquire();
        camera = Camera.open();
        startTime = System.currentTimeMillis();
    }

    public void onPause() {
        super.onPause();
        wakeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private static Size getSmallestPreviewSize(int width, int height, Parameters parameters) {
        Size result = null;
        for (Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else if (size.width * size.height < result.width * result.height) {
                    result = size;
                }
            }
        }
        return result;
    }
}
