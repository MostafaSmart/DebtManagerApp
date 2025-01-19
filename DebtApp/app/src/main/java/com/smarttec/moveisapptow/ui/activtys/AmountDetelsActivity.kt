package com.smarttec.moveisapptow.ui.activtys

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.BaseDirection
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.text.pdf.languages.ArabicLigaturizer
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.data.models.CustomerDebtsSummary
import com.smarttec.moveisapptow.data.models.Debt
import com.smarttec.moveisapptow.data.models.DebtWithCustomer
import com.smarttec.moveisapptow.helper.DateTimeCore
import com.smarttec.moveisapptow.ui.fragmints.AddAmountFragment
import com.smarttec.moveisapptow.ui.fragmints.CustomDialogFragment
import com.smarttec.moveisapptow.viewmodels.CustomerViewModel
import com.smarttec.moveisapptow.viewmodels.DebtMVVM
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AmountDetelsActivity : AppCompatActivity() {


    private lateinit var layoutAppBar1:LinearLayout
    private lateinit var btnCloseSerch:ImageButton
    private lateinit var inputSetrch:EditText
    private lateinit var layoutMainSerch:RelativeLayout
    private lateinit var columnFor: TextView
    private lateinit var columnDate: TextView
    private lateinit var toolbar_pdf: ImageView
    private lateinit var columnAmount: TextView
    private lateinit var columnDetels: TextView
    private lateinit var toolbar_tital: TextView
    private lateinit var tableLayout: TableLayout
    private lateinit var toolbar_search: ImageView
    private lateinit var toolbar_back_toggle: ImageView
    private lateinit var main_amount_detels: LinearLayout
    private lateinit var tableRow_first:TableRow

    private lateinit var btnPhoneIcon:ImageButton
    private lateinit var btnAddAmount:ImageButton
    private lateinit var textShowForLLAh:TextView
    private lateinit var textShowForAALIh:TextView



    private lateinit var dialog: Dialog
    private lateinit var textDate: TextView
    private lateinit var textTime: TextView
    private lateinit var textAmount: TextView
    private lateinit var textDetels: TextView
    private lateinit var textHederName: TextView
    private lateinit var btnDone: android.widget.Button
    private lateinit var btnEditDebt: android.widget.Button
    private lateinit var icon_for:ImageView
    var totalForAllah = 0.0
    var totalForLLah = 0.0

    var flagId = -1



    private lateinit var dialogUpdate: Dialog
    private lateinit var textDateTime: TextView
    private lateinit var btnSaveUpdate: android.widget.Button
    private lateinit var chipForLLAh: com.google.android.material.chip.Chip
    private lateinit var chipForAAlih: com.google.android.material.chip.Chip
    private lateinit var chipPirntFor: com.google.android.material.chip.ChipGroup
    private lateinit var input_amount_number2: com.google.android.material.textfield.TextInputEditText
    private lateinit var input_amount_detels2: com.google.android.material.textfield.TextInputEditText



    private lateinit var dialogAddAmount: Dialog



    var timee = " "
    var fforr = " "

    var customer_id = ""
    var customer_name = ""


    private lateinit var debtMVVM: DebtMVVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount_detels)

        customer_id =  intent.getStringExtra("cos_id")!!

        debtMVVM = ViewModelProviders.of(this)[DebtMVVM::class.java]

        debtMVVM.getDebtWithCustmor(customer_id!!.toInt())

        imlmnet()


        observeCategories()


        resultUpdateAmountObserve()

        textDateTime.setOnClickListener {
           DateTimeCore().showDateTimePicker(this) { selectedDateTime ->
                textDateTime.text = selectedDateTime
            }
        }

        toolbar_back_toggle.setOnClickListener {
            finish()
        }
        btnSaveUpdate.setOnClickListener {

            saveUpdateAmount()
        }
        btnEditDebt.setOnClickListener {

            showFormUpdate()

        }
        btnCloseSerch.setOnClickListener {
            layoutMainSerch.visibility = View.GONE

            layoutAppBar1.visibility = View.VISIBLE
        }

        toolbar_search.setOnClickListener {
            layoutAppBar1.visibility = View.GONE
            layoutMainSerch.visibility = View.VISIBLE
            inputSetrch.requestFocus()

        }
        sortFun()
        btnAddAmount.setOnClickListener {
            val fragment = AddAmountFragment.newInstance(customer_name!!,customer_id)

            val dialogFragment = CustomDialogFragment(fragment)
            dialogFragment.show(supportFragmentManager, "CustomDialog")

        }
//
        inputSetrch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                debtMVVM.observeDebtWithCostmr().value?.let { originalList ->
                    // تصفية البيانات
                    val filteredList = originalList.filter { client ->
                        client.date.contains(query, ignoreCase = true) ||
                                client.amount.toString().contains(query, ignoreCase = true) ||
                                client.description.contains(query, ignoreCase = true)
                    }
                    // تحديث الجدول
                    refreshTable(filteredList)
                }
            }
        })

    }



    private fun saveUpdateAmount(){

        if(!validtionInpust()){
            return
        }

        var amount = input_amount_number2.text.toString().toDouble()
        var detels = input_amount_detels2.text.toString()
        var date = textDateTime.text.toString()

        var forr = ""
       var selected = chipPirntFor.checkedChipId

        if(selected == chipForAAlih.id){
            forr = "عليه"
        }
        else{
            forr ="له"
        }

        var debt = Debt(id = flagId, description = detels , amount = amount ,date = date, d_type = forr ,customer_id = customer_id.toInt())
        debtMVVM.updateDebt(debt)

        dialogUpdate.cancel()

    }

    private fun showFormUpdate(){
        input_amount_detels2.setText(textDetels.text.toString())
        input_amount_number2.setText(textAmount.text)
        textDateTime.text =  timee

        if(fforr =="له"){
            chipForLLAh.isChecked =true
        }
        else{
            chipForAAlih.isChecked = true
        }

        dialog.cancel()
        dialogUpdate.show()
    }


    private fun sortFun(){

        columnDate.setOnClickListener {


            debtMVVM.observeDebtWithCostmr().value?.let { originalList ->
                // تصفية البيانات
                val sort = originalList.sortedBy {
                    it.date
                }
                // تحديث الجدول
                refreshTable(sort)
            }

        }

        columnAmount.setOnClickListener {
            debtMVVM.observeDebtWithCostmr().value?.let { originalList ->
                // تصفية البيانات
                val sort = originalList.sortedBy {
                    it.amount
                }
                // تحديث الجدول
                refreshTable(sort)
            }
        }

        columnFor.setOnClickListener {
            debtMVVM.observeDebtWithCostmr().value?.let { originalList ->
                // تصفية البيانات
                val sort = originalList.sortedBy {
                    it.d_type
                }
                // تحديث الجدول
                refreshTable(sort)
            }
        }
    }

    private fun preperToUpdate( client: DebtWithCustomer){

        textAmount.text = client.amount.toString()
        textDetels.text= client.description
        var splites =  client.date.split(" ")
        textDate.text = splites[0].toString()
        textTime.text = splites[1].toString()
        flagId = client.id
        if(client.d_type =="له"){
            icon_for.setImageResource(R.drawable.icon_arrow_for_lah)
        }
        else{
            icon_for.setImageResource(R.drawable.arrow_down_icon)

        }
        fforr = client.d_type
        timee = client.date
        dialog.show()

    }

    private fun observeCategories() {


        debtMVVM.observeDebtWithCostmr().observe(this,object :
            Observer<List<DebtWithCustomer>> {
            override fun onChanged(value: List<DebtWithCustomer>) {
                customer_name = value[0].customer_name
                refreshTable(value!!)

                toolbar_pdf.setOnClickListener {
                    craateReportPDF(value!!)
                }
            }

        })
    }
    private fun resultUpdateAmountObserve(){
        debtMVVM.updateResult.observe(this) { result ->
            result.onSuccess { id ->
                Snackbar.make(main_amount_detels,"تم تعديل بيانات المبلغ بنجاح", Snackbar.LENGTH_LONG).show()
            }.onFailure { exception ->

                Snackbar.make(main_amount_detels,"فشل: ${exception.message}", Snackbar.LENGTH_LONG).show()

            }
        }
    }




    private fun populateTable(clients: List<DebtWithCustomer>) {

        totalForLLah = 0.0
        totalForAllah = 0.0
        tableLayout.children.forEach { chip->
            if (chip.id !=tableRow_first.id){
                tableLayout.removeView(chip)
            }

        }


        if(clients.isNotEmpty()){
            toolbar_tital.text = clients[0].customer_name
            textHederName.text =clients[0].customer_name
        }

        for (client in clients) {
            val tableRow = TableRow(ContextThemeWrapper(this,R.style.tableBack))
            tableRow.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            // إضافة خط أسفل كل صف
//            tableRow.setBackgroundResource(R.drawable.table_row_background)

            val nameTextView = TextView(this)
            nameTextView.text = client.date
            nameTextView.gravity = Gravity.CENTER
            nameTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.5f)

            tableRow.addView(nameTextView)

            val ageTextView =TextView(this)
            ageTextView.text = client.amount.toString()
            ageTextView.gravity = Gravity.CENTER

            ageTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)

//            ageTextView.setBackgroundResource(R.drawable.table_row_background)
            tableRow.addView(ageTextView)

            val emailTextView =TextView(this)
            emailTextView.text = client.description
            emailTextView.gravity = Gravity.CENTER
            emailTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2f)

            tableRow.addView(emailTextView)

            val forHow =TextView(this)
            forHow.text = client.d_type
            forHow.gravity = Gravity.CENTER
            forHow.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f)
            if (client.d_type == "له") {
                forHow.setTextColor(Color.GREEN)
                totalForLLah +=client.amount

            } else {

                forHow.setTextColor(Color.RED)
                totalForAllah +=client.amount

            }

            tableRow.addView(forHow)

            tableRow.setPadding(2,8,2,8)
            // إضافة الصف إلى الجدول
            tableLayout.addView(tableRow)
            tableRow.setOnClickListener {

                preperToUpdate(client)
            }
            // إضافة فاصل بين الصفوف (View)
            val divider = View(this)
            divider.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                1 // سماكة الخط الفاصل
            )
            divider.setBackgroundColor(Color.BLACK)
            tableLayout.addView(divider)
        }

        textShowForAALIh.text = totalForAllah.toString()
        textShowForAALIh.setTextColor(Color.RED)
        textShowForLLAh.text = totalForLLah.toString()
        textShowForLLAh.setTextColor(Color.GREEN)

    }

    private fun refreshTable(clients: List<DebtWithCustomer>){
        tableLayout.removeViews(1,tableLayout.childCount-1)
        populateTable(clients)
    }


    private fun validtionInpust():Boolean{
        if(input_amount_detels2.text.toString().isEmpty()){
            input_amount_detels2.setError("حقل مطلوب")
            input_amount_detels2.requestFocus()
            return false
        }

        if(input_amount_number2.text.toString().isEmpty()){
            input_amount_number2.setError("يجب ادخال المبلغ")
            input_amount_number2.requestFocus()
            return false
        }


        if(chipForAAlih.isChecked == false && chipForLLAh.isChecked==false){
            return false

            Snackbar.make(chipPirntFor,"يجب اختيار نوع المبلغ",Snackbar.LENGTH_SHORT).show()
        }

        return true
    }

    private fun craateReportPDF(allData:List<DebtWithCustomer>){

        Toast.makeText(this,"يرجى الانتظار",Toast.LENGTH_SHORT).show()

        try{
            val fileName = "ecm1.pdf"
            val path = File(cacheDir, fileName).absolutePath
            val pdfWriter = PdfWriter(path)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            document.setMargins(20f, 20f, 20f, 20f);
            document.setBorder(SolidBorder(4f));

            var languageProcessor = ArabicLigaturizer()

            val assetManager = this.assets


            val fontInputStream = assetManager.open("103-Tahoma.ttf")
            val fontFile = File(this.cacheDir, "103-Tahoma.ttf")
            val outputStream = FileOutputStream(fontFile)
            fontInputStream.copyTo(outputStream)
            outputStream.close()
            fontInputStream.close()


            val logoInputStream = assetManager.open("logo12.png")
            val logoFile = File(this.cacheDir, "logo12.png")
            val logoStream = FileOutputStream(logoFile)
            logoInputStream.copyTo(logoStream)
            logoStream.close()
            logoInputStream.close()


            var dataimage = ImageDataFactory.create(logoFile.absolutePath)
            var img = Image(dataimage).setHeight(100f).setWidth(100f).setHorizontalAlignment(
                HorizontalAlignment.CENTER)
            document.add(img)



            val arabicFont = PdfFontFactory.createFont(fontFile.absolutePath, PdfEncodings.IDENTITY_H)
            val arabicText = "كشف حساب"


            // إنشاء فقرة مع النص العربي
            val paragraph = Paragraph(languageProcessor.process(arabicText))
                .setFont(arabicFont)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20f)
                .setBaseDirection(BaseDirection.RIGHT_TO_LEFT) // محاذاة النص لليمين

            // إضافة الفقرة إلى الوثيقة
            document.add(paragraph)



            var   clender =  Calendar.getInstance()
            var formater = SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale.ENGLISH)

            formater.timeZone = TimeZone.getDefault()

            var timee = formater.format(clender.time)



            document.add(
                Paragraph(languageProcessor.process("  تاريخ الطباعة:\t ${timee} ")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.LEFT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))


            document.add(
                Paragraph(languageProcessor.process("الاسم :\t ${allData[0].customer_name.toString()} ")).setFont(arabicFont).setTextAlignment(
                    TextAlignment.RIGHT).setFontSize(15f).setBaseDirection(BaseDirection.RIGHT_TO_LEFT))

            document.add(Paragraph(" "))


            val table = Table(floatArrayOf(6f, 6f, 6f,6f)).setBaseDirection(BaseDirection.RIGHT_TO_LEFT)
            table.setTextAlignment(TextAlignment.CENTER)
            table.setHorizontalAlignment(HorizontalAlignment.CENTER)


            table.addCell(
                Paragraph(languageProcessor.process("  التاريخ")).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))
            table.addCell(
                Paragraph(languageProcessor.process("  المبلغ")).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))
            table.addCell(
                Paragraph(languageProcessor.process("  التفاصيل")).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))

            table.addCell(
                Paragraph(languageProcessor.process("  له/ عليه")).setFont(arabicFont).setFontSize(15f).setBaseDirection(
                    BaseDirection.RIGHT_TO_LEFT))


            var detelsList = kotlin.collections.ArrayList<kotlin.collections.List<String>>()

            allData.forEach { ss ->

                detelsList.add(listOf(ss.date.toString(),ss.amount.toString(),ss.description.toString(),ss.d_type.toString()))

            }
            document.add(Paragraph(" "))


            detelsList.forEach { prof->
                for (data in prof) {
                    table.addCell(
                        Paragraph(languageProcessor.process(data)).setFontSize(14f).setFont(arabicFont).setBaseDirection(
                            BaseDirection.RIGHT_TO_LEFT))
                }
            }

            document.add(Paragraph(" "))
            document.add(table)

            document.close()

            val pdfFile = File(path)
            Toast.makeText(this,"على وشك الانتهاء",Toast.LENGTH_SHORT).show()
            showOpenOptions(pdfFile)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun showOpenOptions(file: File) {
        val openIntent = Intent(Intent.ACTION_VIEW)
        openIntent.type = "application/pdf"

        // تحديد ملف PDF للفتح باستخدام FileProvider
        val fileUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(this, packageName  + ".fileprovider", file)
        } else {
            Uri.fromFile(file)
        }

        openIntent.setDataAndType(fileUri, "application/pdf")

        // منح الأذونات المؤقتة للوصول إلى الملف
        openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // استدعاء نافذة الفتح
        val chooserIntent = Intent.createChooser(openIntent, "فتح الملف باستخدام:")
        startActivity(chooserIntent)
    }



    private fun imlmnet() {
        columnFor = findViewById(R.id.columnFor)
        columnDate = findViewById(R.id.columnDate)
        toolbar_pdf = findViewById(R.id.toolbar_pdf)
        tableLayout = findViewById(R.id.tableLayout)
        columnAmount = findViewById(R.id.columnAmount)
        columnDetels = findViewById(R.id.columnDetels)
        toolbar_tital = findViewById(R.id.toolbar_tital)
        toolbar_search = findViewById(R.id.toolbar_search)
        main_amount_detels = findViewById(R.id.main_amount_detels)
        toolbar_back_toggle = findViewById(R.id.toolbar_back_toggle)
        tableRow_first = findViewById(R.id.tableRow_first)

        layoutMainSerch = findViewById(R.id.layoutMainSerch)

        inputSetrch = findViewById(R.id.inputSetrch)
        layoutAppBar1 = findViewById(R.id.layoutAppBar1)
        btnCloseSerch = findViewById(R.id.btnCloseSerch)

        textShowForAALIh =findViewById(R.id.textShowForAALIh)

        btnPhoneIcon =findViewById(R.id.btnPhoneIcon)
        btnAddAmount =findViewById(R.id.btnAddAmount)
        textShowForLLAh =findViewById(R.id.textShowForLLAh)




        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_show_debt_amount)


        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)


        btnDone = dialog.findViewById(R.id.btnDone)
        textDate =dialog.findViewById(R.id.textDate)
        textTime = dialog.findViewById(R.id.textTime)
        textAmount = dialog.findViewById(R.id.textAmount)
        textDetels = dialog.findViewById(R.id.textDetels)
        btnEditDebt =dialog.findViewById(R.id.btnEditDebt)
        textHederName = dialog.findViewById(R.id.textHederName)
        icon_for = dialog.findViewById(R.id.icon_for)



        dialogUpdate = Dialog(this)
        dialogUpdate.setContentView(R.layout.dialog_update_form)



        dialogUpdate.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogUpdate.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialogUpdate.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogUpdate.window?.setGravity(Gravity.CENTER)
        chipForLLAh = dialogUpdate.findViewById(R.id.chipForLLAh)
        textDateTime = dialogUpdate.findViewById(R.id.textDateTime)
        chipPirntFor = dialogUpdate.findViewById(R.id.chipPirntFor)
        chipForAAlih = dialogUpdate.findViewById(R.id.chipForAAlih)
        btnSaveUpdate =dialogUpdate. findViewById(R.id.btnSaveUpdate)
        input_amount_number2 = dialogUpdate.findViewById(R.id.input_amount_number2)
        input_amount_detels2 = dialogUpdate.findViewById(R.id.input_amount_detels2)







    }
}
// إضافة متغير للبحث
//private lateinit var searchEditText: EditText
//
//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_amount_detels)
//
//    customer_id = intent.getStringExtra("cos_id")!!
//
//    debtMVVM = ViewModelProviders.of(this)[DebtMVVM::class.java]
//
//    searchEditText = findViewById(R.id.searchEditText) // تأكد من وجود EditText في ملف XML
//
//    observeCategories()
//
//    // إضافة مراقب للنص المدخل
//    searchEditText.addTextChangedListener(object : TextWatcher {
//        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//        override fun afterTextChanged(s: Editable?) {
//            val query = s.toString().lowercase()
//            debtMVVM.observeDebtWithCostmr().value?.let { originalList ->
//                // تصفية البيانات
//                val filteredList = originalList.filter { client ->
//                    client.date.contains(query, ignoreCase = true) ||
//                            client.amount.toString().contains(query, ignoreCase = true) ||
//                            client.description.contains(query, ignoreCase = true)
//                }
//                // تحديث الجدول
//                populateTable(filteredList)
//            }
//        }
//    })
//}
//
//private fun populateTable(clients: List<DebtWithCustomer>) {
//    tableLayout.children.forEach { chip ->
//        if (chip.id != tableRow_first.id) {
//            tableLayout.removeView(chip)
//        }
//    }
//
//    if (clients.isNotEmpty()) {
//        toolbar_tital.text = clients[0].customer_name
//        textHederName.text = clients[0].customer_name
//    }
//
//    for (client in clients) {
//        val tableRow = TableRow(this)
//        tableRow.layoutParams = TableRow.LayoutParams(
//            TableRow.LayoutParams.MATCH_PARENT,
//            TableRow.LayoutParams.WRAP_CONTENT
//        )
//
//        tableRow.setBackgroundResource(R.drawable.table_row_background)
//
//        val nameTextView = TextView(ContextThemeWrapper(this, R.style.tableBack))
//        nameTextView.text = client.date
//        nameTextView.gravity = Gravity.CENTER
//        nameTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.5f)
//
//        tableRow.addView(nameTextView)
//
//        val ageTextView = TextView(ContextThemeWrapper(this, R.style.tableBack))
//        ageTextView.text = client.amount.toString()
//        ageTextView.gravity = Gravity.CENTER
//        ageTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
//        tableRow.addView(ageTextView)
//
//        val emailTextView = TextView(ContextThemeWrapper(this, R.style.tableBack))
//        emailTextView.text = client.description
//        emailTextView.gravity = Gravity.CENTER
//        emailTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2f)
//        tableRow.addView(emailTextView)
//
//        val forHow = TextView(ContextThemeWrapper(this, R.style.tableBack))
//        forHow.text = client.d_type
//        forHow.gravity = Gravity.CENTER
//        forHow.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 0.5f)
//        if (client.d_type == "له") {
//            forHow.setTextColor(Color.GREEN)
//        } else {
//            forHow.setTextColor(Color.RED)
//        }
//        tableRow.addView(forHow)
//
//        tableLayout.addView(tableRow)
//        tableRow.setOnClickListener {
//            preperToUpdate(client)
//        }
//
//        val divider = View(this)
//        divider.layoutParams = TableRow.LayoutParams(
//            TableRow.LayoutParams.MATCH_PARENT,
//            1
//        )
//        divider.setBackgroundColor(Color.BLACK)
//        tableLayout.addView(divider)
//    }
//}
