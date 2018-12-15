# train_ticket_android_app
microservice benchmark android app


### This Android application is based on ticket seller application

### Supported Android Version
- v 6.0+

### Local runtime Android SDK environment
- compileSdkVersion: 28
- targetSdkVersion: 28
- gradle:3.2.0

### Development Tool
- Android Studio v3.2

### SetUp setps
- (1) You should setup [ticket seller application](https://github.com/FudanSELab/train-ticket) , 
  then follow the deployment  with k8s and isito instructions, and expose server port 31380.
  
- (2) Clone all the code to your local computer with git.
- (3) Connect your android phone with your computer with android usb-cable, `please make sure it is connected`. 
- (4) Change Ip to your server master ip in UrlProperties class, `clientIstioIp = "http://master_ip:31380"`
- (5) Than build with Android Studio
That's  all !


### Notification

**Note:** If your server deployed in the Public cloud, your android does not need to use the same network! Conversely needs!


### Use Steps
  if your successfully build it, Than Congratulation!

- Before login, go to account page
   
    <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-56-51-353_ts.trainticket.png" width="350" hegiht="620" align=center />
   
- Login, you can use username `fdse_microservices@163.com` && password `DefaultPassword`

    <img src="https://github.com/liucloudhub/liucloudhub.github.io/blob/master/img/android/Screenshot_2018-12-10-19-56-56-992_ts.trainticket.png" width="350" hegiht="620" align=center />
   
   
- After login, you will see the below  page

    <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-11-03-15-44-30-130_ts.trainticket.png" width="350" hegiht="620" align=center />
   

- Contacts List
  
    <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-26-19-828_ts.trainticket.png" width="350" hegiht="620" align=center />
     
  
- orders  List
  
    <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-30-12-386_ts.trainticket.png" width="350" hegiht="620" align=center />

    <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/0B5A0F77E272A920483C73BA93F102B9.jpg" width="350" hegiht="620" align=center />
 
- Buy ticket

    <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-31-09-747_ts.trainticket.png" width="350" hegiht="620" align=center />

- Travel Path

    <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-31-52-848_ts.trainticket.png" width="350" hegiht="620" align=center />
  
- Path Info
  
     <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-15-53-52-877_ts.trainticket.png" width="350" hegiht="620" align=center />

- Passenger Select

     <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-32-19-692_ts.trainticket.png" width="350" hegiht="620" align=center />

  
- Order Detail

     <img src="https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-32-54-828_ts.trainticket.png" width="350" hegiht="620" align=center />


- Any Other Question?
