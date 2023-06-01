package com.lemniscate.dc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private GraphView graph1, graph2;
    private ArrayList<Double> axList, ayList, azList, gxList, gyList, gzList;
    private ArrayList<String> latList, longList;
    private int rowCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Get file path from previous activity
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("filePath");
        ArrayList<String> activities = intent.getStringArrayListExtra("activitySelected");

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + fileName;

        // Assuming that csvFilePath contains the path to the CSV file
        // Read CSV file
        // Read CSV file
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            List<String[]> csvData = reader.readAll();

// Initialize GraphView and set properties for Graph 1
            GraphView graph1 = findViewById(R.id.graph1);
            graph1.getGridLabelRenderer().setHorizontalAxisTitle("Sample");
            graph1.getGridLabelRenderer().setVerticalAxisTitle("Acceleration");
            graph1.getViewport().setScrollable(true);
            graph1.getViewport().setScalable(true);
            graph1.setBackgroundColor(Color.WHITE);

// Initialize GraphView and set properties for Graph 2
            GraphView graph2 = findViewById(R.id.graph2);
            graph2.getGridLabelRenderer().setHorizontalAxisTitle("Sample");
            graph2.getGridLabelRenderer().setVerticalAxisTitle("Gyro");
            graph2.getViewport().setScrollable(true);
            graph2.getViewport().setScalable(true);
            graph2.setBackgroundColor(Color.WHITE);

//            String activityName="";

// Create data series for Graph 1
            LineGraphSeries<DataPoint> axSeries = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> aySeries = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> azSeries = new LineGraphSeries<>();

// Create data series for Graph 2
            LineGraphSeries<DataPoint> gxSeries = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> gySeries = new LineGraphSeries<>();
            LineGraphSeries<DataPoint> gzSeries = new LineGraphSeries<>();
            String tempActivityName="";
            String[] tempRow=csvData.get(0);
            int gz1=0,timestamp=0;
            for (int i=0; i<tempRow.length; i++)
            {
                if (tempRow[i]=="GZ ")
                {
                    gz1=i;
                }
                else if (tempRow[i]=="Timestamp ")
                {
                    timestamp=i;
                }
            }
            int number=timestamp-gz1-1;
//            Toast.makeText(this, number , Toast.LENGTH_SHORT).show();
// Loop through CSV data and add data points to series for each column
            for (int i = 1; i < csvData.size(); i++) {
                String[] row = csvData.get(i);
                String temp="";
                int rowLength = row.length;
                if (rowLength<7)
                {
                    continue;
                }
                double x = (double) i;
//                activityName=row[6];
                // Add data points to Ax, Ay, Az series
                double ax = Double.parseDouble(row[1]);
                double ay = Double.parseDouble(row[2]);
                double az = Double.parseDouble(row[3]);
                axSeries.appendData(new DataPoint(x, ax), true, csvData.size());
                aySeries.appendData(new DataPoint(x, ay), true, csvData.size());
                azSeries.appendData(new DataPoint(x, az), true, csvData.size());

                // Add data points to Gx, Gy, Gz series
                double gx = Double.parseDouble(row[4]);
                double gy = Double.parseDouble(row[5]);
                double gz = Double.parseDouble(row[6]);
                gxSeries.appendData(new DataPoint(x, gx), true, csvData.size());
                gySeries.appendData(new DataPoint(x, gy), true, csvData.size());
                gzSeries.appendData(new DataPoint(x, gz), true, csvData.size());
                for (int j=7; j<row.length-1; j++)
                {
                    temp=temp+row[j]+", ";
                }
                temp=temp+row[row.length-1];
                tempActivityName=temp;
            }
            String activityName="";
            activityName=activityName+tempActivityName;

// Add Ax, Ay, Az series to Graph 1
            axSeries.setTitle("Ax");
            axSeries.setColor(Color.BLUE);
            graph1.addSeries(axSeries);
            aySeries.setTitle("Ay");
            aySeries.setColor(Color.GREEN);
            graph1.addSeries(aySeries);
            azSeries.setTitle("Az");
            azSeries.setColor(Color.RED);
            graph1.addSeries(azSeries);
            graph1.setTitle("Activities: " + activityName);

// Add Gx, Gy, Gz series to Graph 2
            gxSeries.setTitle("Gx");
            gxSeries.setColor(Color.BLUE);
            graph2.addSeries(gxSeries);
            gySeries.setTitle("Gy");
            gySeries.setColor(Color.GREEN);
            graph2.addSeries(gySeries);
            gzSeries.setTitle("Gz");
            gzSeries.setColor(Color.RED);
            graph2.addSeries(gzSeries);
            graph2.setTitle("Activities: " + activityName);

//            int myInt = 10;


//            TextView activityTextView=findViewById(R.id.activityName);
//            activityTextView.setText(activityName);

            Button shareButton = findViewById(R.id.shareButton);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Code for sharing the graph images
                    // Create Bitmaps of the graphs
                    Bitmap graph1Bitmap = graph1.takeSnapshot();
                    Bitmap graph2Bitmap = graph2.takeSnapshot();

// Create a list of URIs for the graph images
                    ArrayList<Uri> imageUris = new ArrayList<>();
                    imageUris.add(getImageUri(getApplicationContext(), graph1Bitmap));
                    imageUris.add(getImageUri(getApplicationContext(), graph2Bitmap));

// Create an Intent to share the graph images
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
//                    shareIntent.setAction(Intent.ACTION_SEND);

                    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUris.get(0));

                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Share graphs"));

                }
            });




// Set legend and visibility for both graphs
            graph1.getLegendRenderer().setVisible(true);
            graph2.getLegendRenderer().setVisible(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }


    }
    //    public Uri getImageUri(Context context, Bitmap image) {
//        if (context == null) {
////            Toast.makeText(this, "context is null", Toast.LENGTH_SHORT).show();
//        } else {
////            Toast.makeText(this, "context is not null", Toast.LENGTH_SHORT).show();
//        }
//
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.PNG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
//        if (path != null) {
////            Toast.makeText(this, "Path is Null", Toast.LENGTH_SHORT).show();
//            return Uri.parse(path);
//        } else {
////            Toast.makeText(this, "Path is not Null", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,"IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }
}
