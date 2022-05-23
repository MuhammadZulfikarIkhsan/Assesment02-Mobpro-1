package org.d3if2049.tolist.ui.addedittask

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.d3if2049.tolist.R
import org.d3if2049.tolist.databinding.FragmentAddEditTaskBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.d3if2049.util.exhaustive

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)

        binding.apply {
            editTextNamaTugas.setText(viewModel.taskName)
            checkboxPenting.isChecked = viewModel.taskImportance
            checkboxPenting.jumpDrawablesToCurrentState()
            textViewTanggalDibuat.isVisible = viewModel.task != null
            textViewTanggalDibuat.text = "Dibuat: ${viewModel.task?.createdDateFormatted}"

            editTextNamaTugas.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            checkboxPenting.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            fabSaveTugas.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.editTextNamaTugas.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }
    }
}