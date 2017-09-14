package br.com.celulasreligiosas.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import br.com.celulasreligiosas.MainActivity;
import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.dao.CelulasDAO;

public class PhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String estadoLigacao = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String numero = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

        if (estadoLigacao.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
            CelulasDAO dao = new CelulasDAO(context);
            if (dao.findPhone(numero)) {
                enviarNotificacao(context);
                Toast.makeText(context, "Célula Religiosa ligando! ", Toast.LENGTH_SHORT).show();
            }
        }
    }

     public static void enviarNotificacao(Context context){

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(context,0,new Intent(context, MainActivity.class),0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pi);
        builder.setTicker("Chegou mensagem!");
        builder.setContentTitle("Células Religiosas");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher_round));

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String[] listaMsg = new String[]{"Já visitou o sistema hoje?","Visite novas células podem ter surgido.","Clique para entrar no sistema."};
        for(String item : listaMsg){
            style.addLine(item);
        }
        builder.setStyle(style);

        Notification n = builder.build();
        n.flags = Notification.FLAG_AUTO_CANCEL;

        nm.notify(R.mipmap.ic_launcher_round,n);
    }
}
