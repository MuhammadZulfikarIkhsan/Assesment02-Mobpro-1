package org.d3if2049.tolist.ui.negara

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3if2049.tolist.R
import org.d3if2049.tolist.databinding.FragmentNegaraBinding
import org.d3if2049.tolist.network.ApiStatus

class NegaraFragment : Fragment(R.layout.fragment_negara) {

    private val viewModel: NegaraViewModel by lazy {
        ViewModelProvider(this).get(NegaraViewModel::class.java)
    }

    private lateinit var binding: FragmentNegaraBinding
    private lateinit var myAdapter: NegaraAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNegaraBinding.inflate(layoutInflater, container, false)
        myAdapter = NegaraAdapter()
        with(binding.recyclerView) {
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    RecyclerView.VERTICAL
                )
            )
            adapter = myAdapter
            setHasFixedSize(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getData().observe(viewLifecycleOwner) {
            myAdapter.updateData(it)
        }
        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
        }
    }

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
            }
            ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkErrorImg.visibility = View.VISIBLE
                binding.networkError.visibility = View.VISIBLE
                binding.networkErrorDescription.visibility = View.VISIBLE
            }
        }
    }
}