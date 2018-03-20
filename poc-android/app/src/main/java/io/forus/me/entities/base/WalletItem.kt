package io.forus.me.entities.base

import android.arch.persistence.room.Entity

/**
 * Created by martijn.doornik on 16/02/2018.
 */
//@Entity
abstract class WalletItem(address:String, name:String, account:String) : EthereumItem(address, name, account) {
    abstract val amount: String
    open val label:String
            get() = amount + " " + name

}