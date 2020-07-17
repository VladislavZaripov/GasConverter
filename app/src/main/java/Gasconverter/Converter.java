package Gasconverter;

import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;

public class Converter {

    ArrayList<Integer> temperature;
    ArrayList<Integer> pressure;
    ArrayList<ArrayList<Double>> rows;

    public void createData() {
        temperature = new ArrayList<>(Arrays.asList(-30, -20, -10, 0, 10, 20, 30, 40));
        pressure = new ArrayList<>(Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200));
        rows = new ArrayList<>();
        rows.add(new ArrayList<>(Arrays.asList(0.011, 0.011, 0.0108, 0.0106, 0.0106, 0.0106, 0.0104, 0.0104)));
        rows.add(new ArrayList<>(Arrays.asList(0.023, 0.0224, 0.0232, 0.022, 0.0218, 0.0214, 0.0212, 0.0208)));
        rows.add(new ArrayList<>(Arrays.asList(0.0358, 0.034, 0.034, 0.0338, 0.033, 0.0326, 0.0322, 0.0314)));
        rows.add(new ArrayList<>(Arrays.asList(0.0482, 0.0466, 0.046, 0.0454, 0.0444, 0.0434, 0.043, 0.0426)));
        rows.add(new ArrayList<>(Arrays.asList(0.064, 0.061, 0.0596, 0.0588, 0.0568, 0.0562, 0.055, 0.0544)));
        rows.add(new ArrayList<>(Arrays.asList(0.081, 0.0752, 0.0732, 0.0714, 0.0706, 0.069, 0.0682, 0.0654)));
        rows.add(new ArrayList<>(Arrays.asList(0.1, 0.0922, 0.0886, 0.0864, 0.0834, 0.0814, 0.0804, 0.0778)));
        rows.add(new ArrayList<>(Arrays.asList(0.129, 0.1142, 0.1066, 0.104, 0.0976, 0.0952, 0.093, 0.091)));
        rows.add(new ArrayList<>(Arrays.asList(0.1526, 0.1344, 0.125, 0.1184, 0.1126, 0.1098, 0.1058, 0.1034)));
        rows.add(new ArrayList<>(Arrays.asList(0.1754, 0.1538, 0.1448, 0.1352, 0.1298, 0.125, 0.119, 0.1162)));
        rows.add(new ArrayList<>(Arrays.asList(0.1964, 0.1718, 0.1594, 0.1506, 0.1448, 0.1392, 0.1326, 0.1294)));
        rows.add(new ArrayList<>(Arrays.asList(0.2182, 0.1876, 0.179, 0.169, 0.16, 0.1558, 0.1464, 0.1428)));
        rows.add(new ArrayList<>(Arrays.asList(0.2408, 0.2032, 0.197, 0.1858, 0.1756, 0.1666, 0.1604, 0.1566)));
        rows.add(new ArrayList<>(Arrays.asList(0.256, 0.2222, 0.2154, 0.2028, 0.1918, 0.1818, 0.175, 0.1708)));
        rows.add(new ArrayList<>(Arrays.asList(0.2632, 0.238, 0.2272, 0.2174, 0.2054, 0.1948, 0.1876, 0.183)));
        rows.add(new ArrayList<>(Arrays.asList(0.2758, 0.25, 0.2424, 0.2286, 0.2222, 0.2078, 0.2026, 0.1952)));
        rows.add(new ArrayList<>(Arrays.asList(0.2786, 0.2656, 0.2538, 0.2362, 0.2298, 0.218, 0.2126, 0.2074)));
        rows.add(new ArrayList<>(Arrays.asList(0.2858, 0.2728, 0.2648, 0.25, 0.24, 0.2308, 0.225, 0.2196)));
        rows.add(new ArrayList<>(Arrays.asList(0.2924, 0.2836, 0.2714, 0.2568, 0.25, 0.2406, 0.2318, 0.2236)));
        rows.add(new ArrayList<>(Arrays.asList(0.2986, 0.2858, 0.2762, 0.2598, 0.2532, 0.25, 0.2438, 0.2326)));
    }

    public Double convert(Double t, Double mPa, Double volume) {
        Log.d(MainActivity.LOG, "t = " + t);
        Log.d(MainActivity.LOG, "mPa = " + mPa);

        Integer temperatureLow;
        Integer temperatureHigh;
        Integer pressureLow;
        Integer pressureHigh;

        int i = 0;
        do {
            temperatureLow = temperature.get(i);
            i++;
        }
        while (i <= temperature.size() - 1 && t >= temperature.get(i));
        i = 0;
        do {
            pressureLow = pressure.get(i);
            i++;
        }
        while (i <= pressure.size() - 1 && mPa >= pressure.get(i));

        i = temperature.size() - 1;
        do {
            temperatureHigh = temperature.get(i);
            i--;
        }
        while (i >= 0 && t <= temperature.get(i));

        i = pressure.size() - 1;
        do {
            pressureHigh = pressure.get(i);
            i--;
        }
        while (i >= 0 && mPa <= pressure.get(i));

        Log.d(MainActivity.LOG, "temperatureLow = " + temperatureLow);
        Log.d(MainActivity.LOG, "temperatureHigh = " + temperatureHigh);
        Log.d(MainActivity.LOG, "pressureLow = " + pressureLow);
        Log.d(MainActivity.LOG, "pressureHigh = " + pressureHigh);

        int indexTemperatureLow = temperature.indexOf(temperatureLow);
        int indexTemperatureHigh = temperature.indexOf(temperatureHigh);
        int indexPressureLow = pressure.indexOf(pressureLow);
        int indexPressureHigh = pressure.indexOf(pressureHigh);

        double valueLeftTop = rows.get(indexPressureLow).get(indexTemperatureLow);
        double valueRightTop = rows.get(indexPressureLow).get(indexTemperatureHigh);
        double valueLeftBottom = rows.get(indexPressureHigh).get(indexTemperatureLow);
        double valueRightBottom = rows.get(indexPressureHigh).get(indexTemperatureHigh);

        Log.d(MainActivity.LOG, "valueLeftTop = " + valueLeftTop);
        Log.d(MainActivity.LOG, "valueRightTop = " + valueRightTop);
        Log.d(MainActivity.LOG, "valueLeftBottom = " + valueLeftBottom);
        Log.d(MainActivity.LOG, "valueRightBottom = " + valueRightBottom);

        double kColumn = (temperatureHigh - t) / 10;
        double kRow = (pressureHigh - mPa) / 10;

        Log.d(MainActivity.LOG, "kColumn = " + kColumn);
        Log.d(MainActivity.LOG, "kRow = " + kRow);

        double midLeft = valueLeftTop * kRow + valueLeftBottom * (1 - kRow);
        double midRight = valueRightTop * kRow + valueRightBottom * (1 - kRow);

        return volume * (midLeft * kColumn + midRight * (1 - kColumn));
    }
}