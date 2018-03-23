package io.forus.me.views.wallet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.R
import android.arch.lifecycle.Observer
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.forus.me.entities.Token
import io.forus.me.services.AccountService
import io.forus.me.services.TokenService


/**
 * Created by martijn.doornik on 15/03/2018.
 */
class WalletFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wallet_fragment, container, false)
        val pager:ViewPager = view.findViewById(R.id.wallet_pager)
        pager.adapter = WalletPagerAdapter(childFragmentManager, listOf(
            TokenFragment()
        ))
        return view
    }

    class WalletPagerAdapter(fragmentManager: FragmentManager, private val fragments: List<Fragment>): FragmentPagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }
}