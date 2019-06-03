package carlosgaspari.utfpr.edu.gerenciadorexcursao.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.R;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Passageiro;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia.ViagemDatabase;

public class PassageirosActivity extends AppCompatActivity {

    EditText editTextNome, editTextCPF;
    ArrayList<Passageiro> passageiros;
    long idViagem;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passageiros);

        intent = getIntent();
        editTextNome = findViewById(R.id.editTextPassageiroNome);
        editTextCPF = findViewById(R.id.editTextPassageiroCPF);
        idViagem = intent.getLongExtra("idViagem", -1);

        recuperaPassageiros();
    }

    private void recuperaPassageiros() {
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
                Intent intent = new Intent(this, ViagensActivity.class);
                intent.putParcelableArrayListExtra("passageiros", passageiros);
                startActivity(intent);
            }else{
                passageiro.setIdViagem(idViagem);
                ViagemDatabase.getDatabase(getApplicationContext()).daoPassageiro().insert(passageiro);
                this.finish();
            }
        }
        if(add == R.id.menuItemCancelar){
            this.finish();
        }
        return true;
    }


}
