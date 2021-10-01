package rs.edu.viser.pmu.chatappdiplomski.views

import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.prihvati_odbij_zahtev.view.*
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import rs.edu.viser.pmu.chatappdiplomski.R


class PrihvatiOdbijItem(val korisnik: KorisnikModel, val trenutni: KorisnikModel): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.username_zahtev.text = korisnik.username

        viewHolder.itemView.button_odbij.setOnClickListener {

            odbij()
            viewHolder.itemView.button_odbij.visibility = View.GONE
            viewHolder.itemView.button_prihvati.visibility = View.GONE
        }
        viewHolder.itemView.button_prihvati.setOnClickListener {

            prihvati()
            viewHolder.itemView.button_odbij.visibility = View.GONE
            viewHolder.itemView.button_prihvati.visibility = View.GONE
        }
        Picasso.get().load(korisnik.slikaUrl).into(viewHolder.itemView.imageview_zahtev)
    }


    override fun getLayout(): Int {
        return R.layout.prihvati_odbij_zahtev
    }
    private fun prihvati() {
          val Uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/prijatelji/$Uid/${korisnik.uid}")
        val zahtev = KorisnikModel(korisnik.uid, korisnik.username, korisnik.slikaUrl)
        val zahtev2 = KorisnikModel(trenutni.uid, trenutni.username, trenutni.slikaUrl)
        ref.setValue(zahtev)
        val ref2 = FirebaseDatabase.getInstance().getReference("/prijatelji/${korisnik.uid}/$Uid")
        ref2.setValue(zahtev2)
        odbij()

    }
    private fun odbij() {
        val korisnikUid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/zahtevi/$korisnikUid/${korisnik.uid}")
        ref.removeValue()
        val ref2 = FirebaseDatabase.getInstance().getReference("/zahtevi/${korisnik.uid}/$korisnikUid")
        ref2.removeValue()
    }

}