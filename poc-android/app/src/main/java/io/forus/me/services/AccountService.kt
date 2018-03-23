package io.forus.me.services

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Database
import android.content.Context
import io.forus.me.entities.Account
import java.io.File
import java.nio.charset.Charset

/**
 * Created by martijn.doornik on 23/02/2018.
 */
class AccountService {
    companion object {
        private val PREFERENCE_NAME: String = "currentUser"
        private val PREFERENCE_KEY: String = "id"

        var currentUser:Account? = null//Account("0x7b2afe6d5e16944084eaa292ecaa9c3b6469b445", "Mijn Overheid")
        val currentAddress:String
                get() {
                    if (currentUser != null) {
                        return currentUser!!.address
                    }
                    return ""
                }
        fun anyExists(): Boolean {
            val count = DatabaseService.database?.accountDao()?.getAccountCount()
             return  (count != null && count > 0)
        }

        fun getAccountById(accountId: Long): Account? {
            return DatabaseService.database?.accountDao()?.getAccountById(accountId)
        }

        fun getAccounts(): LiveData<List<Account>>? {
            return DatabaseService.database?.accountDao()?.getAccounts()
        }

        fun loadCurrentAccount(context: Context): Account? {
            val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val id = preferences.getLong(PREFERENCE_KEY, -1)
            if (id >= 0) {
                val account = AccountService.getAccountById(id)
                this.currentUser = account
            }
            return currentUser
        }

        fun newAccount(address: String, name: String): Account {
            val ret = Account(address, name)
            DatabaseService.inject.insert(ret)
            return ret
        }

        fun setCurrentAccount(context: Context, account:Account) {
            val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val edit = preferences.edit()
            if (account.id == null) {
                val account = DatabaseService.inject.accountDao().create(account)
                edit.putLong(PREFERENCE_KEY, account)
            } else {
                edit.putLong(PREFERENCE_KEY, account.id!!)
            }
            edit.apply()
        }
    }
}