package rs.edu.viser.pmu.chatappdiplomski.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import rs.edu.viser.pmu.chatappdiplomski.R

class PrijavaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Prijava.setOnClickListener{
            IzvrsiPrijavu()
        }

        VratiRegistraciju.setOnClickListener {
            finish()
        }
    }
    private fun IzvrsiPrijavu(){
        val email = EmailPrijava.text.toString()
        val password = PasswordPrijava.text.toString()
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Niste uneli email ili password", Toast.LENGTH_LONG).show()
            return
        }
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Toast.makeText(this, "Dobro došli", Toast.LENGTH_LONG).show()
                    val intent = Intent(this,
                        PorukeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Netacna e-mail adresa ili lozinka", Toast.LENGTH_SHORT).show()
                }
        }
        else {
            Toast.makeText(this, "Loš format e-mail adrese", Toast.LENGTH_LONG).show()
            return
        }
    }
}