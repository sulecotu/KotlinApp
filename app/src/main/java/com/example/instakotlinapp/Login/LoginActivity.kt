package com.example.instakotlinapp.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etSifre
import kotlinx.android.synthetic.main.fragment_kayit.*

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth:FirebaseAuth
    lateinit var mRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth=FirebaseAuth.getInstance()
        mRef=FirebaseDatabase.getInstance().reference
        init()
    }
    fun init(){
        etEmailTelefonUsername.addTextChangedListener(watcher)
        etSifre.addTextChangedListener(watcher)

        btnGirisYap.setOnClickListener {
            oturumAcacakKullaniciyiDenetle(etEmailTelefonUsername.text.toString(),etSifre.text.toString())

        }
    }

    private fun oturumAcacakKullaniciyiDenetle(emailPhoneNumberUserName: String, sifre: String) {
        mRef.child("kullanıcılar").orderByChild("email").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for(ds in p0!!.children){
                    var okunanKullanici=ds.getValue(Users::class.java)
                    if(okunanKullanici!!.email!!.toString().equals(emailPhoneNumberUserName)){
                        oturumAc(okunanKullanici, sifre, false)
                        break

                    }
                    else if(okunanKullanici!!.kullanici_adi!!.toString().equals(emailPhoneNumberUserName)){
                        oturumAc(okunanKullanici, sifre, false)
                        break

                    }
                    else if(okunanKullanici!!.telefon_numarasi!!.toString().equals(emailPhoneNumberUserName)){
                        oturumAc(okunanKullanici, sifre, true)
                        break

                    }
                }
            }

        })

    }

    private fun oturumAc(okunanKullanici: Users, sifre: String, telefonIleGiris: Boolean) {
        var girisYapacakEmail=""
        if(telefonIleGiris==true){
            girisYapacakEmail=okunanKullanici.email_telefon_numarasi.toString()
        }
        else{
            girisYapacakEmail=okunanKullanici.email.toString()
        }
        mAuth.signInWithEmailAndPassword(girisYapacakEmail,sifre)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                override fun onComplete(p0: Task<AuthResult>) {
                    if(p0!!.isSuccessful){
                        Toast.makeText(this@LoginActivity,"Oturum Açıldı :" + mAuth.currentUser!!.uid , Toast.LENGTH_SHORT).show()


                    }
                    else{
                        Toast.makeText(this@LoginActivity,"Hatalı Giriş :" , Toast.LENGTH_SHORT).show()


                    }
                }

            })


    }

    var watcher: TextWatcher = object : TextWatcher{
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(etEmailTelefonUsername.text.toString().length >=6 && etSifre.text.toString().length >=6){
                btnGirisYap.isEnabled=true
                btnGiris.setTextColor(ContextCompat.getColor(this@LoginActivity!!,R.color.beyaz))
                btnGiris.setBackgroundResource(R.drawable.register_button_aktif)
            }
            else{
                btnGirisYap.isEnabled=false
                btnGiris.setTextColor(ContextCompat.getColor(this@LoginActivity!!,R.color.sonukmavi))
                btnGiris.setBackgroundResource(R.drawable.register_button_aktif)

            }
        }

    }
}
