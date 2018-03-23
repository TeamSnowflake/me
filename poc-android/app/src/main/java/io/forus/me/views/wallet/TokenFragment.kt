package io.forus.me.views.wallet

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.R
import io.forus.me.entities.Token
import io.forus.me.services.AccountService
import io.forus.me.services.TokenService

/**
 * Created by martijn.doornik on 22/03/2018.
 */
class TokenFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.token_fragment, container, false)

        val listView: RecyclerView = view.findViewById(R.id.token_list)
        listView.layoutManager = LinearLayoutManager(this.context)

        listView.adapter = WalletListAdapter()
        TokenService.getTokensByAccount(AccountService.currentAddress)!!.observe(this, Observer {
            tokens: List<Token>? ->
            kotlin.run {
                if (tokens != null) {
                    (listView.adapter as WalletListAdapter).walletItems = tokens
                }
            }
        })

        return view
    }



}