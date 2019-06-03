package carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Passageiro;
import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Viagem;

@Database(entities = {Passageiro.class, Viagem.class}, version = 1, exportSchema = false)
public abstract class ViagemDatabase extends RoomDatabase {

    public abstract DaoPassageiro daoPassageiro();
    public abstract DaoViagem daoViagem();

    private static ViagemDatabase instance;

    public static ViagemDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (ViagemDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            ViagemDatabase.class,
                            "viagem.db").allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }

}
