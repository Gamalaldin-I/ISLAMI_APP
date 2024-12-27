package com.example.readquran.ui.Azkar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.readquran.databinding.FragmentAzkarCategoryBinding
import com.example.readquran.model.ZekrCategory
import com.example.readquran.util.adapter.ZekrCategoriesAdapter
import com.example.readquran.util.adapterListener.ZekrCategoryInterface

class AzkarCategoryFragment : Fragment() , ZekrCategoryInterface{
private lateinit var binding: FragmentAzkarCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAzkarCategoryBinding.inflate(inflater, container, false)

        val azkarCategoryList = ArrayList<ZekrCategory>()
        azkarCategoryList.add(ZekrCategory( "الصباح",1,4))
        azkarCategoryList.add(ZekrCategory( "المساء",1,3))
        azkarCategoryList.add(ZekrCategory( "قبل النوم",1,7))
        azkarCategoryList.add(ZekrCategory( "بعد الإستيقاظ",1,2))
        azkarCategoryList.add(ZekrCategory( "أدعية الأنبياء",1,5))
        azkarCategoryList.add(ZekrCategory("أدعية قرآنية",1,6))
        azkarCategoryList.add(ZekrCategory( "بعد الصلاة المفروضة",1,1))
        azkarCategoryList.add(ZekrCategory( "تسابيح",1,8))

        val adapter = ZekrCategoriesAdapter(azkarCategoryList,this)
        binding.recyclerView.adapter = adapter

        return binding.root
    }

    private fun openReadingZekrActivity(zekrId: Int) {
        val intent = Intent(requireContext(), ReadingZekrActivity::class.java)
        intent.putExtra("azkarId", zekrId)
        startActivity(intent)
    }

    override fun onClick(category: ZekrCategory, position: Int) {
        openReadingZekrActivity(category.zekrId)
    }

}