package carlosgaspari.utfpr.edu.gerenciadorexcursao.activitys;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.R;

public class InformacoesActivity extends AppCompatActivity {

    private static final String ARQUIVO = "carlosgaspari.utfpr.edu.gerenciadorexcursao.TEMA";
    private static final String OPCAO = "TEMA DARK";

    boolean tema;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getString(R.string.app_Informacoes));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);

        lerPreferencia();
    }

    public void lerPreferencia(){
        SharedPreferences s = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);

        tema = s.getBoolean(OPCAO, false);

        muda();
    }

    private void muda() {
        if(tema){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else if(!tema){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
