package carlosgaspari.utfpr.edu.gerenciadorexcursao.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import carlosgaspari.utfpr.edu.gerenciadorexcursao.modelos.Passageiro;

@Dao
public interface DaoPassageiro {
    @Insert
    long insert(Passageiro passageiro);

    @Delete
    void delete(Passageiro passageiro);

    @Update
    void update(Passageiro passageiro);

    @Query("SELECT * FROM passageiro WHERE id = :id")
    Passageiro getById(long id);

    @Query("SELECT * FROM passageiro ORDER BY nome ASC")
    List<Passageiro> getAll();

    @Query("SELECT * FROM passageiro WHERE id = :id")
    List<Passageiro> getByViagem(long id);
}
