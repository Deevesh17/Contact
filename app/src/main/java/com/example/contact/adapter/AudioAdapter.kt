package com.example.contact.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.R
import com.example.contact.musicroomdb.Audio
import kotlinx.android.synthetic.main.adapter_weatherrecentsearch.view.*

class AudioAdapter : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    private var oldList : List<Audio> = emptyList()
    lateinit var context : Context
    var lastPlayedImage : ImageView? = null
    var play : MediaPlayer? = null

    class AudioViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var audiotitle: TextView = itemView.viewTitle
        var playpPause : ImageView = itemView.playpause
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val view  = layoutinflater.inflate(R.layout.adapter_weatherrecentsearch,parent,false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.audiotitle.setText(oldList[position].audioTitle)
        holder.itemView.setOnClickListener {
            if( play != null) {
                if (play?.isPlaying!!) {
                    lastPlayedImage.let { it?.setImageResource(R.drawable.play) }
                    play?.stop()
                    play?.release()
                    play = null
                    play =
                        MediaPlayer.create(context, Uri.fromFile(oldList[position].audioFilePath))
                    play?.start()
                    holder.playpPause.setImageResource(R.drawable.stop)
                    lastPlayedImage = holder.playpPause
                } else {
                    play =
                        MediaPlayer.create(context, Uri.fromFile(oldList[position].audioFilePath))
                    play?.start()
                    holder.playpPause.setImageResource(R.drawable.stop)
                    lastPlayedImage = holder.playpPause
                }
            }
            else{
                play =
                    MediaPlayer.create(context, Uri.fromFile(oldList[position].audioFilePath))
                play?.start()
                lastPlayedImage = holder.playpPause
                holder.playpPause.setImageResource(R.drawable.stop)
            }
            play?.setOnCompletionListener {
                lastPlayedImage.let { it?.setImageResource(R.drawable.play) }
                play?.stop()
                play?.release()
                play = null
            }
        }

        holder.itemView.setOnLongClickListener {
            play?.stop()
            play?.release()
            play = null
            holder.playpPause.setImageResource(R.drawable.play)
            true
        }

    }

    override fun getItemCount(): Int {
        return oldList.size
    }
    fun setData(newContactList: List<Audio>,ctx : Context){
        val audioDiffUtil = AudioDiffUtil(oldList,newContactList)
        val diffUtilResult = DiffUtil.calculateDiff(audioDiffUtil)
        oldList = newContactList
        this.context = ctx
        diffUtilResult.dispatchUpdatesTo(this)
    }
}