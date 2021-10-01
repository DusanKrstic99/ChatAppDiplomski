package rs.edu.viser.pmu.chatappdiplomski.views

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.korisnik_red.view.*
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import rs.edu.viser.pmu.chatappdiplomski.R

class KorisnikItem(val korisnik: KorisnikModel): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_nova_poruka.text = korisnik.username
        Picasso.get().load(korisnik.slikaUrl).into(viewHolder.itemView.imageview_nova_poruka)
    }

    override fun getLayout(): Int {
        return R.layout.korisnik_red
    }
}