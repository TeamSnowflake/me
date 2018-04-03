package io.forus.me.views.record

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.forus.me.R
import io.forus.me.entities.Record
import io.forus.me.entities.RecordCategory
import io.forus.me.services.IdentityService
import io.forus.me.services.RecordService

/**
 * Created by martijn.doornik on 03/04/2018.
 */
class RecordRecyclerAdapter(category: RecordCategory, lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<RecordRecyclerAdapter.RecordViewHolder>() {

    private val recordData: LiveData<List<Record>>? = RecordService.getRecordsByCategoryByIdentity(category.id, IdentityService.currentAddress)
    private var records = emptyList<Record>()

    init {
        recordData!!.observe(lifecycleOwner, Observer {
            if (it != null) {
                records = it
                notifyDataSetChanged()
            }
        })
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.nameView.text = records[position].name
        holder.valueView.text = records[position].value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.record_list_item, parent, false)
        return RecordViewHolder(view)
    }

    class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.nameView)
        val valueView: TextView = view.findViewById(R.id.valueView)
    }
}