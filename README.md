# SoilMonitoring-App

This Application is designed for EE3070 IoT Group Project. Software part is fully contributed by YEUNG Tung Yan.

Functions:
- Remote control sensors ON/OFF
- Remote control watering system ON/OFF
- Recognize the device (proof of scale up IoT solution)
- Latest soil health status
- Latest weather report
- History records

Architecture of the project:
<img width="1255" alt="Screenshot 2021-04-17 at 7 37 01 PM" src="https://user-images.githubusercontent.com/56186850/115111779-850bba00-9fb4-11eb-9d2f-1513022f9fcf.png">

As the above picture shown, Android Studio is for app development. MQTT for remote control. And a bunch of AWS services for fetching data from a NoSQL database DynamoDB(This part is included in another repository: SoilMonitoring-API). As we are applying this device to agricultural use ideally, there will be high-throughput workloads that cannot be served with disk-based data stores, so NoSQL database is chosen to be used.


=================================================================

Personal Project Demonstration: https://youtu.be/06MW89oNJlY 

Personal Project Presentation: https://youtu.be/UO5W-vqJygI

Group Project presentation: https://youtu.be/QgsZyKoRd2s?si=70WXMgmkwinW7pxU
