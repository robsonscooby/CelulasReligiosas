package br.com.celulasreligiosas.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import br.com.celulasreligiosas.CadastroActivity;
import br.com.celulasreligiosas.R;
import br.com.celulasreligiosas.entity.Endereco;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by robson.carlos.santos on 25/08/2017.
 */

public class HttpUrlConnectionAsyncTask extends AsyncTask<String, Void, Endereco> {

    private CadastroActivity context;
    private ProgressBar barra;
    private TextView textoEndereco;

    public HttpUrlConnectionAsyncTask(CadastroActivity context) {
        this.context = context;
        barra = (ProgressBar) context.findViewById(R.id.cadastro__load);
        textoEndereco = (TextView) context.findViewById(R.id.cadastro_endereco);
    }

    @Override
    protected Endereco doInBackground(String... url) {
        String retorno = "";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url[0]).build();
        okhttp3.Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            retorno = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parse(retorno);
    }

    @Override
    protected void onPostExecute(Endereco endereco) {
        super.onPostExecute(endereco);
        barra.setVisibility(View.GONE);

        if(null != endereco){
            textoEndereco.setText(endereco.toString());
        }
    }

    @Override
    protected void onPreExecute() {
        barra.setVisibility(View.VISIBLE);
    }

    private static Endereco parse(String body){
        Endereco endereco = null;
        try {
            JSONObject end = new JSONObject(body);
            endereco = new Endereco(
                        end.getString("logradouro"),
                        end.getString("bairro"),
                        end.getString("localidade"),
                        end.getString("uf"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return endereco;

    }

}
