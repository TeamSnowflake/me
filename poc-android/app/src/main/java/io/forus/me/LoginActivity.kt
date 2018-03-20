package io.forus.me

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.widget.Toast
import io.forus.me.services.AccountService
import kotlinx.android.synthetic.main.register_form.*

class LoginActivity : AppCompatActivity() {

    var currentLayout:Int = 0

    fun cancelRegister(view: View) {
        this.setContentView(R.layout.activity_login)
    }

    fun finishRegister(view: View) {
        Toast.makeText(this, "Account aangemaakt", Toast.LENGTH_SHORT).show()
    }

    fun hidePassphraseField() {
        passphrase_field.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        if (currentLayout != R.layout.activity_login) {
            this.setContentView(R.layout.activity_login)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AccountService.anyExists()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        this.currentLayout = layoutResID
    }

    fun showPassphraseField() {
        passphrase_field.text.clear()
        passphrase_field.visibility = View.VISIBLE
    }

    fun startRecover(view: View) {

    }

    fun startRegister(view: View) {
        this.setContentView(R.layout.register_form)
        passphrase_checkbox.setOnClickListener {
            if (passphrase_checkbox.isChecked) {
                hidePassphraseField()
            } else {
                showPassphraseField()
            }
        }
    }

}
