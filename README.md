# CPR Manikin conversion project

The project's purpose was to convert "dumb" Annie resuscitation manikins to "smart" manikins that provide information regarding CPR practice in a more convenient way.

This project consisted of a CPR Manikin with an Arduino controller, a mobile phone application and a backend server for processing and storing data.


I was responsible for writing the mobile phone application and the backend server.

##Backend:

Spring boot server written in Java to handle the API requests from the front-end application and works with MongoDB.

Written in a layered design (Boundary <-> Controller <-> Service <-> Data access <-> DB)

Has a JUnit test suite as well.


##Frontend:

Mobile device application, written in React-native with Redux and React-native ble plx library for communication with the arduino controller.

![image](https://user-images.githubusercontent.com/17098942/207594304-c7815a72-891b-45cc-9400-3aaa86ca7afe.png)



Requirements:
Node version 16.15.0

Android mobile phone supporting Bluetooth 4.0+

Micro-USB Cable


Installation instructions:

clone this repository

cd to the directory and type "npm install"

in BLEProject/android/gradle.properties set the path to your Java 11 jdk
if your android sdk isn't in your environment variables you'll need to create a local properties file in the android folder and specify that location.

Connect Android phone that supports Bluetooth 4.0 and above via USB

Enable Developer mode -> Enable USB Debugging

Open CMD/Terminal, head to the project's folder

type: "npm run android"
