package io.forus.me.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.forus.me.entities.Token

/**
 * Created by martijn.doornik on 05/03/2018.
 */

@Dao
interface TokenDao {

    @Query("SELECT * FROM `token` WHERE `account` = :arg0")
    fun getTokens(account: String): LiveData<List<Token>>

    @Query("SELECT * FROM `token` WHERE `address` = :arg0 AND `account` = :arg1")
    fun getTokenByAddressByAccount(address:String, account: String): Token

    @Insert
    fun insert(token: Token)

    @Delete
    fun delete(token: Token)
}