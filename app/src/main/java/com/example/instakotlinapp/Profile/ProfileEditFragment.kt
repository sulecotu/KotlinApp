package com.example.instakotlinapp.Profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.instakotlinapp.Model.Users

import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.example.instakotlinapp.utils.UniversalImageLoader
import com.nostra13.universalimageloader.core.ImageLoader
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.*
import kotlinx.android.synthetic.main.fragment_profile_edit.view.circleProfileImage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe




/**
 * A simple [Fragment] subclass.
 */
class ProfileEditFragment : Fragment() {

  lateinit   var circleProfileImageFragment:CircleImageView
    lateinit var gelenKullaniciBilgileri:Users

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view =inflater.inflate(R.layout.fragment_profile_edit, container, false)
        setupKullaniciBilgileri(view)

        view.imgClose.setOnClickListener {

            activity?.onBackPressed()


        }


        return view


    }

    private fun setupKullaniciBilgileri(view: View?) {
        view!!.etProfileName.setText(gelenKullaniciBilgileri!!.ad_soyad)
        view!!.etUserName.setText(gelenKullaniciBilgileri!!.kullanici_adi)
        //biyografinin boş olma ihtimali olduğu için if döndürdük
        if(!!gelenKullaniciBilgileri!!.kulaniciDetaylari!!.biyografi.isNullOrEmpty()){
            view!!.etUserBio.setText(gelenKullaniciBilgileri!!.kulaniciDetaylari!!.biyografi)
        }
        var imgURL=gelenKullaniciBilgileri!!.kulaniciDetaylari!!.profilResmi
        UniversalImageLoader.setImage(imgURL!!,view!!.circleProfileImage,view!!.progressBar,"")

    }

    ////////////////EVENTBUS///////
    @Subscribe(sticky = true)
    internal fun onKullaniciBilgileriEvent(kullaniciBilgileri : EventbusDataEvents.KullaniciBilgileriniGonder){
     gelenKullaniciBilgileri=kullaniciBilgileri!!.kullanici!!





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
