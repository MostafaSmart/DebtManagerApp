package com.smarttec.moveisapptow.ui.activtys

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.helper.FragmentCallBack
import com.smarttec.moveisapptow.ui.fragmints.AddAmountFragment
import com.smarttec.moveisapptow.ui.fragmints.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var main: View
    private lateinit var floatingBtnAdd: FloatingActionButton
    private lateinit var llLeftDrawer: LinearLayout
    private lateinit var drawerLayout: androidx.drawerlayout.widget.DrawerLayout
    private lateinit var dialog: Dialog
    private lateinit var radio_gnral: RadioButton
    private lateinit var radio_custmr: RadioButton
    private lateinit var radio_splayer: RadioButton
    private lateinit var radio_type_parint: RadioGroup

    private lateinit var layoutMainBar:LinearLayout
    private lateinit var btnCloseSerch:ImageButton
    private lateinit var inputSetrch: EditText
    private lateinit var layoutMainSerch: RelativeLayout


    private lateinit var tvAddCostmer: TextView
    private lateinit var tvAddAmount: TextView
    private lateinit var tvBest: TextView

    var flag = false

    private lateinit var ivCloseDrawer: ImageView
    private lateinit var txtDisplayName: TextView

    private lateinit var civProfile: de.hdodenhof.circleimageview.CircleImageView

    private lateinit var toolbar_main_serch:ImageView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var fragmentCallback:FragmentCallBack? = null
    private lateinit var toolbar_end: ImageView
    private lateinit var toolbar_tital: TextView
    private lateinit var toolbar_back_toggle: ImageView
    private lateinit var icon_main_typy:ImageButton

//    private lateinit var home_viewPiger:ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        impemnt()
        setSupportActionBar(toolbar)
        setUpDrawerToggle()

        setFragmet("عملاء")

        floatingBtnAdd.setOnClickListener{
            openAddActivty("amount")
        }
        toolbar_back_toggle.setOnClickListener {
            closeDr()
        }
        ivCloseDrawer.setOnClickListener {
            closeDr()
        }


        tvAddCostmer.setOnClickListener {
            openAddActivty("cost")

        }
        tvAddAmount.setOnClickListener {
            openAddActivty("amount")

        }

        tvBest.setOnClickListener {
            val myintint = Intent(this, BackupActivity::class.java)

            startActivity(myintint)
            finish()
        }


        toolbar_main_serch.setOnClickListener {
            layoutMainBar.visibility = View.GONE

            layoutMainSerch.visibility = View.VISIBLE

            inputSetrch.requestFocus()
        }

        icon_main_typy.setOnClickListener {
            dialog.show()
        }

        btnCloseSerch.setOnClickListener {
            fragmentCallback?.onShowEditText("close","")
            layoutMainSerch.visibility = View.GONE

            layoutMainBar.visibility = View.VISIBLE
        }


        radio_type_parint.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId){
                R.id.radio_custmr->{

                    setFragmet("عملاء")


                }
                R.id.radio_gnral->{
                    setFragmet("عام")

                }       R.id.radio_splayer->{
                setFragmet("موردين")

            }

            }
        }


        inputSetrch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isEmpty()){
                    fragmentCallback?.onShowEditText("close","")
                }
                else{
                    val query = s.toString().lowercase()
                    fragmentCallback?.onShowEditText("ser",query)
                }

            }
        })


    }

    private fun setFragmet(d_type: String) {
        val fragment = HomeFragment.newInstance(d_type)
        toolbar_tital.text = d_type
        dialog.cancel()
        fragmntChing(fragment)

    }

    private fun openAddActivty(type: String) {
        val myintint = Intent(this, AddActivity::class.java)
        myintint.putExtra("frgm", type)
        startActivity(myintint)
    }

    fun closeDr(){
        if(drawerLayout.isDrawerOpen(llLeftDrawer)){
            drawerLayout.closeDrawer(llLeftDrawer)
        }
        else{
            drawerLayout.openDrawer(llLeftDrawer)
        }
    }
    private fun setUpDrawerToggle() {
        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
//                main.translationX = slideOffset * drawerView.width
                (drawerLayout).bringChildToFront(drawerView)
                (drawerLayout).requestLayout()
            }
        }
        toggle.setToolbarNavigationClickListener {
//            if (!SearchFragment().isVisible) {
//                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                } else {
//                    drawerLayout.openDrawer(GravityCompat.START)
//                }
//            }
        }
        toggle.isDrawerIndicatorEnabled = false
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.back_icon, theme)
        toggle.setHomeAsUpIndicator(drawable)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }


    fun impemnt(){
        main = findViewById(R.id.main)

        drawerLayout = findViewById(R.id.drawerLayout)
        llLeftDrawer = findViewById(R.id.llLeftDrawer)

        toolbar = findViewById(R.id.toolbar)

        tvAddCostmer = findViewById(R.id.tvAddCostmer)

        tvAddAmount = findViewById(R.id.tvAddAmount)
        tvBest = findViewById(R.id.tvBest)

        floatingBtnAdd = findViewById(R.id.floatingBtnAdd)


        civProfile = findViewById(R.id.civProfile)

        ivCloseDrawer = findViewById(R.id.ivCloseDrawer)

        txtDisplayName = findViewById(R.id.txtDisplayName)

        icon_main_typy = findViewById(R.id.icon_main_typy)


        layoutMainSerch = findViewById(R.id.layoutMainSerch)

        inputSetrch = findViewById(R.id.inputSetrch)
        layoutMainBar = findViewById(R.id.layoutMainBar)
        btnCloseSerch = findViewById(R.id.btnCloseSerch)


        toolbar_main_serch = findViewById(R.id.toolbar_main_serch)
        toolbar_tital = findViewById(R.id.toolbar_tital)
        toolbar_back_toggle = findViewById(R.id.toolbar_back_toggle)


        dialog = Dialog(this)
        dialog.setContentView(R.layout.dailog_type1)


        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)



        radio_gnral = dialog.findViewById(R.id.radio_gnral)
        radio_custmr = dialog.findViewById(R.id.radio_custmr)
        radio_splayer = dialog.findViewById(R.id.radio_splayer)
        radio_type_parint = dialog.findViewById(R.id.radio_type_parint)

//        home_viewPiger = findViewById(R.id.home_viewPiger)

    }

    fun fragmntChing(fragment: Fragment){

        if(fragment is  FragmentCallBack){
            fragmentCallback = fragment
        }
        else{
            fragmentCallback = null
        }

        var ching = supportFragmentManager.beginTransaction()

        drawerLayout.closeDrawer(llLeftDrawer)
        ching.replace(R.id.container,fragment)
        ching.commit()
        flag = false

    }
}
