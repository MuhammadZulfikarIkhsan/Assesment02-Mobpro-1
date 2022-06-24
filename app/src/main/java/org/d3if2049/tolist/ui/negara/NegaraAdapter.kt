package org.d3if2049.tolist.ui.negara

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if2049.tolist.model.Negara
import org.d3if2049.tolist.R
import org.d3if2049.tolist.databinding.ListItemBinding
import org.d3if2049.tolist.network.NegaraApi

class NegaraAdapter : RecyclerView.Adapter<NegaraAdapter.ViewHolder>() {

    private val data = mutableListOf<Negara>()

    fun updateData(newData: List<Negara>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(negara: Negara) = with(binding) {
            namaTextView.text = negara.nama
            mataUangTextView.text = negara.mataUang
            kodeNegaraTextView.text = negara.kodeNegara

            Glide.with(imageView.context)
                .load(NegaraApi.getNegaraUrl(negara.imageId))
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imageView)

            root.setOnClickListener {
                val message = root.context.getString(R.string.message, negara.nama)
                Toast.makeText(root.context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}