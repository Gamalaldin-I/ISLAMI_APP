package com.example.readquran.ui.sura.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.readquran.R
import com.example.readquran.databinding.FragmentShareBinding
import com.example.readquran.model.Ayah
import com.example.readquran.model.Surah
import com.example.readquran.repo.local.db.LocalRepoImp
import com.example.readquran.util.ArabicTranslator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShareFragment : Fragment() {
    private lateinit var binding: FragmentShareBinding
    private lateinit var db:LocalRepoImp
    private lateinit var _ayahs: List<Ayah>
    private var ayaNumber:Int=0
    private var ayasCount:Int=0
    private var suraNumber:Int=0
    private lateinit var suraName:String
    private lateinit var sharedTxt:String
    private  var range=12
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    fun getInstance(ayaNumber:Int,sura:Surah): ShareFragment {
        val bundle=Bundle()
        val ayasCount=sura.numberOfVerses
        val suraNumber=sura.surahNumber
        val suraName=sura.arabicName
        bundle.putString("suraName",suraName)
        bundle.putInt("suraNumber",suraNumber)
        bundle.putInt("ayaNumber",ayaNumber)
        bundle.putInt("ayasCount",ayasCount)
        val shareFragment= ShareFragment()
        shareFragment.arguments=bundle
        return shareFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        db= LocalRepoImp(requireContext())
        binding=FragmentShareBinding.inflate(inflater,container,false)
        val args=this.arguments
         ayaNumber=args!!.getInt("ayaNumber")
         ayasCount=args.getInt("ayasCount")
         suraNumber=args.getInt("suraNumber")
         suraName=args.getString("suraName") !!
        getAyahsBySurahNumber(db,suraNumber)

        // Inflate the layout for this fragment
        binding.FromNoPicker.minValue=1
        binding.FromNoPicker.maxValue=ayasCount
        binding.FromNoPicker.value=ayaNumber

        binding.toNoPicker.minValue=ayaNumber
        binding.toNoPicker.maxValue=getMaxValueOfToAyahPicker(ayaNumber,ayasCount,12)
        binding.toNoPicker.value=ayaNumber
        binding.FromNoPicker.setOnValueChangedListener { numberPicker, _, _ ->
            binding.toNoPicker.minValue=numberPicker.value
            binding.toNoPicker.value=numberPicker.value
            val maxValue= getMaxValueOfToAyahPicker(numberPicker.value,ayasCount,range)
            binding.toNoPicker.maxValue=maxValue
        }

        binding.radioGroup.setOnCheckedChangeListener{
            _,checkedId->
            when(checkedId){
                R.id.textRB->{
                    range=12
                }
                R.id.imageRB->{
                    range=3
                }

            }
            val maxValue= getMaxValueOfToAyahPicker(binding.FromNoPicker.value,ayasCount,range)
            binding.toNoPicker.maxValue=maxValue

        }



        return binding.root
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun getAyahsBySurahNumber(repo: LocalRepoImp, surahNumber: Int){
        GlobalScope.launch(Dispatchers.IO) {
            val ayahs = repo.getAyatBySurahNumber(surahNumber)
            _ayahs = ayahs
        }
    }
     fun getSharedText():String{
        //get the list of the shared verses of the surah verses
        val ayatToShare:MutableList<Ayah> = _ayahs.subList(binding.FromNoPicker.value-1,binding.toNoPicker.value) as MutableList<Ayah>
        var textToShare=""
        for (ayah in ayatToShare) {
            textToShare += "${ayah.text} ﴿${ArabicTranslator.toArabicNumerals(ayah.numberInSurah)}﴾ "
        }
        return textToShare
    }

    private fun getMaxValueOfToAyahPicker(ayaNumber:Int, ayasCount:Int, range:Int):Int{
        //if there space to 12 ayas in the surah
        //set the max of ayas n of the current +12
        //else set the max by ayas count
        val maxValue= if (ayasCount -ayaNumber<=range) ayasCount
        else ayaNumber+range
        return maxValue
    }
    fun getSelectedId():Int{
        return binding.radioGroup.checkedRadioButtonId
    }


}