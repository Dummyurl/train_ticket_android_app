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

{: .box-warning}
**Notice:** If your server deployed in the Public cloud, your android does not need to use the same network! Conversely needs!

That's  all !

### Use Steps
  if your successfully build it, Than Congratulation!

- before login, go to account page

   ![account](https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-11-03-15-44-02-296_ts.trainticket.png)
   
- login, you can use username `fdse_microservices@163.com` && password `DefaultPassword`

   ![login](https://github.com/liucloudhub/liucloudhub.github.io/blob/master/img/android/Screenshot_2018-12-10-19-56-56-992_ts.trainticket.png?raw=true)
   
- after login, you will see the below  page

   ![Account](https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-11-03-15-44-30-130_ts.trainticket.png)

- See Contacts
  
  ![See Contacts](https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-26-19-828_ts.trainticket.png)
  
- See orders
  
  ![See orders](https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-30-12-386_ts.trainticket.png)
 
- Buy ticket
  ![Buy ticket](https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-31-09-747_ts.trainticket.png)

- Travel Path

  ![Travel Path](https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-31-52-848_ts.trainticket.png)
  
- Path Info
  
  ![Path Info](https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-15-53-52-877_ts.trainticket.png)

- Passenger Select

  ![Passenger Select](https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-32-19-692_ts.trainticket.png)
  
- Order Detail

  ![Order Detail](https://raw.githubusercontent.com/liucloudhub/liucloudhub.github.io/master/img/android/Screenshot_2018-12-10-19-32-54-828_ts.trainticket.png)
  

- Any Other Question?
