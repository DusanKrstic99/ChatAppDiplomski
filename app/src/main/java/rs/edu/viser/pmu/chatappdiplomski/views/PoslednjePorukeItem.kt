package rs.edu.viser.pmu.chatappdiplomski.views

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.poslednja_poruka.view.*
import rs.edu.viser.pmu.chatappdiplomski.models.KorisnikModel
import rs.edu.viser.pmu.chatappdiplomski.models.PorukaModel
import rs.edu.viser.pmu.chatappdiplomski.R

class PoslednjePorukeItem(val chatMessage: PorukaModel): Item<GroupieViewHolder>() {
    var komePisemo: KorisnikModel? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        var komePisemoId: String
        if (chatMessage.odId == FirebaseAuth.getInstance().uid) {
            komePisemoId = chatMessage.zaId
        } else {
            komePisemoId = chatMessage.odId
        }

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/prijatelji/$uid/$komePisemoId")
        val ref2 = FirebaseDatabase.getInstance().getReference("/korisnici/${chatMessage.odId}")
        ref2.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }
            var korisnik:String=""
            var korisnikpom:String=""
            override fun onDataChange(p0: DataSnapshot) {
                korisnikpom = p0.getValue(KorisnikModel::class.java)?.uid ?: return
                korisnik = p0.getValue(KorisnikModel::class.java)?.username ?: return

                ref.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        komePisemo = p0.getValue(KorisnikModel::class.java)
                                if(korisnikpom == uid) {
                                    viewHolder.itemView.poruka_poslednja_poruka.text = "Vi : " + chatMessage.text
                                }
                        else {
                                    viewHolder.itemView.poruka_poslednja_poruka.text =  korisnik+": " + chatMessage.text
                                }

                        viewHolder.itemView.username_textview_poslednja_poruka.text = komePisemo?.username

                        val targetImageView = viewHolder.itemView.imageview_poslednja_poruka
                        Picasso.get().load(komePisemo?.slikaUrl).into(targetImageView)
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.poslednja_poruka
    }
}