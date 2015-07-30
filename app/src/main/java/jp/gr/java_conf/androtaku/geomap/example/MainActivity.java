package jp.gr.java_conf.androtaku.geomap.example;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import jp.gr.java_conf.androtaku.geomap.CountrySection;
import jp.gr.java_conf.androtaku.geomap.GeoMapView;
import jp.gr.java_conf.androtaku.geomap.OnInitializedListener;
import jp.gr.java_conf.androtaku.geomap.SVGParser;


public class MainActivity extends ActionBarActivity {
    GeoMapView geoMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);

        geoMapView = (GeoMapView)findViewById(R.id.geoMap);
        geoMapView.setOnInitializedListener(new OnInitializedListener() {
            @Override
            public void onInitialized(GeoMapView geoMapView) {
                progressBar.setVisibility(View.INVISIBLE);
                geoMapView.setCountryColor("US", "#00FF00");
                geoMapView.setCountryColor("JP", "#FF0000");
                geoMapView.refresh();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
