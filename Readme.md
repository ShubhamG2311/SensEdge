# SensEdge - Sensor-based Multi Activity Annotator

# Introduction

SensEdge is a sensors-based multi-activity annotator app designed to record accelerometer and gyroscope data in real-time while performing various activities. The app allows users to add multiple activities dynamically, record the sensor data while performing the activity, and annotate it with the corresponding activity name. A CSV file that contains all the recorded data is created, which can be visualized in a graph at any time. The app provides a home display that shows GPS, accelerometer, and gyroscope data for all three axes. With features like Dark Mode and Light Mode, the app has a fantastic and immersive user interface.

![image](https://drive.google.com/uc?export=view&id=1jvB9F0tCBQmPLcc5Du_HSWEwq9mEkpD-)

# Problem Statement

The motivation behind developing this app is the increasing demand for sensor-based data collection in various fields such as sports, health, and research. However, the process of collecting and annotating this data manually is time-consuming and prone to errors. This collected and annotated data has huge application impacts in various field. In research field, the app can be used to collect and analyze data based on human activities and movements, which can be useful in fields such as various technology gadgets, therapy and smart home technology. In real life, the app can be used by users who are interested fitness freak and aims to keep the track over their daily activites and movements.

# Design/App Features

The app is created with a Immersive and User-Friendly UI which help the user to easily use the app. There are two main activity in the app. One is the Main Activity and second is Graph Activity. The Main Activity serves as the homepage and has all the major portion of the features such as Add Button, Sensors Data Display Area, Selecting the CSV Files, Light and Dark Mode etc. While the Graph Activity includes Visualization of the data in form of graphs. There are two graph areas in the activity and they are for two sensors data i.e. Accelerometer and Gyroscope. It also has a “Share” Button for sharing the graphs. 

We first have “Add” button by which a user can dynamically add the activities he/she wants. Add buttons and all the dynamically added buttons will be viewed in a Horizontal Scroll View. Then comes the main part of the app which is the area for displaying the Sensors data. We have 3 card views each of them for GPS Data, Accelerometer Data and Gyroscope Data respectively. All three axes data is shown. User then can see a “Select” button for selecting the CSV file he wants to visualize. There is a text box beside it which displays the name of the selected file. There is a submit button beside the text box which will take us to another activity i.e. Graph Activity. Upon selecting atleast one activity button, a “Start Button” will appear on the bottom right for recording the sensor data. There is also a feature of switching between the Dark Mode and Light Mode.

# Dependencies/Implementations

```jsx
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])

    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'com.google.android.gms:play-services-location:17.0.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'androidx.recyclerview:recyclerview:1.3.0'
    implementation 'androidx.wear.tiles:tiles-material:1.1.0-alpha04'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'androidx.annotation:annotation:1.4.0'
    implementation 'com.google.code.gson:gson:2.8.9'
    implementation 'androidx.documentfile:documentfile:1.0.1'
    implementation 'com.jjoe64:graphview:4.2.2'
    implementation 'com.opencsv:opencsv:5.5.2'

}
```

# How to run the project?

1. Install Android Studio
2. Clone/Download the project to your local machine and unzip the folder
3. Open Android Studio 
4. Click on File→New→Import Project
5. Select path where the project is downloaded and select it
6. The Gradle Build will be initiated and the app will run automatically

# Links

- [Teaser Video](https://drive.google.com/file/d/1a9NlZpPxxE6FnAUzUJcOyaEUbfIcUH9n/view?usp=sharing)
- [Presentation](https://docs.google.com/presentation/d/1vwcc-d5AfY3o2KJb5Dom372esNcN-yfT/edit?usp=share_link&ouid=113743890143882298940&rtpof=true&sd=true)