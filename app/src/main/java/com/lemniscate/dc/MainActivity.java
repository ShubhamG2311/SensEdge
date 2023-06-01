package com.lemniscate.dc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.documentfile.provider.DocumentFile;





import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.view.View;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.widget.ToggleButton;

import android.graphics.drawable.GradientDrawable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.example.myapp.ButtonData;









import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManagers;
    private SensorManager sensorManagerG;
    private Sensor senAccelerometor;
    private Sensor senGyroscope;
    private TextView GPSx;
    private TextView GPSy;
    private TextView GPS_loc;

    private String activityName="";
    private FusedLocationProviderClient MyFusedLocationClient;

    private long lastUpdate = 0;
    int count=0;
    private long lastUpdate_gyro = 0;
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    public int i = 0;

    private int idxTemp;
    public int c = 0;
    public String fileString = "";

    //    LinearLayout layout = findViewById(R.id.tag_layout);
//    Button addButton = findViewById(R.id.add_button);
//    SharedPreferences prefs;
    private static final int REQUEST_CODE_CSV = 100;
    private LinearLayout llButtons;

    private String csvFilePath="";
    private Button addButton;
    private SharedPreferences prefs;

    ArrayList<String> activitySelected=new ArrayList<>();;

    private SharedPreferences idxPrefs;
    private Uri selectedFile;


    public class ButtonData {
        private int id;
        private String label;
        private boolean state;

        private String csvFilePath="";



        public ButtonData(int id, String label, boolean state) {
            this.id = id;
            this.label = label;
            this.state = state;
        }

        public int getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public boolean getState() {
            return state;
        }
    }





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedFile=null;

        llButtons=findViewById(R.id.tag_layout);

        // Create a new instance of FusedLocationProviderClient
        MyFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Get the system's sensor service and assign it to sensorManagers and sensorManagerG
        sensorManagers = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManagerG = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Check if the sensorManagers and senGyroscope are not null
        assert sensorManagers != null;
        // Get the default Accelerometer and Gyroscope sensors from the system
        senAccelerometor = sensorManagers.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senGyroscope = sensorManagerG.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        //Assking for Permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, locationRequestCode);
        } else {
            Toast T = Toast.makeText(getApplicationContext(), "Location & file access Permission Granted", Toast.LENGTH_SHORT);
            T.show();
        }


        // Get the dropdown button
        Button dropdownButton = findViewById(R.id.dropdown_button);
        // Add click listener to the dropdown button
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // Set the title and message
                builder.setTitle("Select a Device");
                builder.setMessage("Please select a Device:");

                // Create the radio button
                final RadioButton radioButton = new RadioButton(MainActivity.this);
                radioButton.setText("Phone");
                radioButton.setChecked(true);

                // Add the radio button to the dialog
                builder.setView(radioButton);

                // Set the positive button action
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do something when the positive button is clicked
                        if (radioButton.isChecked()) {
                            Toast.makeText(MainActivity.this, "Phone is selected", Toast.LENGTH_SHORT).show();
                        } else {
                        }
                    }
                });

                // Set the negative button action
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do something when the negative button is clicked
                        dialogInterface.dismiss();
                    }
                });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Setting the default theme of the UI
        ChangeTheme();
        Switch theme = (Switch) findViewById(R.id.sw_theme);
        theme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                c = 1;
                ChangeTheme();
            } else {
                c = 0;
                ChangeTheme();
            }
        });
        switchAppearance();
        Switch toggle = (Switch) findViewById(R.id.sw);
//        switchAppearance();
        toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                i = 1;
                onResume();
//                fileString="";
            } else {
                i = 0;
                onPause();
                SharedPreferences.Editor editor = idxPrefs.edit();
                editor.putInt("LAST_INDEX", idxTemp+1);
                editor.apply();

            }
        });
        llButtons = findViewById(R.id.tag_layout);
        addButton = findViewById(R.id.add_button);
        prefs = getPreferences(Context.MODE_PRIVATE);
        //Load all the buttons stored in the Shared Preferences
        loadButtonsFromPrefs();

//        Button addButton = findViewById(R.id.add_button);
        //Add new buttons for activites in the Horziontal Scroll View dynamically by user
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new toggle button with user-defined label
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter Button Label");

                final EditText input = new EditText(MainActivity.this);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String label = input.getText().toString();
                        addToggleButton(label);
                        saveButtonsToPrefs();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        //Selecting the CSV file to visualize the data using UI System Picker
        Button selectCSVButton = findViewById(R.id.selectButton);
        selectCSVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Select CSV File"), REQUEST_CODE_CSV);
            }
        });
        //Making the Submit Button Functional so that new Graph Activity is created for visualizing the data of the selected CSV File
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etFileName = findViewById(R.id.fileNameTextView);
                String fileName = etFileName.getText().toString();
                if (!fileName.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                    intent.putExtra("filePath", csvFilePath);
                    intent.putExtra("activitySelected", activitySelected);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Please select a file", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("MainActivity", "idx value before writing to CSV: " + idx);
        if (requestCode == REQUEST_CODE_CSV && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String filePath = null;
            if (uri != null) {
//                csvFilePath=uri.getPath();
                if ("content".equals(uri.getScheme())) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(uri, new String[]{MediaStore.Files.FileColumns.DISPLAY_NAME}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            int index = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
                            if (index >= 0) {
                                filePath = cursor.getString(index);
//                                csvFilePath=filePath;
                            }
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                } else if ("file".equals(uri.getScheme())) {
                    filePath = uri.getPath();
//                    csvFilePath=filePath;
                }
            }

            // Use the file path for your app's purpose

            // Update the EditText view with the file name
            csvFilePath=filePath;
            String fileName = filePath != null ? filePath.substring(filePath.lastIndexOf("/") + 1) : "";
            EditText etFileName = findViewById(R.id.fileNameTextView);
            if (etFileName != null) {
                etFileName.setText(fileName);
            }
        }
    }



    private void addToggleButton(String label) {
        // Create a new ToggleButton with user-defined label
        ToggleButton toggleButton = new ToggleButton(this);
        toggleButton.setText(label);
        toggleButton.setTextOn(label);
        toggleButton.setTextOff(label);

        // Set unique ID to the button
        int id = View.generateViewId();
        toggleButton.setId(id);

        // Add button to the linear layout
        llButtons.addView(toggleButton);

        // Set the OnClickListener for the button
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the state of the button
                boolean state = ((ToggleButton) v).isChecked();
                // Do the action based on button state
                switchAppearance();
            }
        });
    }

    private void loadButtonsFromPrefs() {
        // Load the button information from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("BUTTONS_PREFS", MODE_PRIVATE);
        String buttonDataString = prefs.getString("BUTTON_DATA", "");
        if (!buttonDataString.isEmpty()) {
            // Parse the button data and add buttons to LinearLayout
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ButtonData>>(){}.getType();
            ArrayList<ButtonData> buttonDataList = gson.fromJson(buttonDataString, type);
            for (ButtonData buttonData : buttonDataList) {
                ToggleButton button = new ToggleButton(this);
                button.setId(buttonData.getId());
                button.setTextOn(buttonData.getLabel());
                button.setTextOff(buttonData.getLabel());
//                button.setChecked(buttonData.getState());
                button.setChecked(false);
//                String s=buttonData.getLabel();
                llButtons.addView(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toggle the state of the button
                        boolean state = ((ToggleButton) v).isChecked();
                        // Do the action based on button state
                        switchAppearance();
                    }
                });
            }
        }
    }



    private void saveButtonsToPrefs() {
        // Get the button information from LinearLayout and store it in SharedPreferences
        ArrayList<ButtonData> buttonDataList = new ArrayList<>();
        for (int i = 0; i < llButtons.getChildCount(); i++) {
            View view = llButtons.getChildAt(i);
            if (view instanceof ToggleButton) {
                ToggleButton button = (ToggleButton) view;
                int id = button.getId();
                String label = button.getTextOn().toString();
                boolean state = button.isChecked();
                ButtonData buttonData = new ButtonData(id, label, state);
                buttonDataList.add(buttonData);
            }
        }
        Gson gson = new Gson();
        String buttonDataString = gson.toJson(buttonDataList);
        SharedPreferences.Editor editor = getSharedPreferences("BUTTONS_PREFS", MODE_PRIVATE).edit();
        editor.putString("BUTTON_DATA", buttonDataString);
        editor.apply();
    }





    public void FileWriters(String str) {
        SimpleDateFormat dateObj = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        String date = dateObj.format(calendar.getTime());

        idxPrefs=getPreferences(Context.MODE_PRIVATE);

        idxTemp = idxPrefs.getInt("LAST_INDEX", 0);
//        File folder = new File(getExternalFilesDir(null), "MPC Project");
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        if (!folder.exists()) {
            folder.mkdirs(); // create the folder if it doesn't exist
        }

        final File file = new File(folder, "Data " + idxTemp + ".csv");
//        Log.d("MyApp", "Value of idx at CSV write: " + idxTemp);
        try {
            if(!file.exists()){
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file, true);
                String temporary="AX, AY, AZ, GX, GY, GZ";
                int len=activitySelected.size();
                for (int i=0; i<len; i++)
                {
                    temporary=temporary+", Activity"+(i+1);
                }
                temporary=temporary+", Timestamp";
                temporary=temporary+"\n";
                fOut.write(temporary.getBytes());
                fOut.close();


            }
            FileOutputStream fOut = new FileOutputStream(file, true);
            // create a SimpleDateFormat object with the desired format
            fOut.write(str.getBytes());
            fOut.flush();
            fOut.close();
//
//
        } catch (IOException e){
            Log.e("Exception", "File write failed");
        }
        fileString = "";
    }

    public static void removeRowFromCsvFile(File file, int rowIndex) {
        try {
            // Read the file
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();

            // Remove the row at the specified index
            if ((rowIndex >= 0 && rowIndex < lines.size()) && lines.size()>1) {
                lines.remove(rowIndex);
            }
            if (lines.size()>1)
            {
                lines.remove(lines.size()-1);
            }

            // Write the updated content back to the file
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (String s : lines) {
                bw.write(s);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void removeNoise(File file, int rowIndex) {
        try {
            // Read the file
            BufferedReader br = new BufferedReader(new FileReader(file));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();

            // Remove the row at the specified index
            if ((rowIndex >= 0 && rowIndex < lines.size()) && lines.size()>1) {
                lines.remove(rowIndex);
            }
            if (lines.size()>1)
            {
                lines.remove(lines.size()-1);
            }

            // Write the updated content back to the file
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (String s : lines) {
                bw.write(s);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected void onPause() {
        super.onPause();
        sensorManagers.unregisterListener(this);
//        fileString="";
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        final File file = new File(folder, "Data " + idxTemp + ".csv");
        if (file.exists())
        {
            removeRowFromCsvFile(file,1);
            removeNoise(file,1);
        }
    }

    protected void onResume() {
        super.onResume();
//        fileString="";
        if (i == 1) {
            sensorManagers.registerListener(this, senAccelerometor, SensorManager.SENSOR_DELAY_UI );
            sensorManagers.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_UI );
        }
    }


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;

    public void GetNewLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request for the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with getting the location
            MyFusedLocationClient.flushLocations();
            MyFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    wayLatitude = location.getLatitude();
                    wayLongitude = location.getLongitude();

                    GPSx = (TextView) findViewById(R.id.gpsx);
                    GPSx.setText("" + wayLatitude);

                    GPSy = (TextView) findViewById(R.id.gpsy);
                    GPSy.setText("" + wayLongitude);

//                    fileString = fileString + wayLatitude + ", " + wayLongitude + ", ";
//                    FileWriters(fileString);
                    GPS_loc = (TextView) findViewById(R.id.city);
                    GPS_loc.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with getting the location
                GetNewLocation();
            } else {
                // Permission denied, handle the situation
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {



        Sensor mySensor = sensorEvent.sensor;
        Sensor GSensor = sensorEvent.sensor;


        if (GSensor.getType()==Sensor.TYPE_GYROSCOPE){
            float gx = sensorEvent.values[0];
            float gy = sensorEvent.values[1];
            float gz = sensorEvent.values[2];

            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate_gyro > 300)) {

                lastUpdate_gyro = currentTime;


                GetNewLocation();                                   //Just to update the location ;P


                String sX = Float.toString(gx);
                TextView text = (TextView) findViewById(R.id.gx);
                text.setText(sX);

                String sY = Float.toString(gy);
                text = (TextView) findViewById(R.id.gy);
                text.setText(sY);

                String sZ = Float.toString(gz);
                text = (TextView) findViewById(R.id.gz);
                text.setText(sZ);

                fileString = fileString + sX + ", " + sY + ", " + sZ + ", ";


            }
            FileWriters(fileString);
        }

        if(mySensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdate) > 300){
                long timeDiff = currentTime - lastUpdate;
                lastUpdate = currentTime;

                String sX = Float.toString(x);
                TextView text = (TextView) findViewById(R.id.ax);
                text.setText(sX);

//                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//                    int progress = (int) (((-1 * x) + 10) * 10000);
//                    progressBar.setProgress(progress);x

                String sY = Float.toString(y);
                text = (TextView) findViewById(R.id.ay);
                text.setText(sY);

                String sZ = Float.toString(z);
                text = (TextView) findViewById(R.id.az);
                text.setText(sZ);

                fileString = fileString + sX + ", " + sY + ", " + sZ + ", ";
                for (int i=0; i<activitySelected.size()-1; i++)
                {
                    fileString = fileString + activitySelected.get(i) + ", ";
                }
                fileString = fileString + activitySelected.get(activitySelected.size()-1) + ", ";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

// get the current system time
                Date now = new Date();

                // Create a SimpleDateFormat object with the desired format
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                // Format the date as a string using the SimpleDateFormat object
                String currentTimeString = formatter.format(now);
//                currentTimeString=currentTimeString+", ";
                fileString=fileString+currentTimeString+"\n";
//                fileString = fileString + "\n";

            }
            FileWriters(fileString);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void switchAppearance()
    {
        ArrayList<String> tempActivities = new ArrayList<>();;
        Switch mySwitch=findViewById(R.id.sw);
        int cnt = 0;
        if (llButtons!=null)
        {
            for (int i = 0; i < llButtons.getChildCount(); i++) {
                View view = llButtons.getChildAt(i);
                if (view instanceof ToggleButton) {
                    ToggleButton toggleButton = (ToggleButton) view;
                    if (toggleButton.isChecked()) {
                        cnt++;
                    }
                }
            }
        }

        if (cnt>=1)
        {
            mySwitch.setVisibility(View.VISIBLE);
            String tempActivity="";
            for (int i = 0; i < llButtons.getChildCount(); i++) {
                View view = llButtons.getChildAt(i);
                if (view instanceof ToggleButton) {
                    ToggleButton toggleButton = (ToggleButton) view;
                    if (toggleButton.isChecked()) {
                        tempActivity = toggleButton.getText().toString();
                        tempActivities.add(tempActivity);
//                        break;
                    }
                }
            }
            activitySelected=tempActivities;
//            Toast.makeText(MainActivity.this, activitySelected, Toast.LENGTH_SHORT).show();
        }
        else
        {
            mySwitch.setChecked(false);
            mySwitch.setVisibility(View.GONE);
        }
    }

    public void ChangeTheme(){
        if(c==1){

            Switch theme = (Switch) findViewById(R.id.sw_theme);
            theme.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            TextView colorDark = (TextView) findViewById(R.id.gpsx);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            colorDark = (TextView) findViewById(R.id.gpsy);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            colorDark = (TextView) findViewById(R.id.gx);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            colorDark = (TextView) findViewById(R.id.gy);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            colorDark = (TextView) findViewById(R.id.gz);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            colorDark = (TextView) findViewById(R.id.ax);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            colorDark = (TextView) findViewById(R.id.ay);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            colorDark = (TextView) findViewById(R.id.az);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            colorDark = (TextView) findViewById(R.id.city);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_main));

            colorDark = (TextView) findViewById(R.id.city2);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.city3);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.gps);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.textView3);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.textView5);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.textView11);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.textView13);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.textView17);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.textView18);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.textView19);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.textView20);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            colorDark = (TextView) findViewById(R.id.city2);
            colorDark.setTextColor(getResources().getColor(R.color.lite_mode_Text_sub));

            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.c_Layout);
            layout.setBackgroundColor(getResources().getColor(R.color.lite_mode_bg));

            CardView cv = (CardView) findViewById(R.id.cardView);
            cv.setCardBackgroundColor(getResources().getColor(R.color.lite_mode_card));




        }
        else{

            Switch theme = (Switch) findViewById(R.id.sw_theme);
            theme.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            TextView colorDark = (TextView) findViewById(R.id.gpsx);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_Text_Main));

            colorDark = (TextView) findViewById(R.id.gpsy);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_Text_Main));

            colorDark = (TextView) findViewById(R.id.gx);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_Text_Main));

            colorDark = (TextView) findViewById(R.id.gy);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_Text_Main));

            colorDark = (TextView) findViewById(R.id.gz);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_Text_Main));

            colorDark = (TextView) findViewById(R.id.ax);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_Text_Main));

            colorDark = (TextView) findViewById(R.id.ay);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_Text_Main));

            colorDark = (TextView) findViewById(R.id.az);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_Text_Main));

            colorDark = (TextView) findViewById(R.id.city);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_Text_Main));

            colorDark = (TextView) findViewById(R.id.city2);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.city3);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.gps);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.textView3);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.textView5);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.textView11);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.textView13);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.textView17);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.textView18);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.textView19);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.textView20);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            colorDark = (TextView) findViewById(R.id.city2);
            colorDark.setTextColor(getResources().getColor(R.color.dark_mode_text_sub));

            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.c_Layout);
            layout.setBackgroundColor(getResources().getColor(R.color.dark_mode_bg));

            CardView cv = (CardView) findViewById(R.id.cardView);
            cv.setCardBackgroundColor(getResources().getColor(R.color.dark_mode_card));
        }
    }
}