package com.example.instakotlinapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus

class RegisterActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {
    lateinit var manager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        manager = supportFragmentManager
        manager.addOnBackStackChangedListener(this)

        init()
    }

    private fun init() {
        tvGirisYap.setOnClickListener {
            var intent= Intent(this@RegisterActivity,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
        tvEposta.setOnClickListener {
            viewTelefon.visibility = View.INVISIBLE
            viewEposta.visibility = View.VISIBLE
            etGirisYontemi.setText("")
            etGirisYontemi.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            etGirisYontemi.setHint("E-Posta")
            btnIleri.isEnabled = false
        }
        tvTelefon.setOnClickListener {
            viewTelefon.visibility = View.VISIBLE
            viewEposta.visibility = View.INVISIBLE
            etGirisYontemi.setText("")
            etGirisYontemi.inputType = InputType.TYPE_CLASS_NUMBER
            etGirisYontemi.setHint("Telefon")
            btnIleri.isEnabled = false
        }

        etGirisYontemi.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length >= 10) {
                    btnIleri.isEnabled = true
                    btnIleri.setTextColor(
                        ContextCompat.getColor(
                            this@RegisterActivity,
                            R.color.beyaz
                        )
                    )
                    btnIleri.setBackgroundResource(R.drawable.register_button_aktif)
                } else {

                    btnIleri.isEnabled = false
                    btnIleri.setTextColor(
                        ContextCompat.getColor(
                            this@RegisterActivity,
                            R.color.sonukmavi
                        )
                    )
                    btnIleri.setBackgroundResource(R.drawable.register_button)


                }
            }


        })

        btnIleri.setOnClickListener {
            //girilen telefon numarası uygunssa
            if (etGirisYontemi.hint.toString().equals("Telefon")) {

                if(isValidTelefon(etGirisYontemi.text.toString())){
                    loginRoot.visibility = View.GONE
                    loginContainer.visibility = View.VISIBLE
                    var transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.loginContainer, TelefonKoduGirFragment())
                    transaction.addToBackStack("telefonKoduGirFragmentEklendi")
                    transaction.commit()
                    EventBus.getDefault().postSticky(EventbusDataEvents.KayitBilgileriniGonder(etGirisYontemi.text.toString(), null, null, null, false))

                }else{// eğer telefn numarası uygun değilse ekrana mesaj yazar
                    Toast.makeText(this,"Lütfen Geçerli Bir Telefon Numarası Giriniz",Toast.LENGTH_SHORT).show()


                }

            } else { /// girilen email uygunsa
                if(isValidEmail(etGirisYontemi.text.toString())){


                    loginRoot.visibility = View.GONE
                    loginContainer.visibility = View.VISIBLE
                    var transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.loginContainer, KayitFragment())
                    transaction.addToBackStack("EmailGirisYontemiFragmentEklendi")
                    transaction.commit()
                    EventBus.getDefault().postSticky(
                        EventbusDataEvents.KayitBilgileriniGonder(
                            null,
                            etGirisYontemi.text.toString(),
                            null,
                            null,
                            true
                        )
                    )

                }else {// email uygun değilse ekrana mesaj yazar

                    Toast.makeText(this, "Lütfen Geçerli Bir Email Adresi Giriniz", Toast.LENGTH_SHORT).show()
                }


                }
        }
    }


    override fun onBackStackChanged() {
        //geri tuşuna basınca bir önceki fragmente gider.
        val elemanSayisi = manager.backStackEntryCount

        if (elemanSayisi == 0) {
            loginRoot.visibility = View.VISIBLE

        }
    }
    ///kullanıcının girdiği emailin kontrolünü yapan fonksiyon

    fun isValidEmail(kontrolEdilecekEmail: String): Boolean {
        if (kontrolEdilecekEmail == null) {

            return false
        }

        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekEmail).matches()
    }
        //kullanıcının girdiği telefon numarasını kontrol eden fonksiyon
    fun isValidTelefon(kontrolEdilecekTelefon: String): Boolean {
        if (kontrolEdilecekTelefon == null || kontrolEdilecekTelefon.length > 14) {
            return false
        }
        return android.util.Patterns.PHONE.matcher(kontrolEdilecekTelefon).matches()

    }
}