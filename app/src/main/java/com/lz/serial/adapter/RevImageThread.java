package com.lz.serial.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.lz.serial.ui.threader.ThreadActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;

import static com.lz.serial.net.Contracts.VIDEO;
import static com.lz.serial.net.Contracts.url;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/4/18 上午10:50
 * 描述     :
 */
public class RevImageThread extends Thread {
    private Handler handler;

    public static Bitmap bitmap;



    public RevImageThread(Handler handler){
        this.handler = handler;

    }
    @Override
    public void run() {
        byte [] buffer = new byte[1024];
        int len = 0;
        HttpURLConnection conn = null;
        for (int i = 0; i < 3; i ++) {
            try {
                Log.d ("car ", "opening url: " + url);
                URL parse = new URL(url);
                conn = (HttpURLConnection) parse.openConnection ();
                InputStream in = conn.getInputStream ();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();

                while( (len=in.read(buffer)) != -1){
                    outStream.write(buffer, 0, len);
                }
                in.close();
                byte data[] = outStream.toByteArray();
                bitmap = rotation(BitmapFactory.decodeByteArray(data, 0, data.length),180);

                Log.i("car", bitmap.toString());
                Message msg =handler.obtainMessage();
                msg.what = VIDEO;
                msg.obj = bitmap;
                handler.sendMessage(msg);

            } catch (IOException ex) {
                Log.e ("car ", ex.getMessage (), ex);
                try {
                    Thread.sleep (2000);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            } finally {
                if (conn != null) {
                    conn.disconnect ();
                }
            }
        }
    }


    public Bitmap rotation(Bitmap bm, final int orientationDegree){

            Matrix m = new Matrix();
            m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            float targetX, targetY;
            if (orientationDegree == 90) {
                targetX = bm.getHeight();
                targetY = 0;
            } else {
                targetX = bm.getHeight();
                targetY = bm.getWidth();
            }

            final float[] values = new float[9];
            m.getValues(values);

            float x1 = values[Matrix.MTRANS_X];
            float y1 = values[Matrix.MTRANS_Y];

            m.postTranslate(targetX - x1, targetY - y1);

            Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

            Paint paint = new Paint();
            Canvas canvas = new Canvas(bm1);
            canvas.drawBitmap(bm, m, paint);


            return bm1;

    }
}
