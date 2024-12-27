package com.example.readquran.util.adapter.viewPager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.readquran.ui.favourite.downloads.DownloadsFragment
import com.example.readquran.ui.favourite.favouriteTracks.FavTracksFragment
import com.example.readquran.ui.favourite.favouriteVerses.FavVersesFragment

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = arrayListOf(
        FavVersesFragment(),
        FavTracksFragment(),
        DownloadsFragment()
    )

    private val fragmentTitles = arrayListOf(
        "الآيات",
        "الصوتيات",
        "التنزيلات"
    )

    fun clear(){
        fragments.clear()
        fragmentTitles.clear()
    }

    fun getFragmentTitle(position: Int): String = fragmentTitles[position]

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}

