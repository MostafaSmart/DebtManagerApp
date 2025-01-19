package com.smarttec.moveisapptow.ui.fragmints

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.data.models.Customer
import com.smarttec.moveisapptow.viewmodels.CustomerViewModel
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class DialogAddCusmor : DialogFragment() {

    private lateinit var prog_add: ProgressBar
    private lateinit var root_home: LinearLayout
    private lateinit var btn_close_form: ImageButton
    private lateinit var btn_addCustmr: android.widget.Button
    private lateinit var chip_gnral: com.google.android.material.chip.Chip
    private lateinit var chip_custmr: com.google.android.material.chip.Chip
    private lateinit var chip_splayer: com.google.android.material.chip.Chip
    private lateinit var chipGroupType: com.google.android.material.chip.ChipGroup
    private lateinit var name_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var phone_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var lemet_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var customerViewModel: CustomerViewModel

    companion object {
        private const val CUSTMERNAME = "cos_name"


        // دالة لإنشاء الـ DialogFragment مع البيانات المطلوبة
        fun newInstance(cusName:String): DialogAddCusmor {
            val fragment = DialogAddCusmor()
            val args = Bundle()
            args.putString(CUSTMERNAME, cusName)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var view =  inflater.inflate(R.layout.add_custmor_form, container, false)

        customerViewModel = ViewModelProviders.of(this)[CustomerViewModel::class.java]


        imlmnt(view)


        btn_close_form.visibility = View.VISIBLE

        return view
    }

    private fun imlmnt(view: View) {
        prog_add = view.findViewById(R.id.prog_add)
        root_home = view.findViewById(R.id.root_home)
        name_input = view.findViewById(R.id.name_input)
        chip_gnral = view.findViewById(R.id.chip_gnral)
        phone_input = view.findViewById(R.id.phone_input)
        chip_custmr = view.findViewById(R.id.chip_custmr)
        lemet_input = view.findViewById(R.id.lemet_input)
        chip_splayer = view.findViewById(R.id.chip_splayer)
        chipGroupType = view.findViewById(R.id.chipGroupType)
        btn_addCustmr = view.findViewById(R.id.btn_addCustmr)
        btn_close_form = view.findViewById(R.id.btn_close_form)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // استدعاء البيانات من Bundle
        val namec = arguments?.getString(CUSTMERNAME)



        name_input.setText(namec!!.toString())

        btn_close_form.setOnClickListener {
            dismiss() // إغلاق الـ Dialog
        }

        btn_addCustmr.setOnClickListener {
            saveCusmoer()
        }

    }




    fun  saveCusmoer (){

        var name = name_input.text.toString()
        var phone = phone_input.text.toString()
        var type = "عملاء"
        var lemet = lemet_input.text.toString().toInt()
        var selectedID = chipGroupType.checkedChipId

        when(selectedID){
            chip_custmr.id ->{
                type = "عملاء"
            }
            chip_splayer.id ->{
                type = "موردين"
            }
            chip_gnral.id->{
                type = "عام"
            }
        }
        var customer = Customer(name = name, phone = phone, type = type, permissible_limit = lemet)

        customerViewModel.insertCustomer(customer)


        name_input.text!!.clear()

        phone_input.text!!.clear()
        lemet_input.text!!.clear()


        dismiss()


    }

    override fun onStart() {
        super.onStart()
        // ضبط حجم الـ Dialog ليملأ الشاشة
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
