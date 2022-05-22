package org.d3if2049.tolist.ui.addedittask

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.d3if2049.tolist.R
import org.d3if2049.tolist.databinding.FragmentAddEditTaskBinding

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
        }
    }
}