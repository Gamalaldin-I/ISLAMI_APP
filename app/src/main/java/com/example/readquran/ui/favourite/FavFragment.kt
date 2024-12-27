package com.example.readquran.ui.favourite
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.readquran.databinding.FragmentFavBinding
import com.example.readquran.util.adapter.viewPager.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs

class FavFragment : Fragment() {
    private lateinit var binding: FragmentFavBinding
    private var adapter: ViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavBinding.inflate(inflater, container, false)
        val view = binding.root

         adapter= ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        // Link TabLayout with ViewPager
        binding.viewPager.setPageTransformer { page, position ->
            page.alpha = 1 - abs(position)
            page.scaleX = 0.8f + (1 - abs(position)) * 0.2f
            page.scaleY = 0.8f + (1 - abs(position)) * 0.2f
            page.rotation = -10 * position
        }

        TabLayoutMediator(binding.tableLayout, binding.viewPager) { tab, position ->
            tab.text = adapter!!.getFragmentTitle(position)
        }.attach()

        return view

    }
    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.clear()
        adapter = null
        binding.viewPager.adapter = null
    }


}