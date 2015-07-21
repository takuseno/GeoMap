package jp.gr.java_conf.androtaku.geomap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by takuma on 2015/07/18.
 */
public class GeoMapView extends SurfaceView implements SurfaceHolder.Callback{
    private List<CountrySection> countrySections;
    private Context context;
    private Paint defaultPaint;
    private Thread prepareThread;
    private HashMap<String, Paint> countryPaints;
    private OnInitializedListener listener;

    public GeoMapView(Context context){
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        countryPaints = new HashMap<>();
    }
    public GeoMapView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.context = context;
        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        countryPaints = new HashMap<>();
    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder){
        defaultPaint = new Paint();
        defaultPaint.setColor(Color.BLACK);
        defaultPaint.setStyle(Paint.Style.STROKE);
        defaultPaint.setAntiAlias(true);

        prepareThread = new Thread(new Runnable() {
            @Override
            public void run() {
                countrySections = SVGParser.getCountrySections(context);
                Canvas canvas = holder.lockCanvas();
                drawMap(canvas);
                holder.unlockCanvasAndPost(canvas);
                listener.onInitialized(GeoMapView.this);
            }
        });
        prepareThread.start();
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
                Path path = new Path();
                path.moveTo(xPathList.get(i).get(0) * ratio, yPathList.get(i).get(0) * ratio);
                int numPoint = xPathList.get(i).size();
                for (int j = 1; j < numPoint; ++j) {
                    path.lineTo(xPathList.get(i).get(j), yPathList.get(i).get(j));
                }
                Paint paint = countryPaints.get(countrySection.getCountryCode());
                if(paint != null){
                    canvas.drawPath(path, paint);
                }
                canvas.drawPath(path, defaultPaint);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        prepareThread = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public void setCountryColor(String countryCode, String color){
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(color));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        countryPaints.put(countryCode, paint);
    }
    public void setCountryColor(String countryCode, int red, int green, int blue){
        Paint paint = new Paint();
        paint.setColor(Color.rgb(red, green, blue));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        countryPaints.put(countryCode, paint);
    }

    public void removeCountryColor(String countryCode){
        countryPaints.remove(countryCode);
    }

    public void refresh(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Canvas canvas = getHolder().lockCanvas();
                drawMap(canvas);
                getHolder().unlockCanvasAndPost(canvas);
            }
        });
        thread.start();
    }

    public void setOnInitializedListener(OnInitializedListener listener){
        this.listener = listener;
    }
}
