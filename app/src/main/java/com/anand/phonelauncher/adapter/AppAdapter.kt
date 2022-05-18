package com.anand.phonelauncher.adapter;

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.anand.launcherlib.model.ApplicationInfo
import com.anand.phonelauncher.R
import com.google.android.material.textview.MaterialTextView
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class AppAdapter(
    private val context: Context,
    private var arrayList: ArrayList<ApplicationInfo>?,
    private var filterarrayList: ArrayList<ApplicationInfo> = ArrayList<ApplicationInfo>(),
    private var listerns: RecyclerClickInterface,


    ) :
        RecyclerView.Adapter<AppAdapter.ViewHolder>(), Filterable {


    init {


        arrayList?.let { filterarrayList.addAll(it) }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.app_list_row, parent, false)
        )
    }




    override fun getItemCount(): Int {

        return filterarrayList.size
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var pojo = filterarrayList.get(position)
        holder.name.text=pojo.label
        holder.icon.setImageDrawable(pojo.icon)
        holder.icon.setOnClickListener {
            listerns.onClick(pojo)
        }
        holder.icon.setOnLongClickListener(OnLongClickListener {


            var popup = PopupMenu(context, holder.icon)
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu);
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.uninstall ->

                       listerns.onLongCick(pojo)
                    else -> false
                }

                true
            }
            //displaying the popup
            popup.show();

            true
        })
    }

    interface RecyclerClickInterface {
        fun onClick(pojo: ApplicationInfo?)
        fun onLongCick(pojo: ApplicationInfo?)

    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {


        val name = item.findViewById<MaterialTextView>(R.id.name)
        val icon =item.findViewById<CircleImageView>(R.id.circleImageView)


    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults
            {


                if (constraint.toString().isEmpty()) {
                    filterarrayList.clear()
                    arrayList?.let { filterarrayList.addAll(it) }

                } else {
                    val query = constraint.toString().toLowerCase().trim()
                    filterarrayList.clear()
                    for (item in arrayList!!) {
                        if (item.label.toLowerCase().contains(query.toLowerCase())) {
                            filterarrayList.add(item)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filterarrayList
                return results /*returns null in the case of else loop so have passed userArrayListFiltered directly*/


            }


            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                notifyDataSetChanged()
            }

        }


    }

}