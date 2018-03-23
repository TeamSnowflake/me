package io.forus.me.entities.base

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.graphics.Bitmap
import com.google.zxing.EncodeHintType
import io.forus.me.helpers.JsonHelper
import io.forus.me.helpers.QrHelper
import net.glxn.qrgen.android.QRCode

/**
 * Created by martijn.doornik on 23/02/2018.
 */
@Entity
abstract class EthereumItem(
        @ColumnInfo(name="address")
        var address: String,
        var name: String,
        var account: String) {

    @PrimaryKey
    var id: Long? = null

    val qrCode: Bitmap
        get() = QrHelper.getQrBitmap(address)

    companion object {
        fun fromString(input: String): EthereumItem? {
            return JsonHelper.toEthereumItem(input)
        }
    }
}