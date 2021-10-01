package rs.edu.viser.pmu.chatappdiplomski.views

import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.korisnik_zahtev.view.*
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import rs.edu.viser.pmu.chatappdiplomski.R
import rs.edu.viser.pmu.chatappdiplomski.models.ZahtevModel


class ZahtevItem(val korisnik: KorisnikModel): Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.username_novi_zahtev.text = korisnik.username


            viewHolder.itemView.button_zahtev.setOnClickListener {

                slanjeZahteva()
                viewHolder.itemView.button_zahtev.visibility = View.GONE
            }
            Picasso.get().load(korisnik.slikaUrl).into(viewHolder.itemView.imageview_novi_zahtev)
        }


    override fun getLayout(): Int {
        return R.layout.korisnik_zahtev
    }
    private fun slanjeZahteva() {

        val korisnikUid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/zahtevi/$korisnikUid/${korisnik.uid}")
        val zahtev = ZahtevModel(
            korisnikUid!!,
            korisnik.uid
        )
        ref.setValue(zahtev)
        val ref2 = FirebaseDatabase.getInstance().getReference("/zahtevi/${korisnik.uid}/$korisnikUid")
        ref2.setValue(zahtev)
    }

}


