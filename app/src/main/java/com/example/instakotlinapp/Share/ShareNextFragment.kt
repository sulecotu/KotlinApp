package com.example.instakotlinapp.Share

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.example.instakotlinapp.utils.UniversalImageLoader
import kotlinx.android.synthetic.main.fragment_share_next.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 */
class ShareNextFragment : Fragment() {
    var secilenResimYolu:String?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_share_next, container, false)
        UniversalImageLoader.setImage(secilenResimYolu!!,view!!.imgSecilenResim,null,"file://")

        return view
    }
    @Subscribe(sticky = true)
    internal fun onSecilenResimEvent(secilenResim : EventbusDataEvents.PaylasilacakResmiGonder) {
        secilenResimYolu = secilenResim!!.resimYolu!!
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)

    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

}
