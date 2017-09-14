package br.com.celulasreligiosas.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.dao.CelulasDAO;

import static br.com.celulasreligiosas.receiver.PhoneReceiver.enviarNotificacao;

public class SMSReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[]) pdus[0];
        String format = (String) intent.getSerializableExtra("format");
        SmsMessage sms = SmsMessage.createFromPdu(pdu,format);

        String telefone = sms.getDisplayOriginatingAddress();
        CelulasDAO dao = new CelulasDAO(context);
        if(dao.findPhone(telefone)) {
            enviarNotificacao(context);
            MediaPlayer media = MediaPlayer.create(context,R.raw.msg);
            media.start();
            Toast.makeText(context, "Célula Mensagem!", Toast.LENGTH_SHORT).show();
        }
    }
}
