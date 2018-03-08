package io.forus.me

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import io.forus.me.services.DatabaseService

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var dataThread: DatabaseService.DataThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val currentAccount = this.getCurrentAccountFromSettings()
        if (currentAccount != null) {
            AccountService.currentUser = currentAccount
        }*/

        this.dataThread = DatabaseService.DataThread("DATA_MAIN")
        this.dataThread.start()

        this.dataThread.postTask(Runnable { DatabaseService.inject(this) })

        // Hold the main process until database is loaded.
        while (!DatabaseService.ready) {}

        /*
        this.mPager = findViewById(R.id.main_pager)
        this.mPager.setPageTransformer(false, MainTransformer())
        this.mPager.addOnPageChangeListener(this)*/

        /*
        val fragments = ArrayList<Fragment>()
        fragments.add(0, WalletFragment())
        fragments.add(1, MeFragment())
        fragments.add(2, RecordsFragment())
        this.mPagerAdapter = MainPagerAdapter(supportFragmentManager, fragments)
        this.mPager.adapter = this.mPagerAdapter
        this.showMe()*/

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_wallet -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_qr -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_records -> {
                return@OnNavigationItemSelectedListener true

            }
        }
        false
    }

}
