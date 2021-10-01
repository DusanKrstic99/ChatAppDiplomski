package rs.edu.viser.pmu.chatappdiplomski.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_poruke.*
import rs.edu.viser.pmu.chatappdiplomski.R
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import rs.edu.viser.pmu.chatappdiplomski.models.PorukaModel
import rs.edu.viser.pmu.chatappdiplomski.views.PoslednjePorukeItem

class PorukeActivity : AppCompatActivity() {
    companion object {
        var trenutniKorisnik: KorisnikModel? = null
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poruke)
        recyclerview_poslednje_poruke.adapter = adapter
        proveraDaLiJeKorisnikPrijavljen()
        osluskujPoslednjePoruke()
        trenutniKorisnik()
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, KonverzacijaActivity::class.java)


            val row = item as PoslednjePorukeItem
            intent.putExtra("Korisnik", row.komePisemo)
            startActivity(intent)
        }

    }
    private fun osveziRecyclerView() {
        adapter.clear()
        poslednjePorukeMap.values.forEach {
            adapter.add(
                PoslednjePorukeItem(
                    it
                )
            )
        }
    }
    val poslednjePorukeMap = HashMap<String, PorukaModel>()
    private fun osluskujPoslednjePoruke() {
        val zaId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/poslednje-poruke/$zaId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val poruka = p0.getValue(PorukaModel::class.java) ?: return
                poslednjePorukeMap[p0.key!!] = poruka
                osveziRecyclerView()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val poruka = p0.getValue(PorukaModel::class.java) ?: return
                poslednjePorukeMap[p0.key!!] = poruka
                osveziRecyclerView()
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun trenutniKorisnik() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/korisnici/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                trenutniKorisnik = p0.getValue(KorisnikModel::class.java)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun proveraDaLiJeKorisnikPrijavljen(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegistracijaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_nova_poruka -> {
                val intent = Intent(this, NovaPorukaActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_odjava -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegistracijaActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_svi -> {
                val intent = Intent(this, PrikazSvihActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_zahtevi -> {
                val intent = Intent(this, ZahteviActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigacija_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}