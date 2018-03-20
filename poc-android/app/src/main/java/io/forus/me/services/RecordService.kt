package io.forus.me.services

import android.arch.lifecycle.LiveData
import io.forus.me.entities.Record
import io.forus.me.entities.RecordCategory
import io.forus.me.services.base.BaseService

/**
 * Created by martijn.doornik on 27/02/2018.
 */
class RecordService : BaseService() {
    class CategoryIdentifier {
        companion object {
            val PERSONAL: Int = 0
            val LICENCES: Int = 1
            val MEDICAL: Int = 2
            val PROFESSIONAL: Int = 3
            val RELATIONS: Int = 4
            val OTHER: Int = 5
            val list: List<Int>
                get() = listOf(PERSONAL, LICENCES, MEDICAL, PROFESSIONAL, RELATIONS, OTHER)
            fun nameOf(id: Int): String {
                // TODO FIX HARDCODING
                when (id) {
                    PERSONAL -> return "Persoonlijk"
                    LICENCES -> return "Licenties"
                    MEDICAL -> return "Medisch"
                    PROFESSIONAL -> return "Professioneel"
                    RELATIONS -> return "Relaties"
                    OTHER -> return "Overig"
                }
                return ""
            }
        }

    }

    companion object {
        val inject: RecordService = RecordService()
        private var _categories:Map<Int, RecordCategory>? = null

        fun getRecordsByCategoryByAccount(category: Int, account: String): LiveData<List<Record>>? {
            return DatabaseService.database?.recordDao()?.getRecordsFromCategoryAndAccount(category, account)
        }

        fun getRecordCategoriesByAccount(account:String): Map<Int, RecordCategory> {
            if (_categories == null) {
                val ret = LinkedHashMap<Int, RecordCategory>()
                CategoryIdentifier.list.forEach { id: Int ->
                        val recordsByCategory = getRecordsByCategoryByAccount(id, account)
                        val category = RecordCategory(id, CategoryIdentifier.nameOf(id), account, recordsByCategory)
                        ret.put(id, category)
                }
                _categories = ret
            }
            return _categories!!
        }

        /**
         * Create a new record to track.
         *
         * @param address the validated address
         * @param name the name of the record
         * @param category The category of the record
         */
        fun newRecord(address: String, name: String, category: RecordCategory) {
            DatabaseService.database?.insert(Record(address, name, AccountService.currentAddress, category.id))
        }
    }
}