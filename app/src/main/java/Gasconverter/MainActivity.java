package Gasconverter;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static Gasconverter.Helpers.AnimHelper.*;
import static Gasconverter.Helpers.AnimHelper.disableEditText;
import static Gasconverter.DB.addPositionInTable1;
import static Gasconverter.Helpers.ToastHelper.*;
import static Gasconverter.Weather.WeatherDay.getWeather;

public class MainActivity extends FragmentActivity {

    final public static String LOG = "myLog";

    DB db;
    Converter converter;

    Button button_star;

    CardView CV_search;
    LinearLayout LL_search;
    EditText editText_filter_number;
    TextWatcher textWatcher;
    Spinner spinner_group;
    Spinner spinner_brand;
    Button button_filter;
    static String filter_group;
    static String filter_brand;
    ArrayAdapter<String> adapterSpinnerGroup;
    ArrayAdapter<String> adapterSpinnerBrand;

    Button button_new;
    Button button_edit;
    EditText editText_group;
    EditText editText_brand;
    EditText editText_number;
    LayoutInflater inflater;
    LinearLayout LL_TABLE_2;
    LinearLayout LL_button_panel;
    Button button_addRow;
    Button button_deleteRow;
    Button button_ok;
    Button button_delete;
    Boolean newRecipeMode = false;
    String editNumber;
    ArrayList<Integer> rowsToDelete;

    RadioGroup radioGroupVolume;
    RadioButton radioButton_VolumeVehicle;
    RadioButton radioButton_VolumeEdit;
    ImageView imageView_rg1_rb1_img;
    TextView textView_rg1_rb1_description;
    TextView textView_VolumeVehicle;
    ImageView imageView_rg1_rb2_img;
    TextView textView_rg1_rb2_description;
    EditText editText_VolumeEdit;

    EditText editText_Pressure;

    RadioGroup radioGroupTemperature;
    RadioButton radioButton_TemperatureGismeteo;
    RadioButton radioButton_TemperatureEdit;
    ImageView imageView_rg2_rb1_img;
    TextView textView_city;
    TextView textView_TemperatureOpenWeather;
    ImageView imageView_rg2_rb2_img;
    TextView textView_rg2_rb2_description;
    EditText editText_TemperatureEdit;

    TextView textView_calculate;
    TextView textView_volumeCNG;
    Button button_Calculate;

    LocationManager locationManager;
    final static int REQUEST_CODE_PERMISSION_FINE_LOCATION = 0;
    int permissionFineLocation;
    boolean permissionGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDB();
        Log.d(LOG, "openDB");

        converter = new Converter();
        converter.createData();
        Log.d(LOG, "createData");

        initTitle ();
        Log.d(LOG, "initTitle");

        initCardSearch ();
        Log.d(LOG, "initCardSearch");

        initCardVehicle();
        Log.d(LOG, "initCardVehicle");

        initCardVolume();
        Log.d(LOG, "initCardVolume");

        initCardPressure();
        Log.d(LOG, "initCardPressure");

        initCardTemperature();
        Log.d(LOG, "initCardTemperature");

        initResult();
        Log.d(LOG, "initResult");

        permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(requestPermissions())
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        else
            textView_city.setText("Отсутсвует разрешение");
        Log.d(LOG, "locationManager");
    }


    private void openDB () {
        db = new DB(this);
        db.open();
    }

    private void initTitle () {

        button_star  = findViewById(R.id.button_star);
        create_Button_star();
    }

    private void create_Button_star() {
        button_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimClick(button_star);
                OpenAppInPlayStore();
            }
        });
    }

    private void OpenAppInPlayStore(){
        String packageName = this.getPackageName();

        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }

    }


    private void initCardSearch () {
        CV_search = findViewById(R.id.CV_search);
        LL_search = findViewById(R.id.LL_search);

        editText_filter_number = findViewById(R.id.editText_filter_number);
        create_EditText_filter_number();

        spinner_group  = findViewById(R.id.spinner_group);
        spinner_brand  = findViewById(R.id.spinner_brand);
        create_Spinners();

        button_filter  = findViewById(R.id.button_filter);
        create_Button_filter();
    }

    private void create_EditText_filter_number () {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prepareVehicleCard (editText_filter_number.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editText_filter_number.addTextChangedListener(textWatcher);
    }

    private void create_Spinners() {
        final ArrayList<List<String>> filter = DB.getFilter();

        adapterSpinnerGroup = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filter.get(0));
        adapterSpinnerBrand = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filter.get(1));
        adapterSpinnerGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterSpinnerBrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_group.setAdapter(adapterSpinnerGroup);
        spinner_brand.setAdapter(adapterSpinnerBrand);
        spinner_group.setPrompt(filter.get(0).get(0));
        spinner_brand.setPrompt(filter.get(1).get(0));
        spinner_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_group = filter.get(0).get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter_brand = filter.get(1).get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void create_Button_filter() {
        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimClick(button_filter);
                DialogFragment dialog = new FilterDialogFragment(DB.getFilterResult(filter_group,filter_brand),editText_filter_number);
                dialog.show(getSupportFragmentManager(), "FilterDialog");
            }
        });
    }

    private void update_Spinners() {
        final ArrayList<List<String>> filter = DB.getFilter();

        adapterSpinnerGroup.clear();
        adapterSpinnerBrand.clear();

        adapterSpinnerGroup.addAll(filter.get(0));
        adapterSpinnerBrand.addAll(filter.get(1));

        adapterSpinnerGroup.notifyDataSetChanged();
        adapterSpinnerBrand.notifyDataSetChanged();

        spinner_group.setSelection(0);
        spinner_brand.setSelection(0);
    }

    private void prepareVehicleCard (String number) {
        clearVehicleCard (false);

        Cursor cursor = DB.getTable1Pos(number);
        Integer vehicle_id = null;
        if (cursor.moveToFirst()) {
            editText_group.setText(cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_GROUP)));
            editText_brand.setText(cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_BRAND)));
            editText_number.setText(cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_NUMBER)));
            vehicle_id = (cursor.getInt(cursor.getColumnIndex(DB.TABLE1_COLUMN_ID)));
        }

        int volumeSum = 0;
        if (vehicle_id!=null)
            cursor = DB.getTable2Pos(vehicle_id);
        if (cursor.moveToFirst()) {
            int balloonNumber = 1;
            do {
                int volume = cursor.getInt(cursor.getColumnIndex(DB.TABLE2_COLUMN_VOLUME));
                int id = cursor.getInt(cursor.getColumnIndex(DB.TABLE2_COLUMN_ID));
                addRow(balloonNumber, id, volume,true);
                volumeSum = volumeSum + volume;
                balloonNumber++;
            } while (cursor.moveToNext());
        }
        textView_VolumeVehicle.setText(String.valueOf(volumeSum));
        cursor.close();
    }

    private void clearVehicleCard (boolean anim) {
        Log.d(LOG, "clearVehicleCard");
        editText_group.setText("");
        editText_brand.setText("");
        editText_number.setText("");

        if (anim)
        {
            int count = LL_TABLE_2.getChildCount();
            for (int idView = 0; idView < count; idView++) {
                final View view = LL_TABLE_2.getChildAt(idView);
                AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        LL_TABLE_2.removeView(view);
                    }
                };
                AnimDeleteRow(view,adapter);
            }
            Log.d(LOG, "clearVehicleCard: anim");
        }
        else {
            LL_TABLE_2.removeAllViews();
            Log.d(LOG, "clearVehicleCard: no anim");
        }
        textView_VolumeVehicle.setText("");
    }


    private void initCardVehicle () {
        button_new = findViewById(R.id.button_new);
        create_Button_new ();

        button_edit = findViewById(R.id.button_edit);
        create_Button_edit ();

        editText_group = findViewById(R.id.editText_group);
        disableEditText(editText_group);

        editText_brand = findViewById(R.id.editText_brand);
        disableEditText(editText_brand);

        editText_number = findViewById(R.id.editText_number);
        disableEditText(editText_number);

        inflater = getLayoutInflater();
        LL_TABLE_2 = findViewById(R.id.LL_TABLE_2);

        LL_button_panel = findViewById(R.id.LL_button_panel);
        AnimShadowLLButtonQuick(LL_button_panel);

        button_addRow = findViewById(R.id.button_addRow);
        create_Button_addRow();
        AnimRemoveButton(button_addRow);

        button_deleteRow = findViewById(R.id.button_deleteRow);
        create_Button_deleteRow();
        AnimRemoveButton(button_deleteRow);

        button_ok = findViewById(R.id.button_ok);
        create_Button_ok();
        AnimRemoveButton(button_ok);

        button_delete = findViewById(R.id.button_delete);
        create_Button_delete();
        AnimRemoveButton(button_delete);
    }

    private void create_Button_new() {
        button_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecipeMode = true;

                editText_group.setText("");
                editText_brand.setText("");
                editText_number.setText("");

                int count = LL_TABLE_2.getChildCount();
                Log.d(LOG, "count all = " + count);

                if (count>2) {
                    for ( ; count > 2; count--) {
                        Log.d(LOG, "count>2: " + count);
                        final View view = LL_TABLE_2.getChildAt(count-1);
                        AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                LL_TABLE_2.removeView(view);
                            }
                        };
                        AnimDeleteRow(view, adapter);
                    }
                }

                if (count==2){
                    Log.d(LOG, "count==2: " + count);
                    clearVehicleCard(false);
                    addRow (1, null, null,false);
                    addRow (2, null, null,false);
                }

                if (count==1){
                    Log.d(LOG, "count==1: " + count);
                    clearVehicleCard(false);
                    addRow (1, null, null,true);
                    addRow (2, null, null,false);
                }

                if (count==0){
                    Log.d(LOG, "count==0: " + count);
                    clearVehicleCard(false);
                    addRow (1, null, null,true);
                    addRow (2, null, null,true);
                }

                openNewEditMode();
            }
        });
    }

    private void create_Button_edit() {
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText_number.getText().toString().equals(""))
                {
                    newRecipeMode = false;
                    int count = LL_TABLE_2.getChildCount();
                    for (int idView = 0; idView < count; idView++) {
                        View view = LL_TABLE_2.getChildAt(idView);
                        final EditText editText_Volume = view.findViewById(R.id.editText_Volume);
                        enableEditText(editText_Volume);
                    }
                    editNumber = editText_number.getText().toString();
                    rowsToDelete = new ArrayList<>();

                    openNewEditMode();
                }
                else {
                    toastNoChooseVehicle(MainActivity.this);
                }
            }
        });
    }

    private void create_Button_addRow() {
        button_addRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimClick(button_addRow);
                int count = LL_TABLE_2.getChildCount();
                addRow (count+1,null,null,true);
            }
        });
    }

    private void create_Button_deleteRow() {
        button_deleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimClick(button_deleteRow);
                int count = LL_TABLE_2.getChildCount();
                if (count>0) {
                    final View view = LL_TABLE_2.getChildAt(count - 1);
                    if (view.getTag() != null) {
                        Integer id = (Integer) view.getTag();
                        Log.d(LOG, "id = " + id + " add to rowsToDelete");
                        rowsToDelete.add(id);
                    }
                    AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            LL_TABLE_2.removeView(view);
                        }
                    };
                    AnimDeleteRow(view,adapter);
                }
            }
        });
    }

    private void create_Button_ok() {
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimClick(button_ok);
                if(checkVehicleCardNullObject()) {
                    String group = editText_group.getText().toString();
                    String brand = editText_brand.getText().toString();
                    String number = editText_number.getText().toString();
                    Integer vehicle_id = null;
                    if (newRecipeMode) {
                        vehicle_id = addPositionInTable1(group, brand, number);
                    }
                    else {
                        Cursor cursor = DB.getTable1Pos(editNumber);
                        if (cursor.moveToFirst())
                            vehicle_id = (cursor.getInt(cursor.getColumnIndex(DB.TABLE1_COLUMN_ID)));
                        if (vehicle_id != null)
                            DB.updatePositionInTable1(vehicle_id,group, brand, number);
                        cursor.close();
                    }

                    int volumeSum = 0;
                    int count = LL_TABLE_2.getChildCount();
                    for (int idView = 0; idView < count; idView++) {
                        View view = LL_TABLE_2.getChildAt(idView);
                        final EditText editText_Volume = view.findViewById(R.id.editText_Volume);
                        Integer volume = Integer.valueOf(editText_Volume.getText().toString());
                        volumeSum = volumeSum + volume;
                        disableEditText(editText_Volume);
                        if (newRecipeMode)
                            DB.addPositionInTable2(vehicle_id, "cng", volume);
                        else {
                            if (rowsToDelete.size()>0) {
                                Log.d(LOG, "rowsToDelete" + rowsToDelete.size());
                                for (Integer integer : rowsToDelete)
                                    DB.deleteRowTable2(integer);
                            }
                            if (view.getTag() != null) {
                                int id = (int) view.getTag();
                                DB.updatePositionInTable2(id, vehicle_id, "cng", volume);
                            } else
                                DB.addPositionInTable2(vehicle_id, "cng", volume);
                        }
                    }
                    textView_VolumeVehicle.setText(String.valueOf(volumeSum));
                    closeNewEditMode();
                    newRecipeMode = false;
                    update_Spinners();
                }
            }
        });
    }

    private void create_Button_delete() {
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimClick(button_delete);
                if(!newRecipeMode) {
                    Cursor cursor = DB.getTable1Pos(editNumber);
                    Integer vehicle_id = null;
                    if (cursor.moveToFirst())
                        vehicle_id = (cursor.getInt(cursor.getColumnIndex(DB.TABLE1_COLUMN_ID)));
                    if (vehicle_id != null)
                        DB.deletePosition(vehicle_id);
                    cursor.close();
                }

                clearVehicleCard (true);
                closeNewEditMode();

                newRecipeMode = false;
                update_Spinners();
            }
        });
    }

    private void openNewEditMode() {
        disableEditText(editText_filter_number);
        button_filter.setEnabled(false);
        button_edit.setEnabled(false);
        button_new.setEnabled(false);
        if(newRecipeMode) {
            AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    button_new.setBackgroundResource(R.drawable.newvempty);
                }
            };
            AnimRemoveButtonNewEdit(button_edit, adapter);
        }
        else {
            AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            };
            AnimRemoveButtonNewEdit(button_new, adapter);
        }
        AnimShadowCVSearch(CV_search);

        enableEditText(editText_group);
        enableEditText(editText_brand);
        enableEditText(editText_number);

        AnimRevealLLButton(LL_button_panel);
        AnimAddButton(button_addRow);
        AnimAddButton(button_deleteRow);
        AnimAddButton(button_delete);
        AnimAddButton(button_ok);
    }

    private void closeNewEditMode() {
        enableEditText(editText_filter_number);
        button_filter.setEnabled(true);
        button_edit.setEnabled(true);
        button_new.setEnabled(true);
        if (newRecipeMode) {
            AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    button_new.setBackgroundResource(R.drawable.newv);
                }
            };
            AnimAddButtonNewEdit(button_edit, adapter);
        }
        else {
            AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            };
            AnimAddButtonNewEdit(button_new, adapter);
        }
        AnimRevealCVSearch(CV_search);

        disableEditText(editText_group);
        disableEditText(editText_brand);
        disableEditText(editText_number);

        AnimRemoveButton(button_addRow);
        AnimRemoveButton(button_deleteRow);
        AnimRemoveButton(button_delete);
        AnimRemoveButton(button_ok);
        AnimShadowLLButton(LL_button_panel);

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        editText_filter_number.removeTextChangedListener(textWatcher);
        editText_filter_number.setText(editText_number.getText().toString());
        editText_filter_number.addTextChangedListener(textWatcher);
    }

    private void addRow (int balloonNumber, Integer id, Integer volume, boolean anim) {
        Log.d(LOG, "addRow: balloonNumber = " + balloonNumber + "; id = " + id + "; volume = " + volume);
        View viewRowTable2 = inflater.inflate(R.layout.rowtable2, LL_TABLE_2, false);
        if (id!=null)
            viewRowTable2.setTag(id);
        final EditText editText_Volume = viewRowTable2.findViewById(R.id.editText_Volume);
        if (volume!=null) {
            disableEditText(editText_Volume);
            editText_Volume.setText(String.valueOf(volume));
        }
        else {
            enableEditText(editText_Volume);
        }
        final TextView textView_Balloon_number = viewRowTable2.findViewById(R.id.textView_Balloon_number);
        textView_Balloon_number.setText("Баллон № " + balloonNumber);
        LL_TABLE_2.addView(viewRowTable2);
        if(anim)
            AnimAddRow(viewRowTable2);
    }

    private boolean checkVehicleCardNullObject (){
        boolean OK = true;
        if(editText_group.getText().toString().equals("")) {
            AnimIncorrectParam(editText_group);
            toastIncorrectParam(this);
            OK = false;
        }

        if(editText_brand.getText().toString().equals("")) {
            AnimIncorrectParam(editText_brand);
            toastIncorrectParam(this);
            OK = false;
        }

        String number = null;
        if(editText_number.getText().toString().equals("")) {
            AnimIncorrectParam(editText_number);
            toastIncorrectParam(this);
            OK = false;
        }
        else number = editText_number.getText().toString();

        if(number!=null && !number.equals(editNumber)) {
            Cursor cursor = DB.getTable1Pos(number);
            if (cursor.moveToFirst())
                if(number.equals(cursor.getString(cursor.getColumnIndex(DB.TABLE1_COLUMN_NUMBER)))) {
                    AnimIncorrectParam(editText_number);
                    toastDuplicateNumber(this);
                    OK = false;
                }
            cursor.close();
        }

        int count = LL_TABLE_2.getChildCount();
        for (int idView = 0; idView < count; idView++) {
            View view = LL_TABLE_2.getChildAt(idView);
            final EditText editText_Volume = view.findViewById(R.id.editText_Volume);
            if (editText_Volume.getText().toString().equals("")) {
                AnimIncorrectParam(editText_Volume);
                toastIncorrectParam(this);
                OK = false;
            }
        }
        return OK;
    }


    private void initCardVolume(){
        radioGroupVolume = findViewById(R.id.radioGroup_Volume);
        radioButton_VolumeVehicle = findViewById(R.id.radioButton_VolumeVehicle);
        radioButton_VolumeEdit = findViewById(R.id.radioButton_VolumeEdit);

        imageView_rg1_rb1_img = findViewById(R.id.imageView_rg1_rb1_img);
        textView_rg1_rb1_description = findViewById(R.id.textView_rg1_rb1_description);
        textView_VolumeVehicle = findViewById(R.id.textView_VolumeVehicle);

        imageView_rg1_rb2_img = findViewById(R.id.imageView_rg1_rb2_img);
        textView_rg1_rb2_description = findViewById(R.id.textView_rg1_rb2_description);
        editText_VolumeEdit = findViewById(R.id.editText_VolumeEdit);

        create_RadioGroupVolume();
    }

    private void create_RadioGroupVolume(){
        radioGroupVolume.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_VolumeVehicle:
                        AnimReveal(radioButton_VolumeVehicle);
                        AnimReveal(imageView_rg1_rb1_img);
                        AnimReveal(textView_rg1_rb1_description);
                        AnimReveal(textView_VolumeVehicle);

                        AnimShadow(radioButton_VolumeEdit);
                        AnimShadow(imageView_rg1_rb2_img);
                        AnimShadow(textView_rg1_rb2_description);
                        AnimShadow(editText_VolumeEdit);
                        disableEditText(editText_VolumeEdit);
                        break;

                    case R.id.radioButton_VolumeEdit:
                        AnimShadow(radioButton_VolumeVehicle);
                        AnimShadow(imageView_rg1_rb1_img);
                        AnimShadow(textView_rg1_rb1_description);
                        AnimShadow(textView_VolumeVehicle);

                        AnimReveal(radioButton_VolumeEdit);
                        AnimReveal(imageView_rg1_rb2_img);
                        AnimReveal(textView_rg1_rb2_description);
                        AnimReveal(editText_VolumeEdit);
                        enableEditText(editText_VolumeEdit);
                        break;
                }
            }
        });
        radioButton_VolumeEdit.setChecked(true);
    }


    private void initCardPressure(){
        editText_Pressure = findViewById(R.id.editText_Pressure);
    }


    private void initCardTemperature(){
        radioGroupTemperature = findViewById(R.id.radioGroup_Temperature);
        radioButton_TemperatureGismeteo = findViewById(R.id.radioButton_TemperatureGismeteo);
        radioButton_TemperatureEdit = findViewById(R.id.radioButton_TemperatureEdit);

        imageView_rg2_rb1_img = findViewById(R.id.imageView_rg2_rb1_img);
        textView_city = findViewById(R.id.textView_city);
        textView_TemperatureOpenWeather = findViewById(R.id.textView_TemperatureGismeteo);

        imageView_rg2_rb2_img = findViewById(R.id.imageView_rg2_rb2_img);
        textView_rg2_rb2_description = findViewById(R.id.textView_rg2_rb2_description);
        editText_TemperatureEdit = findViewById(R.id.editText_TemperatureEdit);

        create_radioGroupTemperature();
    }

    private void create_radioGroupTemperature () {
        radioGroupTemperature.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_TemperatureGismeteo:
                        AnimReveal(radioButton_TemperatureGismeteo);
                        AnimReveal(imageView_rg2_rb1_img);
                        AnimReveal(textView_city);
                        AnimReveal(textView_TemperatureOpenWeather);

                        AnimShadow(radioButton_TemperatureEdit);
                        AnimShadow(imageView_rg2_rb2_img);
                        AnimShadow(textView_rg2_rb2_description);
                        AnimShadow(editText_TemperatureEdit);
                        disableEditText(editText_TemperatureEdit);
                        break;

                    case R.id.radioButton_TemperatureEdit:
                        AnimShadow(radioButton_TemperatureGismeteo);
                        AnimShadow(imageView_rg2_rb1_img);
                        AnimShadow(textView_city);
                        AnimShadow(textView_TemperatureOpenWeather);

                        AnimReveal(radioButton_TemperatureEdit);
                        AnimReveal(imageView_rg2_rb2_img);
                        AnimReveal(textView_rg2_rb2_description);
                        AnimReveal(editText_TemperatureEdit);
                        enableEditText(editText_TemperatureEdit);
                        break;
                }
            }
        });
        radioButton_TemperatureGismeteo.setChecked(true);
    }


    private void initResult() {
        textView_calculate = findViewById(R.id.textView_calculate);
        textView_volumeCNG = findViewById(R.id.textView_volumeCNG);
        button_Calculate = findViewById(R.id.button_calculate);
        create_button_Calculate();
    }

    private void create_button_Calculate() {
        button_Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimClick(button_Calculate);
                Double volumeBalloon = null;
                Double pressure = null;
                Double temperature = null;

                if (radioButton_VolumeVehicle.isChecked())
                    try {
                        volumeBalloon = Double.valueOf(textView_VolumeVehicle.getText().toString());
                    } catch (Exception e) {
                        AnimIncorrectParam(textView_VolumeVehicle);
                        toastIncorrectParam(MainActivity.this);
                    }

                if (radioButton_VolumeEdit.isChecked())
                    try {
                        volumeBalloon = Double.valueOf(editText_VolumeEdit.getText().toString());
                    } catch (Exception e) {
                        AnimIncorrectParam(editText_VolumeEdit);
                        toastIncorrectParam(MainActivity.this);
                    }

                try {
                    if (Double.parseDouble(editText_Pressure.getText().toString()) >= 10 && Double.parseDouble(editText_Pressure.getText().toString()) <= 200)
                        pressure = Double.parseDouble(editText_Pressure.getText().toString());
                    else {
                        AnimIncorrectParam(editText_Pressure);
                        toastIncorrectParam(MainActivity.this);
                    }
                } catch (Exception e) {
                    AnimIncorrectParam(editText_Pressure);
                    toastIncorrectParam(MainActivity.this);
                }

                if (radioButton_TemperatureGismeteo.isChecked())
                    try {
                        temperature = Double.valueOf(textView_TemperatureOpenWeather.getText().toString());
                    } catch (Exception e) {
                        AnimIncorrectParam(textView_TemperatureOpenWeather);
                        toastIncorrectParam(MainActivity.this);
                    }

                if (radioButton_TemperatureEdit.isChecked())
                    try {
                        if (Double.parseDouble(editText_TemperatureEdit.getText().toString()) >= -30 && Double.parseDouble(editText_TemperatureEdit.getText().toString()) <= 40)
                            temperature = Double.parseDouble(editText_TemperatureEdit.getText().toString());
                        else {
                            AnimIncorrectParam(editText_TemperatureEdit);
                            toastIncorrectParam(MainActivity.this);
                        }
                    } catch (Exception e) {
                        AnimIncorrectParam(editText_TemperatureEdit);
                        toastIncorrectParam(MainActivity.this);
                    }

                if (volumeBalloon != null && pressure != null && temperature != null) {
                    DecimalFormat format = new DecimalFormat("#.#");
                    format.setRoundingMode(RoundingMode.HALF_UP);
                    Double cng = converter.convert(temperature, pressure, volumeBalloon);
                    textView_calculate.setText("Расчитано: V = " + volumeBalloon+", P = " + pressure + ", t° = " + temperature);
                    textView_volumeCNG.setText(format.format(cng) + " м\u00B3");
                }
            }
        });
    }

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.d(LOG, "onLocationChanged");
            if (location == null)
                return;
            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                Log.d(LOG,"GPS_PROVIDER: Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude());
                getWeather(location.getLatitude(), location.getLongitude(), textView_city, textView_TemperatureOpenWeather);
            } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                Log.d(LOG,"NETWORK_PROVIDER: Latitude = " + location.getLatitude() + ", Longitude = " + location.getLongitude());
                getWeather(location.getLatitude(), location.getLongitude(), textView_city, textView_TemperatureOpenWeather);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(LOG, "onProviderDisabled: provider = " + locationManager.isProviderEnabled(provider));
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(LOG, "onProviderEnabled: provider = " + locationManager.isProviderEnabled(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public boolean requestPermissions() {
        permissionGPS = false;
        permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionFineLocation == PackageManager.PERMISSION_GRANTED)
        {
            permissionGPS = true;
        } else
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION_FINE_LOCATION);
        }
        return permissionGPS;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                permissionGPS = true;
            }
            else
                textView_city.setText("Отсутсвует разрешение");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionFineLocation == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 1000, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 1000, locationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionFineLocation == PackageManager.PERMISSION_GRANTED)
            locationManager.removeUpdates(locationListener);
    }
}