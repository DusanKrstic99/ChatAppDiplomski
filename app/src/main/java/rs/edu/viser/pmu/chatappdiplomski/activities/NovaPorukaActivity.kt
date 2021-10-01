package rs.edu.viser.pmu.chatappdiplomski.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_nova_poruka.*
import rs.edu.viser.pmu.chatappdiplomski.R
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import rs.edu.viser.pmu.chatappdiplomski.views.KorisnikItem

class NovaPorukaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_poruka)
        fetchKorisnike()
    }
    private fun fetchKorisnike() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/prijatelji/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                p0.children.forEach {
                    val korisnik = it.getValue(KorisnikModel::class.java)
                    val uid = FirebaseAuth.getInstance().uid
                    if (korisnik != null && korisnik.uid != uid ) {
                        adapter.add(
                            KorisnikItem(
                                korisnik
                            )
                        )
                    }
                }
                adapter.setOnItemClickListener { item, view ->

                    val korItem = item as KorisnikItem

                    val intent = Intent(view.context, KonverzacijaActivity::class.java)
                    intent.putExtra("Korisnik", korItem.korisnik)
                    startActivity(intent)

                    finish()

                }
                recyclerview_novaporuka.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}