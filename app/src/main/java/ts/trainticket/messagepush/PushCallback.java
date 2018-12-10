package ts.trainticket.messagepush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;


import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import ts.trainticket.MainActivity;
import ts.trainticket.R;


public class PushCallback implements MqttCallback {

    private ContextWrapper context;

    public PushCallback(ContextWrapper context) {

        this.context = context;
    }

    @Override
    public void connectionLost(Throwable cause) {
        //We should reconnect here
    }

    @Override
    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {

        final NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        final Intent intent = new Intent(context, MainActivity.class);
        final PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);
        Notification noti = new Notification.Builder(context)
                .setContentTitle("您有新的消息")
                        .setContentText(new String(message.getPayload()))
                        .setSmallIcon(R.drawable.icon_launcher)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(activity)
                        .build();
        notificationManager.notify(0, noti);
    }

    @Override
    public void deliveryComplete(MqttDeliveryToken token) {
        //We do not need this because we do not publish
    }

}
