package ts.trainticket.messagepush;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

/**
 * Created by liuZOZO on 2018/4/17.
 */
public class MQTTService extends Service {

    public static final String BROKER_URL = "tcp://115.159.107.170:1883";
    public static final String clientId = "android-client";
    public static final String TOPIC = "qqq";
    private MqttClient mqttClient;
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());
            mqttClient.setCallback(new PushCallback(this));
            mqttClient.connect();
            mqttClient.subscribe(TOPIC);
            Toast.makeText(getApplicationContext(), "connect success" , Toast.LENGTH_LONG).show();
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        try {
            mqttClient.disconnect(0);
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}