package jp.gr.java_conf.androtaku.geomap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.nio.FloatBuffer;
import java.util.List;

/**
 * Created by takuma on 2015/07/18.
 */
public class GeoMapView extends SurfaceView implements SurfaceHolder.Callback{
    private List<CountrySection> countrySections;
    private Context context;

    private Paint paint;

    public GeoMapView(Context context){
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
    }
    public GeoMapView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.context = context;
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                countrySections = SVGParser.getCountrySections(context);

                paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setAntiAlias(true);

                Canvas canvas = holder.lockCanvas();
                drawMap(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        });
        thread.start();
    }

    private void drawMap(Canvas canvas){
        float ratio = 1.0f;
        if(SVGParser.xMax < canvas.getWidth()){
            ratio = (float)canvas.getWidth() / SVGParser.xMax;
        }
        else{
            ratio = SVGParser.xMax / (float)canvas.getWidth();
        }

        for(CountrySection countrySection : countrySections){
            List<List<Float>> xPathList = countrySection.getXPathList();
            List<List<Float>> yPathList = countrySection.getYPathList();
            int numList = xPathList.size();
            for (int i = 0; i < numList; ++i) {
                FloatBuffer floatBuffer = FloatBuffer.allocate(2048);
                float preXPos = xPathList.get(i).get(0);
                float preYPos = yPathList.get(i).get(0);
                int numPoint = xPathList.get(i).size();
                for (int j = 1; j < numPoint; ++j) {
                    floatBuffer.put(preXPos * ratio);
                    floatBuffer.put(preYPos * ratio);
                    floatBuffer.put(xPathList.get(i).get(j) * ratio);
                    floatBuffer.put(yPathList.get(i).get(j) * ratio);
                    preXPos = xPathList.get(i).get(j);
                    preYPos = yPathList.get(i).get(j);
                }
                canvas.drawLines(floatBuffer.array(), paint);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }
}
