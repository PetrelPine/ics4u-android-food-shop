package com.example.admin.ccb.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.example.admin.ccb.MainActivity;
import com.example.admin.ccb.R;

import www.ccb.com.common.base.BaseActivity;
import www.ccb.com.common.utils.UiUtils;


/**
 * flash page + weather page
 * isForecast ： false (flash page)
 * isForecast ： true (weather page)
 */
public class SplashActivity extends BaseActivity implements AMapLocationListener, WeatherSearch.OnWeatherSearchListener {

    public static final String WEATHER = "WEATHER";
    // Declare the mlocationClient object
    public AMapLocationClient mlocationClient;
    // Declare the mlocationoption object
    public AMapLocationClientOption mLocationOption = null;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private TextView tvCountTime, tvAddress, tvTime, tvTianQi, tvWendu, tvFengli, tvTre;
    private LinearLayout layoutForecast, layouTrea, rootView;
    private CardView cv;
    private boolean isWeather; // Show forecast
    private CountDownTimer countDownTimer;

    @Override
    public int getContentViewResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

        rootView = findViewById(R.id.rootView);
        imageView = findViewById(R.id.iv);
        recyclerView = findViewById(R.id.rv);
        tvAddress = findViewById(R.id.tvAddress);
        tvTime = findViewById(R.id.tvTime);
        tvTianQi = findViewById(R.id.tvTianQi);
        tvWendu = findViewById(R.id.tvWendu);
        tvFengli = findViewById(R.id.tvFengli);
        tvTre = findViewById(R.id.tvTre);
        tvCountTime = findViewById(R.id.tvCountTime);
        layoutForecast = findViewById(R.id.layoutForecast);
        layouTrea = findViewById(R.id.layouTrea);
        cv = findViewById(R.id.cv);

    }

    @Override
    protected void initList() {
    }

    @Override
    protected void initData() {
        isWeather = getIntent().getBooleanExtra(WEATHER, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setAdapter(WeatherAdapter);
        setViewColor();
        goLocation();
        startSplash();
    }

    private void setViewColor() {
        if (isWeather) {
            imageView.setVisibility(View.GONE);
            rootView.setBackgroundResource(R.color.ali_color);
            tvAddress.setPadding(0, 0, 0, UiUtils.dp2px(80));
            tvAddress.setTextSize(36);
            tvAddress.setTextColor(getResources().getColor(R.color.colorWhite));
            tvTre.setTextColor(getResources().getColor(R.color.colorWhite));
            tvTime.setTextColor(getResources().getColor(R.color.colorWhite));
            tvTianQi.setTextColor(getResources().getColor(R.color.colorWhite));
            tvWendu.setTextColor(getResources().getColor(R.color.colorWhite));
            tvFengli.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    private void startSplash() {
        if (isWeather) return;

        countDownTimer = new CountDownTimer(6 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountTime.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };
        countDownTimer.start();

        cv.setVisibility(View.VISIBLE);
        tvCountTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
                countDownTimer.cancel();
                countDownTimer = null;
            }
        });
    }

    private void goLocation() {
        // Get location information
        mlocationClient = new AMapLocationClient(this);
        // Initialize location parameters
        mLocationOption = new AMapLocationClientOption();
        // Setting location Listening
        mlocationClient.setLocationListener(SplashActivity.this);
        // Set the location mode to high precision, Battery_Saving to low power, and Device_Sensors to device-only
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // Set the location interval, in milliseconds. The default value is 2000ms
        mLocationOption.setInterval(-1000);
        mLocationOption.setOnceLocation(true);
        // Setting Location Parameters
        mlocationClient.setLocationOption(mLocationOption);
        // Enable Location
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            getWeather(aMapLocation.getCity());
        }
        mlocationClient.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) mlocationClient.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void getWeather(String city) {
        // The search parameters are city and weather type, with live weather as WEATHER_TYPE_LIVE and weather forecast as WEATHER_TYPE_FORECAST
        WeatherSearchQuery mquery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearch mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); // Asynchronous search

        if (isWeather) {
            WeatherSearchQuery mquery2 = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_FORECAST);
            WeatherSearch mweathersearch2 = new WeatherSearch(this);
            mweathersearch2.setOnWeatherSearchListener(this);
            mweathersearch2.setQuery(mquery2);
            mweathersearch2.searchWeatherAsyn(); // Asynchronous search
        }
    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int i) {
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
    }
}
