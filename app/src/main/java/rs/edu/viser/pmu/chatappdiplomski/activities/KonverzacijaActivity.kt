package rs.edu.viser.pmu.chatappdiplomski.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_konverzija.*
import rs.edu.viser.pmu.chatappdiplomski.R
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import rs.edu.viser.pmu.chatappdiplomski.models.PorukaModel
import rs.edu.viser.pmu.chatappdiplomski.views.PorukaOdItem
import rs.edu.viser.pmu.chatappdiplomski.views.PorukaZaItem

class KonverzacijaActivity : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()
    var KomeSaljemo: KorisnikModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konverzija)
        recyclerview_konverzija.adapter = adapter

        KomeSaljemo = intent.getParcelableExtra("Korisnik")
        supportActionBar?.title = KomeSaljemo?.username
        osluskujPoruke()


        posalji_poruku.setOnClickListener{
            slanjePoruka()
        }

    }
    private fun osluskujPoruke() {
        val odId = FirebaseAuth.getInstance().uid
        val zaId = KomeSaljemo?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/poruke/$odId/$zaId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val poruka = p0.getValue(PorukaModel::class.java)
                if (poruka != null) {
                    if (poruka.odId == FirebaseAuth.getInstance().uid) {
                        val currentUser = PorukeActivity.trenutniKorisnik
                            ?: return
                        adapter.add(PorukaOdItem(poruka.text,currentUser))
                    } else {
                        adapter.add(PorukaZaItem(poruka.text,KomeSaljemo!!))
                    }
                }
                recyclerview_konverzija.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }

    private fun slanjePoruka() {
        val text = unos_poruke.text.toString()

        val odId = FirebaseAuth.getInstance().uid
        var KomeSaljemo: KorisnikModel? = null
         KomeSaljemo = intent.getParcelableExtra("Korisnik")
        val zaId = KomeSaljemo.uid

        if (odId == null) return

        val refOdZa = FirebaseDatabase.getInstance().getReference("/poruke/$odId/$zaId").push()

        val refZaOd = FirebaseDatabase.getInstance().getReference("/poruke/$zaId/$odId").push()

        val chatMessage = PorukaModel(
            refOdZa.key!!,
            text,
            odId,
            zaId,
            System.currentTimeMillis() / 1000
        )

        refOdZa.setValue(chatMessage)
            .addOnSuccessListener {
                unos_poruke.text.clear()
                recyclerview_konverzija.scrollToPosition(adapter.itemCount - 1)
            }

        refZaOd.setValue(chatMessage)

        val poslednjaporukaOdZa = FirebaseDatabase.getInstance().getReference("/poslednje-poruke/$odId/$zaId")
        poslednjaporukaOdZa.setValue(chatMessage)

        val poslednjaporukaZaOd = FirebaseDatabase.getInstance().getReference("/poslednje-poruke/$zaId/$odId")
        poslednjaporukaZaOd.setValue(chatMessage)
    }
}