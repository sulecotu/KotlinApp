package com.example.instakotlinapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.instakotlinapp.Model.UserPosts
import com.example.instakotlinapp.R
import kotlinx.android.synthetic.main.tek_post_recycler_item.view.*

class HomeFragmentRecylerAdapter(var context:Context,var tumGonderiler:ArrayList<UserPosts>):RecyclerView.Adapter<HomeFragmentRecylerAdapter.MyViewHolder>() {
    override fun getItemCount(): Int {
        return tumGonderiler.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
     var viewHolder=LayoutInflater.from(context).inflate(R.layout.tek_post_recycler_item,parent,false)
        return MyViewHolder(viewHolder)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(position,tumGonderiler.get(position))

    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var tumLayout=itemView as ConstraintLayout
        var profilImage=tumLayout.imgUserProfile
        var userNameTitle=tumLayout.tvKullaniciAdiBaslik
        var gonderi=tumLayout.imgPostResim
        var gonderiAciklama=tumLayout.tvPostAciklama

        fun setData(position: Int,oankiGonderi:UserPosts) {
            userNameTitle.setText(oankiGonderi.userName)
            UniversalImageLoader.setImage(oankiGonderi.postURL!!,gonderi,null,"")
            gonderiAciklama.setText(oankiGonderi.postAciklama)
            UniversalImageLoader.setImage(oankiGonderi.userPhotoURL!!,profilImage,null,"")
        }

    }
}