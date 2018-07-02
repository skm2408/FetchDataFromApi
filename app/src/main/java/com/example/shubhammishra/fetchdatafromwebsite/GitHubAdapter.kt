package com.example.shubhammishra.fetchdatafromwebsite

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shubhammishra.fetchdatafromwebsite.R.id.imageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_view_gitdata.view.*

class GitHubAdapter(var user:ArrayList<GitHubUser>):RecyclerView.Adapter<GitHubAdapter.GitHubHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubHolder {
        val lf=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        return GitHubHolder(lf.inflate(R.layout.list_view_gitdata,parent,false))
    }

    override fun getItemCount(): Int =user.size

    override fun onBindViewHolder(holder: GitHubHolder, position: Int) {
        holder.itemView.tvid.text=user[position].id.toString()
        holder.itemView.tvlogin.text=user[position].login
        holder.itemView.tvhtml.text=user[position].html_url
        holder.itemView.tvavtar.text=user[position].avtar_url
        holder.itemView.tvscore.text= user[position].score.toString()
        Picasso.get().load(user[position].avtar_url).resize(1000,1000).onlyScaleDown().into(holder.itemView.imageView);

    }

    class GitHubHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }

}