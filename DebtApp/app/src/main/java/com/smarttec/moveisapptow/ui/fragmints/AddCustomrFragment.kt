package com.smarttec.moveisapptow.ui.fragmints

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.data.models.Customer
import com.smarttec.moveisapptow.viewmodels.CustomerViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"


class AddCustomrFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
private val PICK_CONTACT_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 1001

    private lateinit var prog_add: ProgressBar
    private lateinit var root_home: LinearLayout
    private lateinit var btn_addCustmr: android.widget.Button
    private lateinit var chip_gnral: com.google.android.material.chip.Chip
    private lateinit var chip_custmr: com.google.android.material.chip.Chip
    private lateinit var chip_splayer: com.google.android.material.chip.Chip
    private lateinit var chipGroupType: com.google.android.material.chip.ChipGroup
    private lateinit var name_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var phone_input: com.google.android.material.textfield.TextInputEditText
    private lateinit var lemet_input: com.google.android.material.textfield.TextInputEditText

    private lateinit var btnAddConn: ImageButton
    private lateinit var customerViewModel: CustomerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
        customerViewModel = ViewModelProviders.of(this)[CustomerViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_customr, container, false)


        imelmnt(view)

        resultObserve()
        btn_addCustmr.setOnClickListener {
            saveCusmoer()
        }


        btnAddConn.setOnClickListener {
            // التحقق من وجود إذن الوصول إلى جهات الاتصال
            val permission = Manifest.permission.READ_CONTACTS
            if (ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED) {
                // إذا كان لديك الإذن، افتح شاشة جهات الاتصال
                openContactPicker()
            } else {
                // إذا لم يكن لديك الإذن، اطلبه
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), PERMISSION_REQUEST_CODE)
            }
        }

        return view
    }

    // التعامل مع نتيجة اختيار جهة الاتصال
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            val contactUri: Uri? = data?.data
            val cursor: Cursor? = requireActivity().contentResolver.query(contactUri!!, null, null, null, null)

            cursor?.let {
                if (it.moveToFirst()) {
                    val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val phoneNumber = it.getString(phoneIndex)
                    phone_input.setText(phoneNumber.toString())
                    // هنا يمكنك التعامل مع رقم الهاتف الذي تم اختياره
                    // على سبيل المثال، إرجاعه للتطبيق أو استخدامه في مكان آخر
                    Toast.makeText(requireActivity(), "Selected Contact Number: $phoneNumber", Toast.LENGTH_SHORT).show()
                }
                it.close()
            }
        }
    }

    // التعامل مع نتيجة طلب الإذن
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // الإذن تم منحه، افتح شاشة جهات الاتصال
                openContactPicker()
            } else {
                // الإذن تم رفضه، يمكنك إظهار رسالة للمستخدم أو التعامل مع الحالة
                Toast.makeText(requireActivity(), "Permission denied to read contacts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // فتح شاشة جهات الاتصال
    private fun openContactPicker() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent, PICK_CONTACT_REQUEST)
    }


    fun saveCusmoer() {

        var name = ""
        var phone: String = ""
        var lemet: Int? = null
        var type = "عملاء"
        if (name_input.text.toString().isEmpty()) {
            name_input.setError("حقل الاسم مطلوب")
            return
        }


        name = name_input.text.toString()

        if (phone_input.text.toString().isNotEmpty()) {
            phone = phone_input.text.toString()
        }

        if (lemet_input.text.toString().isNotEmpty()) {
            lemet = lemet_input.text.toString().toInt()
        }


        var selectedID = chipGroupType.checkedChipId

        when (selectedID) {
            chip_custmr.id -> {
                type = "عملاء"
            }

            chip_splayer.id -> {
                type = "موردين"
            }

            chip_gnral.id -> {
                type = "عام"
            }
        }
        var customer = Customer(name = name, phone = phone, type = type, permissible_limit = lemet)

        customerViewModel.insertCustomer(customer)


    }

    private fun resultObserve() {
        customerViewModel.insertResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { id ->
                Toast.makeText(
                    requireActivity(),
                    "تمت الإضافة بنجاح برقم ID: $id",
                    Toast.LENGTH_SHORT
                ).show()
                name_input.text!!.clear()

                phone_input.text!!.clear()
                lemet_input.text!!.clear()
            }.onFailure { exception ->
                Toast.makeText(
                    requireActivity(),
                    "فشل في الإضافة: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun imelmnt(view: View) {

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
        btnAddConn = view.findViewById(R.id.btnAddConn)


    }

}
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.database.Cursor
//import android.net.Uri
//import android.provider.ContactsContract
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//class MainActivity : AppCompatActivity() {
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // افترض أن لديك زر
//        val button = findViewById<Button>(R.id.button_select_contact)
//        button.setOnClickListener {
//
//        }
//    }
//
//
//
//
//}
