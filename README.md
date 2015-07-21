# GeoMap
GeoMapView library for Android

##Usage
``` .java
GeoMapView geoMapView = (GeoMapView)findViewById(R.id.geoMap);
geoMapView.setOnInitializedListener(new OnInitializedListener() {
    @Override
    public void onInitialized(GeoMapView geoMapView) {
        geoMapView.setCountryColor("US", "#00FF00");
        geoMapView.setCountryColor("JP", "#FF0000");
        geoMapView.refresh();
    }
});
```
