package Gasconverter.Weather;

import Gasconverter.MainActivity;
import android.util.Log;
import android.widget.TextView;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDay {

    public class WeatherMain {
        Double temp;
    }

    @SerializedName("main")
    private WeatherMain main;

    @SerializedName("name")
    private String name;

    public WeatherDay(WeatherMain main) {
        this.main = main;
    }

    public String getTempInteger() { return String.valueOf(main.temp.intValue()); }

    public String getCity() { return name; }

    public static void getWeather(Double lat, Double lng, final TextView textView_city, final TextView textView_TemperatureGismeteo) {
        WeatherAPI.ApiInterface api = WeatherAPI.getClient().create(WeatherAPI.ApiInterface.class);
        String units = "metric";
        String key = WeatherAPI.KEY;

        Call<WeatherDay> callToday = api.getToday(lat, lng, units, key);
        callToday.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(Call<WeatherDay> call, Response<WeatherDay> response) {
                Log.e(MainActivity.LOG, "onResponse");
                WeatherDay data = response.body();
                Log.d(MainActivity.LOG, response.toString());

                if (response.isSuccessful()) {
                    if (data.getCity().equals("Nikolayevka"))
                        textView_city.setText("Balashikha");
                    else
                        textView_city.setText(data.getCity());
                    textView_TemperatureGismeteo.setText(data.getTempInteger());
                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                Log.e(MainActivity.LOG, "onFailure");
                Log.e(MainActivity.LOG, t.toString());
            }
        });
    }
}