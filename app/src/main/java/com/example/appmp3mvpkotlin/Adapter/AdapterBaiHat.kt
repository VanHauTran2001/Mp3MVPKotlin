package com.example.appmp3mvpkotlin.Adapter

import Model.BaiHat
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appmp3mvpkotlin.R
import com.example.appmp3mvpkotlin.databinding.ItemListNhacBinding

class AdapterBaiHat : RecyclerView.Adapter<AdapterBaiHat.BaiHatViewHolder> {
    var binding: ItemListNhacBinding? = null
    var iListen: IListen? = null

    constructor(iListen: IListen) {
        this.iListen = iListen
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaiHatViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_nhac, parent, false)
        binding = DataBindingUtil.bind(view)
        return BaiHatViewHolder(binding!!.getRoot())
    }

    override fun onBindViewHolder(holder: BaiHatViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val baiHats: BaiHat = iListen!!.getData(position)!!
        binding!!.txtTenBaiHat.setText(baiHats.getNameSong())
        binding!!.txtTenCaSiBaiHat.setText(baiHats.getNameSinger())
        holder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
            iListen!!.onClickBaiHat(
                position
            )
        })
    }

    override fun getItemCount(): Int {
        return iListen!!.getCount()
    }

    class BaiHatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface IListen {
        fun getCount(): Int
        fun getData(position: Int): BaiHat?
        fun onClickBaiHat(position: Int)

    }
}