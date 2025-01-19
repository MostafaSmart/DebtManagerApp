package com.smarttec.moveisapptow.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.smarttec.moveisapptow.R
import com.smarttec.moveisapptow.data.models.CustomerDebtsSummary
import com.smarttec.moveisapptow.data.models.MovesModel
import com.smarttec.moveisapptow.ui.activtys.AddActivity
import com.smarttec.moveisapptow.ui.activtys.AmountDetelsActivity

class CusRVAdapter(): RecyclerView.Adapter<CusRVAdapter.Viewha>() {
    var count:Int = 0
    var custmor:List<CustomerDebtsSummary> = ArrayList()

    fun setMovesList(categoryList: List<CustomerDebtsSummary>, count2:Int){
        this.custmor = categoryList
        this.count = count2
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CusRVAdapter.Viewha {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_cusmer_summry,parent,false)



        return Viewha(v)
    }



    override fun getItemCount(): Int {
    return this.count
    }
    override fun onBindViewHolder(holder: CusRVAdapter.Viewha, position: Int) {

       var data = custmor[position]

        holder.item_name_text.text = data.customer_name
        holder.item_count_aboyt.text =data.total_debts_for.toString()
        holder.item_count_forhim.text = data.total_debts_on.toString()
        holder.item_totalCount_forhim.text = data.count_debts_on.toString()
        holder.item_totalCount_aboyt.text = data.count_debts_for.toString()


        holder.item_add_btn.setOnClickListener {
            val myintint = Intent(holder.itemView.context, AddActivity::class.java)
            myintint.putExtra("frgm", "amount")
            myintint.putExtra("name", data.customer_name)
            myintint.putExtra("id", data.customer_id.toString())

            holder.itemView.context.startActivity(myintint)
        }

        holder.itemView.setOnClickListener {
           if(data.count_debts_on <=0 && data.count_debts_for <= 0){

               Toast.makeText(holder.itemView.context,"لا يوجد مبالغ مسجلة",Toast.LENGTH_SHORT).show()
           }
            else{

               val myintint = Intent(holder.itemView.context,  AmountDetelsActivity::class.java)
               myintint.putExtra("cos_id", data.customer_id.toString())
               holder.itemView.context.startActivity(myintint)
           }

        }

    }

    class Viewha(vim: View) :RecyclerView.ViewHolder(vim) {
        val item_add_btn = vim.findViewById<ImageButton>(R.id.item_add_btn);
        val item_name_text = vim.findViewById<TextView>(R.id.item_name_text);
        val item_count_aboyt = vim.findViewById<TextView>(R.id.item_count_aboyt);
        val item_count_forhim = vim.findViewById<TextView>(R.id.item_count_forhim);
        val item_totalCount_aboyt = vim.findViewById<TextView>(R.id.item_totalCount_aboyt)
        val item_totalCount_forhim = vim.findViewById<TextView>(R.id.item_totalCount_forhim)

    }
}