package com.smarttec.moveisapptow.ui.fragmints

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.adapters.CusRVAdapter
import com.smarttec.moveisapptow.adapters.MoviesViewPagerAdapter
import com.smarttec.moveisapptow.data.models.Customer
import com.smarttec.moveisapptow.data.models.CustomerDebtsSummary
import com.smarttec.moveisapptow.data.models.MovesModel
import com.smarttec.moveisapptow.helper.FragmentCallBack
import com.smarttec.moveisapptow.viewmodels.AllMovesViewModel
import com.smarttec.moveisapptow.viewmodels.CustomerViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USERSTYPE = "c_type"


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() ,FragmentCallBack {
    // TODO: Rename and change types of parameters
    private var c_type: String? = null



    private lateinit var input_search_dropmenu: MaterialAutoCompleteTextView
    private lateinit var root_home: LinearLayout
    var isSerch = false
    private lateinit var layoutSerch: LinearLayout
    private lateinit var icon_close_seach: ImageButton
    private lateinit var custmer_summery_list: androidx.recyclerview.widget.RecyclerView

    private lateinit var myAdapter:CusRVAdapter
    private lateinit var customerViewModel: CustomerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            c_type = it.getString(USERSTYPE)
        }
        myAdapter = CusRVAdapter()
        customerViewModel = ViewModelProviders.of(this)[CustomerViewModel::class.java]
        Log.d(ContentValues.TAG, "type  : ----------: ${c_type}--------------------")

        customerViewModel.getCutmerDebtSummry(c_type!!)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_home, container, false)

        imelmnt(view)




        return view
    }
    override fun onShowEditText(flag: String,query:String) {
        if(flag == "ser"){

            customerViewModel.observeSummry().value?.let { originalList ->
                // تصفية البيانات
                val filteredList = originalList.filter { client ->

                            client.customer_name.contains(query, ignoreCase = true)
                }
                // تحديث الجدول
                showData1(filteredList)
            }


        }
        else if(flag == "close"){
            customerViewModel.observeSummry().value?.let { originalList ->
                showData1(originalList)

            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCategories()
    }


    private fun prepareRecyclerView(myaadapter: CusRVAdapter) {
        custmer_summery_list.adapter = myaadapter
        custmer_summery_list.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
    }

    private fun observeCategories() {
        customerViewModel.observeSummry().observe(viewLifecycleOwner,object : Observer<List<CustomerDebtsSummary>>{
            override fun onChanged(value: List<CustomerDebtsSummary>) {
                showData1(value)

            }

        })
    }




    private fun showData1(allData:List<CustomerDebtsSummary>){
        var dd = CusRVAdapter()
        dd.setMovesList(allData!!, allData.size)
        prepareRecyclerView(dd)
    }

    private fun imelmnt(view: View) {

        custmer_summery_list = view.findViewById(R.id.custmer_summery_list)
        root_home = view.findViewById(R.id.root_home)

        layoutSerch = view.findViewById(R.id.layoutSerch)
        input_search_dropmenu = view.findViewById(R.id.input_search_dropmenu)
        icon_close_seach = view.findViewById(R.id.icon_close_seach)




    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(c_type: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(USERSTYPE, c_type)

                }
            }
    }




}
//
//fun  saveCusmoer (){
//
//    var name = name_input.text.toString()
//    var phone = phone_input.text.toString()
//    var type = "عملاء"
//    var lemet = lemet_input.text.toString().toInt()
//    var selectedID = chipGroupType.checkedChipId
//
//    when(selectedID){
//        chip_custmr.id ->{
//            type = "عملاء"
//        }
//        chip_splayer.id ->{
//            type = "موردين"
//        }
//        chip_gnral.id->{
//            type = "عام"
//        }
//    }
//    var customer = Customer(name = name, phone = phone, type = type, permissible_limit = lemet)
//
//    customerViewModel.insertMeal(customer)
//
//
//
//
//
//
//}

//public class MyFragment extends Fragment implements FragmentCallback {
//    private EditText editText;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_layout, container, false);
//        editText = view.findViewById(R.id.my_edit_text);
//        return view;
//    }
//
//    @Override
//    public void onShowEditText() {
//        if (editText != null) {
//            editText.setVisibility(View.VISIBLE);
//        }
//    }
//}
