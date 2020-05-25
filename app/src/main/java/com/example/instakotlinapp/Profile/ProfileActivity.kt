package com.example.instakotlinapp.Profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instakotlinapp.Login.LoginActivity
import com.example.instakotlinapp.Model.Posts
import com.example.instakotlinapp.Model.UserPosts
import com.example.instakotlinapp.Model.Users
import com.example.instakotlinapp.R
import com.example.instakotlinapp.utils.BottomNavigationViewHelper
import com.example.instakotlinapp.utils.EventbusDataEvents
import com.example.instakotlinapp.utils.ProfilePostGridRecyclerAdapter
import com.example.instakotlinapp.utils.UniversalImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.greenrobot.eventbus.EventBus

class ProfileActivity : AppCompatActivity() {

    private val ACTIVITY_NO = 3
    private val TAG = "ProfileActivity"
    lateinit var tumGonderiler:ArrayList<UserPosts>


    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener:FirebaseAuth.AuthStateListener
    lateinit var mUser:FirebaseUser
    lateinit var mRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        setupAuthListener()
        mRef=FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        mUser=mAuth.currentUser!!

        tumGonderiler=ArrayList<UserPosts>()




        setupToolBar()
        kullaniciBilgileriniGetir()
        setupProfilePhoto()
        kullaniciPostlariniGetir(mUser.uid)
        imgGridView.setOnClickListener {
            setupRecyclerView(1)
        }

    }

    private fun kullaniciBilgileriniGetir() {
        profillDüzenleButon.isEnabled=false
        imgProfileSettings.isEnabled=false
        mRef.child("kullanıcılar").child(mUser!!.uid).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.getValue()!=null){
                    var okunanKullaniciBilgileri=p0!!.getValue(Users::class.java)


                    EventBus.getDefault().postSticky(EventbusDataEvents.KullaniciBilgileriniGonder(okunanKullaniciBilgileri))
                    profillDüzenleButon.isEnabled=true
                    imgProfileSettings.isEnabled=true


                    tvProfilAdiToolbar.setText(okunanKullaniciBilgileri!!.kullanici_adi)
                    tvProfilGercekAdi.setText(okunanKullaniciBilgileri!!.ad_soyad)
                    tvTakipciSayisi.setText((okunanKullaniciBilgileri!!.kulaniciDetaylari!!.takipciSayisi))
                    tvTakipSayisi.setText(okunanKullaniciBilgileri!!.kulaniciDetaylari!!.takipSayisi)
                    tvGönderiSayisi.setText(okunanKullaniciBilgileri!!.kulaniciDetaylari!!.gönderiSayisi)

                    var imgUrl=okunanKullaniciBilgileri!!.kulaniciDetaylari!!.profilResmi!!
                    UniversalImageLoader.setImage(imgUrl,circleProfileImage,progressBar, "")
                    if(!okunanKullaniciBilgileri!!.kulaniciDetaylari!!.biyografi!!.isNullOrEmpty()){
                        tvBiyografi.visibility=View.VISIBLE
                        tvBiyografi.setText(okunanKullaniciBilgileri!!.kulaniciDetaylari!!.biyografi!!)
                    }

                }


            }

        })

    }

    private fun setupProfilePhoto() {
        val imgUrl="i.pinimg.com/236x/48/44/60/4844604305738af2fecf9c42308e2826--heart-care-original-paintings.jpg"
        UniversalImageLoader.setImage(imgUrl,circleProfileImage,progressBar,"https://")
    }


    override fun onResume() {
        setupNavigationView()
        super.onResume()
    }
    //ayarlar tuşunu aktifleştirdik
    private fun setupToolBar() {
     imgProfileSettings.setOnClickListener {
     var intent=Intent(this,ProfileSettingsActivity::class.java)
         startActivity(intent)

     }
         profillDüzenleButon.setOnClickListener {

             profilRoot.visibility= View.GONE
             var transaction=supportFragmentManager.beginTransaction()
             transaction.replace(R.id.profileContainer, ProfileEditFragment())
             transaction.addToBackStack("editFragment eklendi")
             transaction.commit()


         }

    }

    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavigationView)

        //activity'in seçili olduğunu belirtmek
        var menu = bottomNavigationView.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.isChecked = true
    }
    private fun kullaniciPostlariniGetir(kullaniciID:String) {

        mRef.child("kullanıcılar").child(kullaniciID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var userID=kullaniciID
                var kullaniciAdi=p0!!.getValue(Users::class.java)!!.kullanici_adi
                var kullaniciFotoURL=p0!!.getValue(Users::class.java)!!.kulaniciDetaylari!!.profilResmi

                mRef.run {
                    child("posts").child(kullaniciID).addListenerForSingleValueEvent(object :ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0!!.hasChildren()) {
                                Log.e("HATA", "ÇOCUK VAR")
                                for (ds in p0!!.children) {
                                    var eklenecekUserPosts= UserPosts()
                                    eklenecekUserPosts.userID=userID
                                    eklenecekUserPosts.userName=kullaniciAdi
                                    eklenecekUserPosts.userPhotoURL=kullaniciFotoURL
                                    eklenecekUserPosts.postID =
                                        ds.getValue(Posts::class.java)!!.post_id
                                    eklenecekUserPosts.postURL =
                                        ds.getValue(Posts::class.java)!!.photo_url
                                    eklenecekUserPosts.postAciklama =
                                        ds.getValue(Posts::class.java)!!.acıklama

                                    tumGonderiler.add(eklenecekUserPosts)

                                }
                            }

                            setupRecyclerView(1)


                        }
                    })
                }


            }

        })

    }

    private fun setupRecyclerView(layoutCesidi: Int) {
        if(layoutCesidi==1){
            var kullaniciPostListe=profileRecyclerView
            kullaniciPostListe.adapter=ProfilePostGridRecyclerAdapter(tumGonderiler,this)
            kullaniciPostListe.layoutManager=GridLayoutManager(this,3)

        }else if(layoutCesidi==2){

        }

    }


    override fun onBackPressed() {
        profilRoot.visibility= View.VISIBLE
        super.onBackPressed()
    }
    private fun setupAuthListener() {

        mAuthListener=object :FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user=FirebaseAuth.getInstance().currentUser
                if(user==null){
                    //kullanıcı doğrulandıysa homeaktivitye geç
                    var intent=Intent(this@ProfileActivity,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish() //homeaktivityden geri tuşuna basınca logine geri döndürüyor

                }else{

                }

            }

        }
    }
    override fun onStart(){
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }
    override fun onStop(){
        super.onStop()
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

}
