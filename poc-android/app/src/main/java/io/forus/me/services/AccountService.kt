package io.forus.me.services

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Database
import io.forus.me.entities.Account

/**
 * Created by martijn.doornik on 23/02/2018.
 */
class AccountService {
    companion object {
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

        fun getAccountById(accountId: Int): Account? {
            return DatabaseService.database?.accountDao()?.getAccountById(accountId)
        }

        fun getAccounts(): LiveData<List<Account>>? {
            return DatabaseService.database?.accountDao()?.getAccounts()
        }

        fun newAccount(address: String, name: String) {
            DatabaseService.inject.insert(Account(address, name))
        }
    }
}