# Focusing

Focusing is an Android application for self-control of smartphone application usage.  

## Getting Started

#### **Prerequisites**

1. A computer installed with Android studio version 3.3.1
2. Google Pixel 2 XL with Oreo+ (other android phones may have user interface display issues)

#### **Installing**

1. Download the source code
2. Open it through Android studio
3. Select connected devices (your testing phone)
4. Run the program

## Running tests

#### **Preparation:**

Preset a "Reading" profile for test the concurrency process of instant focus task and profile:

- Apps to block: Instagram
- Start time: 6:00
- End time: 23:50
- Repeat: Select the current day of week

#### **Part A: Focus Now**

1. Click "Focus Now" on the homepage.

2. Select "Instagram", "Facebook" to block

3. Set 1 hour for this focusing task

4. Click "Start"

   Check:

   - Instagram and Facebook are both blocked 
   - The countdown views of Instagram and Facebook both show1 hour

5. Kill Foucsing:

   - Both of them can be blocked

6. After 1 hour, a finish view will show up

7. Then try to open Instagram and Facebook:

   - Facebook can be open as usual
   - Instagram is blocked because of the "Reading" profile and the countdown time is the interval from now to 23:50

8. Cancel focusing task:

   - Instagram and Facebook can be open

     

#### **Part B: My Profiles**

1. Click "My profiles", add new profile：

   - Name: testing
   - Block: Facebook, spotify
   - Start time: current time-1hour
   - End time: current time+1hour
   - Repeat:everyday
   - Check:
     - New profile is listed in profile list

2. Open Facebook，Spotify

   Check:

   - All blocked with showing correct countdown time

3. Kill Focusing:

   Check:

   - All blocked with showing correct countdown time

4. After the profile end time:

   Check:

   - Facebook and spotify can be open as usual

     

#### **Part C: My schedule**

1. Click on the calendar for the day, yesterday and tomorrow:

   Check:

   - The profiles listed match the profile Settings for the selected date

   - Be able to open certain profile item to look through or modify

     #### 

#### **Part D: Time statistic**

1. Open "Time statistics":

   Check:

   - Focusing time is increased

1. Open Instagram and running for 10 minutes

   Check：

   - Instagram's screen time is increased by 10 minutes
   - Instagram's open times out focus time adds 1



## Built With

* Gradle

```java
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:28.0.0'
    implementation 'com.android.support:support-v4:28.0.0'
    implementation 'com.android.support:design:28.0.0'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
    //Database LitePal
    implementation 'org.litepal.android:java:3.0.0'
    implementation 'cn.qqtheme.framework:ColorPicker:1.1.3'
	// time count down view
    implementation 'com.github.iwgang:countdownview:2.1.6'
    implementation 'com.contrarywind:Android-PickerView:4.1.7'
    //bar chart
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0-alpha'
    // debug database
    debugImplementation 'com.amitshekhar.android:debug-db:1.0.4'
}

```

