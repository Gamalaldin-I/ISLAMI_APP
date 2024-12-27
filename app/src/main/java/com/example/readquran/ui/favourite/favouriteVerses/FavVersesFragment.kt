package com.example.readquran.ui.favourite.favouriteVerses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.readquran.databinding.FragmentFavVersesBinding
import com.example.readquran.model.FavVerse
import com.example.readquran.repo.local.db.LocalRepoImp
import com.example.readquran.ui.sura.SuraActivity
import com.example.readquran.util.adapter.FavVersesAdapter
import com.example.readquran.util.adapterListener.FavouriteListener

class FavVersesFragment : Fragment() ,FavouriteListener{
    private lateinit var binding: FragmentFavVersesBinding
    private lateinit var  db:LocalRepoImp
    private lateinit var adapter:FavVersesAdapter
    private lateinit var viewModel: FavVersesViewModel
    private var  faveVersesList :List<FavVerse> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        binding=FragmentFavVersesBinding.inflate(inflater,container,false)
        db=LocalRepoImp(requireContext())
        //init view model
        viewModel=ViewModelProvider(this)[FavVersesViewModel::class.java]
        //setup and updateData
        viewModel.favVerses.observe(this.viewLifecycleOwner){
            faveVersesList=it
            setupAdapter(faveVersesList)
        }
        viewModel.getFavVerses(db)

        return binding.root
    }
    private fun setupAdapter(verses:List<FavVerse>){
        adapter=FavVersesAdapter(verses,this)
        binding.recyclerView.adapter=adapter
    }




    override fun onClick(f: FavVerse, pos: Int) {
        val intent= Intent(requireContext(),SuraActivity::class.java)
        intent.putExtra("position",f.number)
        intent.putExtra("surahNumber",f.suraId)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.deleteFavVerse(adapter.getWillBeRemoved(),db)
    }

}


