package carlosgaspari.utfpr.edu.gerenciadorexcursao.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.R;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Passageiro;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia.ViagemDatabase;

public class PassageirosActivity extends AppCompatActivity {

    private static final String ARQUIVO = "carlosgaspari.utfpr.edu.gerenciadorexcursao.TEMA";
    private static final String OPCAO = "TEMA DARK";

    boolean tema;

    EditText editTextNome, editTextCPF;
    ArrayList<Passageiro> passageiros;
    long idViagem;
    String localViagem;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passageiros);

        intent = getIntent();
        editTextNome = findViewById(R.id.editTextPassageiroNome);
        editTextCPF = findViewById(R.id.editTextPassageiroCPF);

        recuperaPassageiros();

        lerPreferencia();
    }

    private void recuperaPassageiros() {
        localViagem = intent.getStringExtra("localViagem");
        idViagem = intent.getLongExtra("idViagem", -1);
        passageiros = intent.getParcelableArrayListExtra("passageiros");
        if(passageiros == null){
            passageiros = new ArrayList<>();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int add = item.getItemId();

        if(add == R.id.menuItemSalvar){
            Passageiro passageiro = new Passageiro();
            passageiro.setNome(editTextNome.getText().toString());
            passageiro.setCpf(editTextCPF.getText().toString());
            if(idViagem == -1) {
                passageiros.add(passageiro);
                Intent intent = new Intent(getApplicationContext(), ViagensActivity.class);
                intent.putParcelableArrayListExtra("passageiros", passageiros);
                intent.putExtra("localViagem", localViagem);
                startActivity(intent);
            }else{
                passageiro.setIdViagem(idViagem);
                ViagemDatabase.getDatabase(getApplicationContext()).daoPassageiro().insert(passageiro);
                Intent intent = new Intent(getApplicationContext(), EditaViagemActivity.class);
                intent.putExtra("idViagem", idViagem);
                startActivity(intent);
                this.finish();
            }
        }
        if(add == R.id.menuItemCancelar){
            this.finish();
        }
        return true;
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
