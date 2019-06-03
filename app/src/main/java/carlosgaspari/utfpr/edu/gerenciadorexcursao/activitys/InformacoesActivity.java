package carlosgaspari.utfpr.edu.gerenciadorexcursao.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.R;

public class InformacoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getString(R.string.app_Informacoes));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes);
    }
}
