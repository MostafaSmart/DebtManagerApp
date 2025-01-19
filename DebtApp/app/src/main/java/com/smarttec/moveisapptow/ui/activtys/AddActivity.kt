package com.smarttec.moveisapptow.ui.activtys

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.ui.fragmints.AddAmountFragment
import com.smarttec.moveisapptow.ui.fragmints.AddCustomrFragment

class AddActivity : AppCompatActivity() {
    private lateinit var toolbar_tital: TextView
    private lateinit var add_main_root: LinearLayout
    private lateinit var toolbar_back_toggle: ImageView
    private lateinit var add_main_countener: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        imolmnet()


        val frgmType = intent.getStringExtra("frgm")
        val coustmerName = intent.getStringExtra("name")
        val coustmerID = intent.getStringExtra("id")


        if(frgmType== "amount"){
            toolbar_tital.text ="اضافة مبلغ"
            if(coustmerID!=null){
                val fragment = AddAmountFragment.newInstance(coustmerName!!,coustmerID)
                fragmntChing(fragment)
            }
            else{
                val fragment = AddAmountFragment()
                fragmntChing(fragment)

            }


        }
        else if(frgmType== "cost"){
            toolbar_tital.text ="اضافة اسم"

            fragmntChing(AddCustomrFragment())
        }



    }

    private fun imolmnet() {
        add_main_root = findViewById(R.id.add_main_root)
        toolbar_tital = findViewById(R.id.toolbar_tital)
        add_main_countener = findViewById(R.id.add_main_countener)
        toolbar_back_toggle = findViewById(R.id.toolbar_back_toggle)
    }

    fun fragmntChing(fragment: Fragment){
        var ching = supportFragmentManager.beginTransaction()


        ching.replace(R.id.add_main_countener,fragment)
        ching.commit()


    }
}