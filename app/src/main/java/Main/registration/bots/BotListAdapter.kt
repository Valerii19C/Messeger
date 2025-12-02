package Main.registration.bots

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.R

class BotListAdapter(
    private val botList: List<Bot>,
    private val onBotClick: (Bot) -> Unit
) : RecyclerView.Adapter<BotListAdapter.BotViewHolder>() {

    inner class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val botNameTextView: TextView = itemView.findViewById(R.id.bot_name)

        fun bind(bot: Bot) {
            botNameTextView.text = bot.name
            itemView.setOnClickListener { onBotClick(bot) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bot_list_item, parent, false)
        return BotViewHolder(view)
    }

    override fun onBindViewHolder(holder: BotViewHolder, position: Int) {
        holder.bind(botList[position])
    }

    override fun getItemCount(): Int = botList.size
}
