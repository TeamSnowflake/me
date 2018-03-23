package io.forus.me.services

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import io.forus.me.dao.AccountDao
import io.forus.me.dao.RecordDao
import io.forus.me.dao.TokenDao
import io.forus.me.entities.Account
import io.forus.me.entities.Record
import io.forus.me.entities.Token

@Database(entities = arrayOf(
        Account::class,
        Record::class,
        Token::class
        ), version = 5)
abstract class DatabaseService: RoomDatabase() {
    private val ACCOUNT_THREAD: String = "DATA_ACCOUNT"
    private val MAIN_THREAD: String = "DATA_MAIN"
    private val RECORD_THREAD: String = "DATA_RECORD"
    private val TOKEN_THREAD: String = "DATA_TOKEN"

    private var threads: HashMap<String, DataThread> = HashMap()

    private val accountThread: DataThread
            get() {
                return getDataThread(ACCOUNT_THREAD)
            }

    val mainThread: DataThread
            get() = getDataThread(MAIN_THREAD)

    private val recordThread: DataThread
            get() {
                return getDataThread(RECORD_THREAD)
            }
    private val tokenThread: DataThread
        get() {
            return getDataThread(TOKEN_THREAD)
        }

    abstract fun accountDao():AccountDao
    abstract fun recordDao(): RecordDao
    abstract fun tokenDao(): TokenDao

    fun delete(account: Account) {
        accountThread.postTask(Runnable { accountDao().delete(account) })
    }

    fun delete(record: Record) {
        recordThread.postTask(Runnable { recordDao().delete(record) })
    }

    fun delete(token: Token) {
        tokenThread.postTask(Runnable { tokenDao().delete(token) })
    }

    private fun getDataThread(key:String): DataThread {
        if (!threads.containsKey(key)) {
            threads[key] = DataThread(key)
            threads[key]!!.start()
        }
        return threads[key]!!
    }

    fun insert(account: Account) {
        accountThread.postTask(Runnable { accountDao().create(account) })
    }

    fun insert(record: Record) {
        recordThread.postTask(Runnable {recordDao().insert(record)})
    }

    fun insert(token: Token) {
        tokenThread.postTask(Runnable { tokenDao().insert(token) })
    }

    companion object {
        var database: DatabaseService? = null
        val ready:Boolean
            get() = database != null

        fun inject(context: Context): DatabaseService {
            if (database == null) {
                synchronized(DatabaseService::class) {
                    database = Room.databaseBuilder(context.applicationContext,
                            DatabaseService::class.java, "me_client_architecture")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return database!!
        }

        val inject: DatabaseService
                get() {
                    if (database == null) {
                        throw RuntimeException("Database not yet initiated")
                    }
                    return database!!
                }

        fun prepare(context: Context): DatabaseService {
            val thread = DataThread("Initializer")
            thread.start()
            thread.postTask(Runnable {
                this.inject(context)
            })
            while (!this.ready) {}
            return this.inject
        }

    }

    class DataThread(name: String): HandlerThread(name) {
        private lateinit var mHandler: Handler
        private var loaded: Boolean = false

        override fun onLooperPrepared() {
            super.onLooperPrepared()
            mHandler = Handler(looper)
            loaded = true
        }

        fun postTask(task: Runnable) {
            while (!loaded) {}
            mHandler.post(task)
        }
    }
}