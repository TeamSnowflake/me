package io.forus.me.views.wallet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.R
import io.forus.me.entities.base.WalletItem
import kotlinx.android.synthetic.main.token_listitem_view.view.*

/**
 * Created by martijn.doornik on 22/03/2018.
 */
class WalletListAdapter(var walletItems: List<WalletItem> = listOf()) : RecyclerView.Adapter<WalletListAdapter.WalletViewHolder>() {
    override fun getItemCount(): Int {
        return walletItems.size
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        holder.nameView.text = walletItems[position].name
        holder.qrView.setImageBitmap(walletItems[position].qrCode)
        holder.valueView.text = walletItems[position].amount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.token_listitem_view, parent,false)
        return WalletViewHolder(view)
    }

    class WalletViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val qrView = view.qr_view
        val nameView = view.name_text
        val valueView = view.value_text
    }
}