package carlosgaspari.utfpr.edu.gerenciadorexcursao.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.R;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Viagem;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia.ViagemDatabase;

public class PrincipalActivity extends AppCompatActivity {

    ListView listViewViagens;
    List<Viagem> viagens;

    private ActionMode actionMode;
    private int positionSelected = -1;
    private View viewSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        listViewViagens = findViewById(R.id.listViewViagens);

        listViewViagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionSelected = position;
                editaSelecionado();
            }
        });

        listViewViagens.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewViagens.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(actionMode != null){
                    return false;
                }
                positionSelected = position;
                view.setBackgroundColor(Color.LTGRAY);
                viewSelected = view;
                listViewViagens.setEnabled(true);
                actionMode = startSupportActionMode(mActionModeCallback);

                return true;
            }
        });

        populaLista();
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

            listViewViagens.setEnabled(true);
        }
    };

    private void deletaSelecionado() {
        AlertDialog.Builder confirma = new AlertDialog.Builder(this);
        confirma.setMessage(getString(R.string.confirmar));
        confirma.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Viagem s = ViagemDatabase.getDatabase(getApplicationContext()).daoViagem().getById(viagens.get(positionSelected).getId());
                ViagemDatabase.getDatabase(getApplicationContext()).daoViagem().delete(s);
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
        Intent intent = new Intent(this, EditaViagemActivity.class);
        intent.putExtra("idViagem", viagens.get(positionSelected).getId());
        startActivity(intent);
    }

    private void populaLista() {
        viagens = ViagemDatabase.getDatabase(getApplicationContext()).daoViagem().getAll();

        ArrayAdapter<Viagem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, viagens);
        listViewViagens.setAdapter(adapter);
        registerForContextMenu(listViewViagens);
    }


    public void informacoes(View view){
        Intent intent = new Intent (this, InformacoesActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int add = item.getItemId();

        if(add == R.id.menuItemAdicionar){
            Intent intent = new Intent(this, ViagensActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
