package rs.edu.viser.pmu.chatappdiplomski.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_zahtevi.*
import rs.edu.viser.pmu.chatappdiplomski.R
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import rs.edu.viser.pmu.chatappdiplomski.models.ZahtevModel
import rs.edu.viser.pmu.chatappdiplomski.views.PrihvatiOdbijItem

class ZahteviActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zahtevi)
        fetchKorisnike()


    }
    private fun fetchKorisnike() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/korisnici")
        val ref2 = FirebaseDatabase.getInstance().getReference("/zahtevi/$uid")
        ref2.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var list= mutableListOf<String>()
                var koSalje= mutableListOf<String>()

                snapshot.children.forEach{
                    it.getValue(ZahtevModel::class.java)?.odKoga?.let { it1 -> koSalje.add(it1) }
                    it.getValue(ZahtevModel::class.java)?.zaKoga?.let { it1 -> list.add(it1) }

                }

                ref.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        var trenutniKorisnik: KorisnikModel =
                            KorisnikModel()
                        val adapter = GroupAdapter<GroupieViewHolder>()
                        p0.children.forEach {
                            val korisnik = it.getValue(KorisnikModel::class.java)
                            val uid = FirebaseAuth.getInstance().uid
                            if (korisnik != null) {
                                if (korisnik.uid == uid) {
                                    trenutniKorisnik = korisnik
                                }
                            }
                        }
                        p0.children.forEach {
                            val korisnik = it.getValue(KorisnikModel::class.java)
                            val uid = FirebaseAuth.getInstance().uid
                            if (korisnik != null && korisnik.uid != uid && list.contains(uid) && koSalje.contains(korisnik.uid)) {
                                adapter.add(
                                    PrihvatiOdbijItem(
                                        korisnik,
                                        trenutniKorisnik
                                    )
                                )
                            }
                        }
                        recyclerview_zahtevi.adapter = adapter
                    }
                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
            }

        })

    }
}
