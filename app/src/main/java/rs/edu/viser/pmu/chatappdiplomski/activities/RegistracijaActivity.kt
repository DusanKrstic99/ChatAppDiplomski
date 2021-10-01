package rs.edu.viser.pmu.chatappdiplomski.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import rs.edu.viser.pmu.chatappdiplomski.R
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import java.util.*

class RegistracijaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Registracija.setOnClickListener{
            izvrsiRegistraciju()
        }

        PostojiNalog.setOnClickListener {
            val intent = Intent(this,
                PrijavaActivity::class.java)
            startActivity(intent)
        }
        OdabirSlike.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type= "image/*"
            startActivityForResult(intent,0)
        }
    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            OdabirSlikeView.setImageBitmap(bitmap)
            OdabirSlike.alpha = 0f


        }

    }
    private fun uploadSlikeNaFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/slike/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    cuvanjeKorisnikaUBazi(it.toString())
                }
            }
            .addOnFailureListener {

            }
    }
    private fun cuvanjeKorisnikaUBazi(URLSlike: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/korisnici/$uid")
        val korisnik = KorisnikModel(
            uid,
            Username.text.toString(),
            URLSlike
        )
        ref.setValue(korisnik)
            .addOnSuccessListener {
                val intent = Intent(this,
                    PorukeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
            }
    }
    private fun izvrsiRegistraciju(){
        val email = Email.text.toString()
        val password = Password.text.toString()
        val username = Username.text.toString()
        if(email.isEmpty() || password.isEmpty() || username.isEmpty() || selectedPhotoUri == null) {
            Toast.makeText(this,"Niste uneli sva polja",Toast.LENGTH_LONG).show()
            return
        }
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    uploadSlikeNaFirebaseStorage()
                    Toast.makeText(this, "Uspesna registracija sa e-mail adresom $email", Toast.LENGTH_LONG).show()
                }
        }
        else {
            Toast.makeText(this, "Loš format e-mail adrese", Toast.LENGTH_LONG).show()
            return
        }
    }

}