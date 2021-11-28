package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DataAdapter(private val mList: List<DataClass>,var Context :Context) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        Picasso.get().load(ItemsViewModel.image).into(holder.imageView)
        holder.textView.text = ItemsViewModel.text
        holder.txt_price.text = ItemsViewModel.text_ratings
        holder.txt_itemname.text = ItemsViewModel.item_name
        holder.txt_item_price.text = ItemsViewModel.Item_price
        var counter=0

        holder.img_dropdown.setOnClickListener(View.OnClickListener {
            if (counter == 0){
                val animation = AnimationUtils.loadAnimation(
                    Context, R.anim.slide_down)
                //appending animation to textView
                holder.rl_itemvalue.startAnimation(animation)
                counter++
            }else{
                val animation = AnimationUtils.loadAnimation(
                    Context, R.anim.slide_up)
                //appending animation to textView
                holder.rl_itemvalue.startAnimation(animation)
                counter--
            }
        })

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val txt_price: TextView = itemView.findViewById(R.id.txt_ratings)
        val txt_itemname: TextView = itemView.findViewById(R.id.txt_itemsname)

        val txt_item_price: TextView = itemView.findViewById(R.id.txt_price)
        val rl_itemvalue:RelativeLayout = itemView.findViewById(R.id.rl_itemvalue)
        val img_dropdown:ImageView = itemView.findViewById(R.id.img_dropdown)

    }
}
