package carlosgaspari.utfpr.edu.gerenciadorexcursao.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.R;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Passageiro;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Viagem;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia.ViagemDatabase;

public class ViagensActivity extends AppCompatActivity {

    private static final String ARQUIVO = "carlosgaspari.utfpr.edu.gerenciadorexcursao.TEMA";
    private static final String OPCAO = "TEMA DARK";

    boolean tema;

    EditText editTextLocalizacao;
    ListView listViewPassageiros;
    ArrayList<Passageiro> passageiros;

    private ActionMode actionMode;
    private int positionSelected = -1;
    private View viewSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_viagem);

        editTextLocalizacao = findViewById(R.id.editTextLocalizacao);
        listViewPassageiros = findViewById(R.id.listViewPassageiros);
        editTextLocalizacao.setText(getIntent().getStringExtra("localViagem"));

        listViewPassageiros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSelected = position;
                editaSelecionado();
            }
        });

        listViewPassageiros.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewPassageiros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(actionMode != null){
                    return false;
                }
                positionSelected = position;
                view.setBackgroundColor(Color.LTGRAY);
                viewSelected = view;
                listViewPassageiros.setEnabled(true);
                actionMode = startSupportActionMode(mActionModeCallback);

                return true;
            }
        });

        populaLista();

        lerPreferencia();
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_contexto, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.menuItemAlterar :
                    editaSelecionado();
                    mode.finish();
                    return true;

                case R.id.menuItemExcluir :
                    deletaSelecionado();
                    mode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(viewSelected != null){
                viewSelected.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode = null;
            viewSelected = null;

            listViewPassageiros.setEnabled(true);
        }
    };

    private void deletaSelecionado() {
        AlertDialog.Builder confirma = new AlertDialog.Builder(getApplicationContext());
        confirma.setMessage(getString(R.string.confirmar));
        confirma.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                passageiros.remove(passageiros.get(positionSelected));
                populaLista();
            }
        });
        confirma.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        confirma.show();
    }

    private void editaSelecionado() {
        Intent intent = new Intent(getApplicationContext(), EditarPassageiroActivity.class);
        intent.putExtra("passageiro", passageiros.get(positionSelected));
        intent.putExtra("posicao", positionSelected);
        intent.putParcelableArrayListExtra("passageiros", passageiros);
        startActivity(intent);
    }

    private void populaLista() {
        passageiros = getIntent().getParcelableArrayListExtra("passageiros");
        if(passageiros == null)
            passageiros = new ArrayList<>();
        ArrayAdapter<Passageiro> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, passageiros);
        listViewPassageiros.setAdapter(adapter);
        registerForContextMenu(listViewPassageiros);
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
            Viagem viagem = new Viagem();
            viagem.setLocalizacao(editTextLocalizacao.getText().toString());
            ViagemDatabase.getDatabase(getApplicationContext()).daoViagem().insert(viagem);
            for(Passageiro passageiro : passageiros){
                passageiro.setIdViagem(ViagemDatabase.getDatabase(getApplicationContext()).daoViagem().getLast().getId());
                ViagemDatabase.getDatabase(getApplicationContext()).daoPassageiro().insert(passageiro);
            }

            Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
            startActivity(intent);
        }
        if(add == R.id.menuItemCancelar){
            this.finish();
        }
        return true;
    }

    public void adicionaPassageiro(View view){
        Intent intent = new Intent(getApplicationContext(), PassageirosActivity.class);
        intent.putParcelableArrayListExtra("passageiros", passageiros);
        intent.putExtra("localViagem", editTextLocalizacao.getText().toString());
        startActivity(intent);
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
