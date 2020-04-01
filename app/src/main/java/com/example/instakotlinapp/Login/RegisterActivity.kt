package com.example.instakotlinapp.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {
        tvEposta.setOnClickListener {
            viewTelefon.visibility=View.INVISIBLE
            viewEposta.visibility=View.VISIBLE
            etGirisYontemi.setText("")
            etGirisYontemi.inputType=InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            etGirisYontemi.setHint("E-Posta")
            btnIleri.isEnabled=false
        }
        tvTelefon.setOnClickListener {
            viewTelefon.visibility=View.VISIBLE
            viewEposta.visibility=View.INVISIBLE
            etGirisYontemi.setText("")
            etGirisYontemi.inputType=InputType.TYPE_CLASS_NUMBER
            etGirisYontemi.setHint("Telefon")
            btnIleri.isEnabled=false
        }

        etGirisYontemi.addTextChangedListener (object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start+before+count >= 10){
                    btnIleri.isEnabled=true
                    btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.beyaz))
                    btnIleri.setBackgroundResource(R.drawable.register_button_aktif)
                }
                else{

                    btnIleri.isEnabled=false
                    btnIleri.setTextColor(ContextCompat.getColor(this@RegisterActivity,R.color.sonukmavi))
                    btnIleri.setBackgroundResource(R.drawable.register_button)


                }
            }


        })

        btnIleri.setOnClickListener{
            if(etGirisYontemi.hint.toString().equals("Telefon")){
                loginRoot.visibility=View.GONE
                loginContainer.visibility=View.VISIBLE
                var transaction=supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer,TelefonKoduGirFragment())
                transaction.addToBackStack("telefonKoduGirFragmentEklendi")
                transaction.commit()
                EventBus.getDefault().postSticky(EventbusDataEvents.TelefonNoGonder(etGirisYontemi.text.toString()))
            }
            else{
                loginRoot.visibility=View.GONE
                loginContainer.visibility=View.VISIBLE
                var transaction=supportFragmentManager.beginTransaction()
                transaction.replace(R.id.loginContainer,EmailGirisYontemiFragment())
                transaction.addToBackStack("EmailGirisYontemiFragmentEklendi")
                transaction.commit()
                EventBus.getDefault().postSticky(EventbusDataEvents.EmailGonder(etGirisYontemi.text.toString()))

            }
        }
    }
    override fun onBackPressed() {
        loginRoot.visibility= View.VISIBLE
        super.onBackPressed()
    }

}
