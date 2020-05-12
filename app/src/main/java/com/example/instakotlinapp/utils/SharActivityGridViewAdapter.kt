package com.example.instakotlinapp.utils

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import com.example.instakotlinapp.R
import kotlinx.android.synthetic.main.tek_sutun_grid_resim.view.*

class SharActivityGridViewAdapter(context: Context, resource: Int, var klasordekiDosyalar: ArrayList< String>) : ArrayAdapter<String>(context, resource, klasordekiDosyalar) {

    var inflater: LayoutInflater
    var tekSutunResim: View? = null
    lateinit var viewHolder: ViewHolder

    init {
        inflater = LayoutInflater.from(context)
    }

    inner class ViewHolder() {
        lateinit var imageView: GridImageView
        lateinit var progressBar: ProgressBar
        lateinit var tvSure: TextView


    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        tekSutunResim = convertView

        if (tekSutunResim == null) {
            tekSutunResim = inflater.inflate(R.layout.tek_sutun_grid_resim, parent, false)
            viewHolder = ViewHolder()
            //tek bir view holder nesnesi olacağı için belleği etkili bir şekilde kullanırız
            viewHolder.imageView = tekSutunResim!!.imgTekSutunImage
            viewHolder.progressBar = tekSutunResim!!.progressBar
            viewHolder!!.tvSure = tekSutunResim!!.tvSure

            //view holder nesenesini teksutunresime tuttururuz böylece else kısmında da view holder nesnesinden yararlanırız.
            tekSutunResim!!.setTag(viewHolder)
        } else {

            viewHolder = tekSutunResim!!.getTag() as ViewHolder


        }
        var dosyaYolu = klasordekiDosyalar.get(position)
        var dosyaTuru = dosyaYolu.substring(dosyaYolu.lastIndexOf("."))
        if (dosyaTuru.equals(".mp4")) {

            viewHolder.tvSure.visibility = View.VISIBLE
            var retriver = MediaMetadataRetriever()
            retriver.setDataSource(context, Uri.parse("file://" + dosyaYolu))
            var videoSuresi = retriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            var videoSuresiLong = videoSuresi.toLong()
            viewHolder.tvSure.setText(videoSuresi)
            UniversalImageLoader.setImage(
                klasordekiDosyalar.get(position),
                viewHolder.imageView,
                viewHolder.progressBar,
                "file:/"
            )

        } else {
            viewHolder.tvSure.visibility = View.GONE

            UniversalImageLoader.setImage(
                klasordekiDosyalar.get(position),
                viewHolder.imageView,
                viewHolder.progressBar,
                "file:/"
            )

        }






        return tekSutunResim!!

    }


}