package io.forus.me

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.kenai.jffi.Main
import io.forus.me.entities.Account
import io.forus.me.helpers.QrHelper
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.AccountService
import io.forus.me.services.DatabaseService
import io.forus.me.services.Web3Service
import kotlinx.android.synthetic.main.activity_assign_delegates.*
import org.json.JSONObject
import java.util.concurrent.Callable

class LoginActivity : AppCompatActivity() {

    fun createAccount(view: View) {
        val address = Web3Service.newAccount()
        this.setContentView(R.layout.loading)
        val account = ThreadHelper.await<Account>(Callable {
            val account = AccountService.newAccount(address, "TODO")
            AccountService.setCurrentAccount(this, account)
            account
        })
        val intent = Intent()
        setResult(MainActivity.ResultCode.OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun startRecover(view: View) {

    }

}
