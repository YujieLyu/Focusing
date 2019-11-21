# Focusing

Focusing is an Android application for self-control of smartphone application usage for my graduation computing project.  

## Getting Started

#### **Prerequisites**

1. Android studio version: 3.3.1+
2. The minimal SDK version : API 26
3. Gradle version: 3.3.1

#### **Compiling and Installing**

1. Download the source code.
2. Open it through Android studio.
3. Select connected devices (your testing phone).
4. Run the program.

## Running tests

#### **Preparation:**

Pre-set a "**Reading**" profile for testing the concurrency process of instant focus task and profile:

- Apps to block: Instagram
- Start time: 6:00
- End time: 23:50
- Repeat: Select the current day of week

#### **Part A: Focus Now**

1. Click "**Focus Now**" on the homepage.

2. Select "Instagram", "Facebook" to block.

3. Set 1 hour for this focusing task.

4. Click "**Start**"

   Check if:

   - Instagram and Facebook are both blocked.
   - The countdown views of Instagram and Facebook both show 1 hour.

5. Kill **Focusing**(remove this running app from system recent task list):

   - Both of them can still be blocked.

6. After 1 hour, a finish view will show up.

7. Then try to open Instagram and Facebook:

   - Facebook can be open as usual.
   - Instagram is blocked because of the "**Reading**" profile.
   -  The countdown time is the interval from now to 23:50.

8. Cancel focusing task:

   - Instagram and Facebook can be open
   
Demo:
	<video id="video" controls="" preload="none">
	<source id="mp4" src="https://github.com/YujieLyu/Focusing/blob/master/focusnow.mp4" type="video/mp4">
	</video>

#### **Part B: My Profiles**

1. Click "**My profiles**", add new profile：

   - Name: testing
   - Block: Facebook, Spotify
   - Start time: current time - 1hour
   - End time: current time + 1hour
   - Repeat: Sun. Mon. and today.
   - Check if:
     - New profile is listed in profile list.

2. Open Facebook，Spotify

   Check if:

   - All blocked with showing correct countdown time.

3. Kill **Focusing**:

   Check if:

   - All blocked with showing correct countdown time.

4. After the profile end time:

   Check if:

   - Facebook and Spotify can be open as usual.

 Demo:

     <video src="https://github.com/YujieLyu/Focusing/blob/master/profile.mp4" controls="controls" width="640" height="320" autoplay="autoplay"></video>
    

#### **Part C: My schedule**

1. Click on the calendar for the day, Sunday and Monday:

   Check if:

   - The profiles listed match the profile Settings for the selected date.

   - Be able to open certain profile item to look through or modify when clicking certain items.
   
 Demo:

     <video src="https://github.com/YujieLyu/Focusing/blob/master/schedule.mp4" controls="controls" width="640" height="320" autoplay="autoplay"></video>

  #### 

#### **Part D: Time statistic**

1. Open "**Time statistics**":

   Check if:

   - Focusing time is increased.

1. Open Instagram and running 10 minutes

   Check if：

   - Instagram's screen time is increased by 10 minutes.
   - Instagram's open times out focus time add 1.

 Demo:

     <video src="https://github.com/YujieLyu/Focusing/blob/master/statistic.mp4" controls="controls" width="640" height="320" autoplay="autoplay"></video>


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

