import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gp.Classes.Message
import com.example.gp.R
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val messages: MutableList<Message> = mutableListOf()
    private val currentUserEmail: String? = FirebaseAuth.getInstance().currentUser?.email

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sender, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.text

        val layoutParams = holder.messageTextView.layoutParams as ViewGroup.MarginLayoutParams

        if (message.senderEmail == currentUserEmail) {
            holder.messageTextView.setBackgroundResource(R.drawable.send)
            holder.messageTextView.gravity = Gravity.START
            holder.messageTextView.setPadding(0, 0, 50, 0)
        } else {
            holder.messageTextView.setBackgroundResource(R.drawable.resieve)
            holder.messageTextView.gravity = Gravity.END
            holder.messageTextView.setPadding(50, 0,0, 0)

        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
