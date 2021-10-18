package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iamkamrul.expandablerecyclerviewlist.model.ParentListItem
import com.iamkamrul.expandablerecyclerviewlist.viewholder.ChildViewHolder
import com.iamkamrul.expandablerecyclerviewlist.viewholder.ParentViewHolder
import kotlin.collections.ArrayList


//feed_heading
//childexpandableheader

class MeetingExpandableHeaderAdapter(var context: Context, var arrayListString: ArrayList<String>) : RecyclerView.Adapter<MeetingExpandableHeaderAdapter.ViewHolders>() {

    private var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.feed_heading, parent, false)
        return ViewHolders(v)
    }

    override fun onBindViewHolder(holder: ViewHolders, @SuppressLint("RecyclerView") position: Int) {

        holder.headingTxt.setText(arrayListString.get(position))

        if (currentPosition == position) {
            //creating an animation
            val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)

            //toggling visibility
            holder.showmeeting_rv.visibility = View.VISIBLE

            //adding sliding effect
            holder.showmeeting_rv.startAnimation(slideDown)
            holder.arrowImage.animate().rotation(180f).start()
        } else {
            val slideDown = AnimationUtils.loadAnimation(context, R.anim.slideup)

            //adding sliding effect
            holder.showmeeting_rv.startAnimation(slideDown)
            holder.arrowImage.animate().rotation(0f).start()

            //toggling visibility
            holder.showmeeting_rv.visibility = View.GONE
        }
        holder.toggleView.setOnClickListener {
            if (currentPosition == position) {
                currentPosition = -1
            } else {
                currentPosition = position
            }


            //reloding the list
            notifyDataSetChanged()
        }
        holder.showmeeting_rv.layoutManager = LinearLayoutManager(context)
        val adapter = ScheduledDashboardAdapter()
        holder.showmeeting_rv.adapter = adapter
    }

    override fun getItemCount(): Int {
        return arrayListString.size
    }

    class ViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var toggleView: LinearLayout
        var showmeeting_rv: RecyclerView
        var arrowImage: ImageView
        var headingTxt: TextView

        init {
            toggleView = itemView.findViewById(R.id.toggleView)
            showmeeting_rv = itemView.findViewById(R.id.showmeeting_rv)
            arrowImage = itemView.findViewById(R.id.toggleIcon)
            headingTxt = itemView.findViewById(R.id.headingTxt)
        }
    }

}


class CategoryListViewHolder(view: View) : ChildViewHolder(view){
    fun bind(categoryList: CategoryList){
      //  itemView.findViewById<TextView>(R.id.subject_tv).text = categoryList.name
     //   itemView.findViewById<TextView>(R.id.headingTxt).text = categoryList.name
    }
}

class CategoryViewHolder(itemView: View) : ParentViewHolder(itemView) {
    private lateinit var animation: RotateAnimation

    fun bind(category: Category){
        itemView.findViewById<TextView>(R.id.headingTxt).text = category.name
    }

    override fun onExpansionToggled(expanded: Boolean) {
        super.onExpansionToggled(expanded)
        animation = if (expanded)
            RotateAnimation(
                    180f,
                    0f,
                    RotateAnimation.RELATIVE_TO_SELF,
                    0.5f,
                    RotateAnimation.RELATIVE_TO_SELF,
                    0.5f
            )
        else
            RotateAnimation(
                    -1 * 180f,
                    0f,
                    RotateAnimation.RELATIVE_TO_SELF,
                    0.5f,
                    RotateAnimation.RELATIVE_TO_SELF,
                    0.5f
            )

        animation.duration = 200
        animation.fillAfter = true
        itemView.findViewById<ImageView>(R.id.toggleIcon).startAnimation(animation)

    }

    override fun setExpanded(expanded: Boolean) {
        super.setExpanded(expanded)
        if (expanded)itemView.findViewById<ImageView>(R.id.toggleIcon).rotation = 180f
        else itemView.findViewById<ImageView>(R.id.toggleIcon).rotation = 0f
    }

}



data class CategoryList(val name: String)

data class Category(val name: String, val movieList: List<CategoryList>) : ParentListItem {
    override fun getChildItemList(): List<*> = movieList
    override fun isInitiallyExpanded(): Boolean = false
}