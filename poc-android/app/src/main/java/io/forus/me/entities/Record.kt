package io.forus.me.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import io.forus.me.entities.base.EthereumItem
import io.forus.me.services.AccountService
import io.forus.me.services.RecordService

/**
 * Created by martijn.doornik on 27/02/2018.
 */
@Entity
class Record(
        address: String = "0x0",
        name: String = "",
        account:String = AccountService.currentAddress,
        var recordCategoryId: Int = RecordService.CategoryIdentifier.OTHER,
        @Ignore var value: String = "") : EthereumItem(address, name, account)