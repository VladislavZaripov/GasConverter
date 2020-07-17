package Gasconverter.Helpers;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastHelper {

    public static void toastIncorrectParam(Context context) {
        Toast toast = Toast.makeText(context,"Некорректное значение",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void toastDuplicateNumber(Context context) {
        Toast toast = Toast.makeText(context,"Данный номер уже есть в базе",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void toastNoChooseVehicle(Context context){
        Toast toast = Toast.makeText(context,"Не выбрано ТС",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void toastTrialVersion(Context context) {
        Toast toast = Toast.makeText(context, "Пробная версия до 31.07.2020", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
