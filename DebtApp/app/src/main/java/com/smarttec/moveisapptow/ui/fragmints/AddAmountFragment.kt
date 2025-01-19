package com.smarttec.moveisapptow.ui.fragmints

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.data.models.Customer
import com.smarttec.moveisapptow.data.models.Debt
import com.smarttec.moveisapptow.helper.DateTimeCore
import com.smarttec.moveisapptow.viewmodels.CustomerViewModel
import com.smarttec.moveisapptow.viewmodels.DebtMVVM
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.SimpleTimeZone
import java.util.TimeZone

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "name"
private const val ARG_PARAM2 = "id"

/**
 * A simple [Fragment] subclass.
 * Use the [AddAmountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAmountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var name: String? = null
    private var id: String? = null


    private lateinit var prog_add: ProgressBar
    private lateinit var root_add: LinearLayout
    private lateinit var text_amount_date: TextView

    private lateinit var layout_btns_save: LinearLayout
    private lateinit var btn_forhim: android.widget.Button

    private lateinit var btn_clear_name: ImageButton

    private lateinit var dialog: Dialog

    private lateinit var name_amount_contenrer: RelativeLayout
    private lateinit var btn_not_forhim: android.widget.Button
    private lateinit var TextInputLayout_name: com.google.android.material.textfield.TextInputLayout
    private lateinit var input_amount_number: com.google.android.material.textfield.TextInputEditText
    private lateinit var input_amount_detels: com.google.android.material.textfield.TextInputEditText
    private lateinit var input_name_dropmenu: com.google.android.material.textfield.MaterialAutoCompleteTextView

    private lateinit var customerViewModel: CustomerViewModel
    private lateinit var debtMVVM: DebtMVVM


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
    var saveFor =""


    var names= ArrayList<String>()
    var ides = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(ARG_PARAM1)
            id = it.getString(ARG_PARAM2)
        }
        customerViewModel = ViewModelProviders.of(this)[CustomerViewModel::class.java]
        debtMVVM = ViewModelProviders.of(this)[DebtMVVM::class.java]

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCategories()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_add_amount, container, false)



        implmnet(view)

        if (name!=null){
            input_name_dropmenu.setText(name)
            input_name_dropmenu.isEnabled = false
            btn_clear_name.visibility= View.VISIBLE
        }




        resultAddAmountObserve()
        resultAddCustmerObserve()


        onClickedListeners()

        return  view
    }

    private fun onClickedListeners() {
        btn_clear_name.setOnClickListener {
            input_name_dropmenu.text.clear()
            input_name_dropmenu.isEnabled = true
            btn_clear_name.visibility = View.GONE

        }
        btn_forhim.setOnClickListener {
            prperTtSaveAmount("له")
        }
        btn_not_forhim.setOnClickListener {
            prperTtSaveAmount("عليه")
        }

        btn_addCustmr.setOnClickListener {
            saveCusmoer()
        }


        text_amount_date.setOnClickListener {
            DateTimeCore().showDateTimePicker(requireActivity()) { selectedDateTime ->
                text_amount_date.text = selectedDateTime
            }
        }
    }


    private fun prperTtSaveAmount(forHo:String){


        if(!validtionInpust()){
            return
        }

       var selectName = input_name_dropmenu.text.trim().toString()

        if(names.size<0){

        }
        else{

            if (names.contains(selectName)){

                var indexe = names.indexOf(selectName)
                var id = ides[indexe]

                insertCustmor(id.toInt(),forHo)




            }
            else{
                name_input.setText(selectName)
                saveFor= forHo
                dialog.show()
            }



        }



    }


    private fun insertCustmor(customer_id:Int,forHo:String){



        var amount = input_amount_number.text.toString().toDouble()

        var desc = input_amount_detels.text.toString()
        var date = text_amount_date.text.toString()


        var debt =Debt(amount = amount, customer_id = customer_id, d_type = forHo, date = date, description = desc)

        debtMVVM.insertDebt(debt)
    }

    private fun validtionInpust():Boolean{
        if(input_name_dropmenu.text.toString().isEmpty()){
            input_name_dropmenu.setError("يجب ادخال الاسم")
            input_name_dropmenu.requestFocus()
            return false
        }

        if(input_amount_number.text.toString().isEmpty()){
            input_amount_number.setError("يجب ادخال المبلغ")
            input_amount_number.requestFocus()
            return false
        }

        if(input_amount_detels.text.toString().isEmpty()){
            input_amount_detels.setError("يجب ادخال التفاصيل")
            input_amount_detels.requestFocus()
            return false
        }

        return true
    }


    private  fun  saveCusmoer (){

        var name = ""
        var phone:String = ""
        var lemet:Int? = null
        var type = "عملاء"
        if( name_input.text.toString().isEmpty()){
            name_input.setError("حقل الاسم مطلوب")
            return
        }


        name =name_input.text.toString()

        if(phone_input.text.toString().isNotEmpty()){
            phone = phone_input.text.toString()
        }

        if(lemet_input.text.toString().isNotEmpty()){
            lemet = lemet_input.text.toString().toInt()
        }



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





    }



    private fun resultAddAmountObserve(){
        debtMVVM.insertResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { id ->
                Snackbar.make(input_name_dropmenu,"تم اضافة المبلغ بنجاح",Snackbar.LENGTH_LONG).show()

                input_amount_number.text!!.clear()
                input_amount_detels.text!!.clear()
            }.onFailure { exception ->

                Snackbar.make(input_name_dropmenu,"فشل في الإضافة: ${exception.message}",Snackbar.LENGTH_LONG).show()

            }
        }
    }

    private fun resultAddCustmerObserve(){
        customerViewModel.insertResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { id ->
                Toast.makeText(requireActivity(), "تمت اضافة الاسم بنجاح برقم ID: $id", Toast.LENGTH_SHORT).show()
                name_input.text!!.clear()

                phone_input.text!!.clear()
                lemet_input.text!!.clear()
                dialog.cancel()

                insertCustmor(id.toInt(),saveFor)
            }.onFailure { exception ->
                Toast.makeText(requireActivity(), "فشل في الإضافة: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun observeCategories() {
        customerViewModel.observeAllCousmers().observe(viewLifecycleOwner,object : Observer<List<Customer>> {
            override fun onChanged(value: List<Customer>) {

                value.forEach {
                    names.add(it.name)
                    ides.add(it.id.toString())
                }

                input_name_dropmenu.setAdapter(ArrayAdapter(requireActivity(),R.layout.siper2,names))

            }

        })
    }




    private fun implmnet(view: View) {
        root_add = view.findViewById(R.id.root_add)
        prog_add = view.findViewById(R.id.prog_add)
        btn_forhim = view.findViewById(R.id.btn_forhim)

        btn_clear_name = view.findViewById(R.id.btn_clear_name)
        btn_not_forhim = view.findViewById(R.id.btn_not_forhim)
        text_amount_date = view.findViewById(R.id.text_amount_date)

        layout_btns_save = view.findViewById(R.id.layout_btns_save)


        input_name_dropmenu = view.findViewById(R.id.input_name_dropmenu)
        input_amount_number = view.findViewById(R.id.input_amount_number)
        input_amount_detels = view.findViewById(R.id.input_amount_detels)
        TextInputLayout_name = view.findViewById(R.id.TextInputLayout_name)
        name_amount_contenrer = view.findViewById(R.id.name_amount_contenrer)


      var   clender =  Calendar.getInstance()
        var formater = SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.ENGLISH)

        formater.timeZone = TimeZone.getDefault()

        var timee = formater.format(clender.time)


        text_amount_date.text = timee



        dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.add_custmor_form)


        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.CENTER)



        prog_add = dialog.findViewById(R.id.prog_add)
        root_home =dialog.findViewById(R.id.root_home)
        name_input = dialog.findViewById(R.id.name_input)
        chip_gnral = dialog.findViewById(R.id.chip_gnral)
        phone_input = dialog.findViewById(R.id.phone_input)
        chip_custmr = dialog.findViewById(R.id.chip_custmr)
        lemet_input = dialog.findViewById(R.id.lemet_input)
        chip_splayer = dialog.findViewById(R.id.chip_splayer)
        chipGroupType =dialog.findViewById(R.id.chipGroupType)
        btn_addCustmr = dialog.findViewById(R.id.btn_addCustmr)
        btn_close_form = dialog.findViewById(R.id.btn_close_form)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddAmountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(name: String, id: String) =
            AddAmountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, name)
                    putString(ARG_PARAM2, id)
                }
            }
    }
}