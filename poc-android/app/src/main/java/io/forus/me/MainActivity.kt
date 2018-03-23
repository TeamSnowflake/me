package io.forus.me

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.forus.me.entities.Record
import io.forus.me.entities.Token
import io.forus.me.entities.base.EthereumItem
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.AccountService
import io.forus.me.services.DatabaseService
import io.forus.me.services.TokenService
import io.forus.me.services.Web3Service
import io.forus.me.views.main.MainPagerAdapter
import io.forus.me.views.me.MeFragment
import io.forus.me.views.record.RecordsFragment
import io.forus.me.views.wallet.WalletFragment

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_add_qr.*
import java.util.concurrent.Callable


class MainActivity : AppCompatActivity(), MeFragment.QrListener {

    private lateinit var dataThread: DatabaseService.DataThread
    private lateinit var mainPager: ViewPager
    private lateinit var meFragment: MeFragment

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.RequestCode.LOGIN) {
            initMainView()
        }
    }

    private fun initMainView() {
        setContentView(R.layout.activity_main)
        this.mainPager = findViewById(R.id.main_pager)
        //this.mainPager.setPageTransformer(false, MainTransformer())
        //this.mainPager.addOnPageChangeListener(this)

        val fragments = ArrayList<Fragment>()
        fragments.add(MainPager.WALLET_VIEW, WalletFragment())
        meFragment = MeFragment().with(this)
        fragments.add(MainPager.ME_VIEW, meFragment)
        fragments.add(MainPager.RECORDS_VIEW, RecordsFragment())
        val adapter = MainPagerAdapter(supportFragmentManager, fragments)
        this.mainPager.adapter = adapter

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)
        Web3Service.initialize(this)
        // Show welcome screen for 1 second
        Handler().postDelayed({
            // Require login to continue
            if (requireLogin()) {
                // If logged in, show main view; otherwise, this is done in onActivityResult
                initMainView()
            }
        }, 2200)
    }

    override fun onQrError(code: Int) {
        when (code) {
            MeFragment.QrListener.ErrorCode.INVALID_OBJECT -> {
                Snackbar.make(this.mainPager, "QR Code is ongeldig.", Snackbar.LENGTH_LONG).show()
            }
        }
        // Make sure the error doesn't happen over and over again
        Thread.sleep(100)
        meFragment.resumeScanner()
    }

    override fun onQrResult(result: EthereumItem) {
        // TODO Search for duplicates
        val alert = Dialog(this)//LayoutDialog().with(R.layout.alert_add_qr)
        alert.setContentView(R.layout.alert_add_qr)
        alert.alert_add_qr_address.setAddress(result.address)
        alert.alert_add_qr_text.text = String.format(alert.alert_add_qr_text.text.toString(), result.name)
        alert.alert_add_qr_add_button.setOnClickListener {
            if (result is Record) {
                this.mainPager.currentItem = MainPager.RECORDS_VIEW
            } else {
                if (result is Token) {
                    TokenService.addToken(result)
                }
                this.mainPager.currentItem = MainPager.WALLET_VIEW
            }
            alert.dismiss()
            meFragment.resumeScanner()
        }
        alert.alert_add_qr_cancel_button.setOnClickListener {
            alert.dismiss()
            meFragment.resumeScanner()
        }
        alert.show()
    }

    private fun requireLogin(): Boolean {
        DatabaseService.prepare(this)
        val account = ThreadHelper.await(Callable {
            AccountService.loadCurrentAccount(this)
        })
        if (account == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, MainActivity.RequestCode.LOGIN)
            return false
        }
        return true
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_wallet -> {
                this.mainPager.currentItem = MainPager.WALLET_VIEW
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr -> {
                this.mainPager.currentItem = MainPager.ME_VIEW
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_records -> {
                this.mainPager.currentItem = MainPager.RECORDS_VIEW
                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }

    private class MainPager {
        companion object {
            internal val WALLET_VIEW: Int = 0
            internal val ME_VIEW: Int = 1
            internal val RECORDS_VIEW: Int = 2
        }
    }

    private class RequestCode {
        companion object {
            val LOGIN: Int = 42
        }
    }

    class ResultCode {
        companion object {
            val OK: Int = 1
        }
    }
}
