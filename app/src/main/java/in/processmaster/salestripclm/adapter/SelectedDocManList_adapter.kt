import `in`.processmaster.salestripclm.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelectedDocManList_adapter(
    var arrayListSelector: ArrayList<DocManagerModel>,
    var listner: IntegerInterface,
    var selectionType: Int
) : RecyclerView.Adapter<SelectedDocManList_adapter.ViewHolders>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolders {
        var itemView : View
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.selectedview, parent, false)
        return ViewHolders(itemView)
    }

    class ViewHolders(view: View) : RecyclerView.ViewHolder(view) {
        var name_tv=view.findViewById<TextView>(R.id.name_tv)
        var cross_iv=view.findViewById<ImageView>(R.id.cross_iv)

    }

    override fun onBindViewHolder(holder: SelectedDocManList_adapter.ViewHolders, position: Int) {
        holder.name_tv.setText(arrayListSelector.get(position).getName())

        holder.cross_iv.setOnClickListener({
            listner.passid(arrayListSelector.get(position).getId()!!,selectionType)
        })
    }

    override fun getItemCount(): Int {
        return arrayListSelector.size
    }
}