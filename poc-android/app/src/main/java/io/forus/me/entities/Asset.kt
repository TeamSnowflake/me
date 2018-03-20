package io.forus.me.entities

import android.arch.persistence.room.Entity
import io.forus.me.entities.base.WalletItem
import io.forus.me.services.AccountService

/**
 * Created by martijn.doornik on 16/02/2018.
 */
//@Entity
class Asset(address: String, name: String, account:String = AccountService.currentAddress) : WalletItem(address, name, account) {
    override val amount: String
        get() {
            return ""
        }

    override val label: String
            get() = this.name
}