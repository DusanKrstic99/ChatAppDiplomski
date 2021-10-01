package rs.edu.viser.pmu.chatappdiplomski.views

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.poruka_od_korisnika.view.*
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import rs.edu.viser.pmu.chatappdiplomski.R

class PorukaOdItem(val text: String, val korisnik: KorisnikModel): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textview_od_korisnika.text = text
        val uri = korisnik.slikaUrl
        val targetImageView = viewHolder.itemView.imageview_korisnika
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.poruka_od_korisnika
    }
}