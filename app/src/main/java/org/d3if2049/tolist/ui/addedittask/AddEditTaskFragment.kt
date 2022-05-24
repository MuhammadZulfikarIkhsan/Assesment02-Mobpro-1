package org.d3if2049.tolist.ui.addedittask

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
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
import kotlinx.android.synthetic.main.fragment_add_edit_task.*
import kotlinx.coroutines.flow.collect
import org.d3if2049.util.exhaustive

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    private lateinit var binding: FragmentAddEditTaskBinding

    // text to share, from edit text
    private var textToShare = ""

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun shareText() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"

        intent.putExtra(Intent.EXTRA_SUBJECT, "Subjek Di sini")
        intent.putExtra(Intent.EXTRA_TEXT, textToShare)
        startActivity(Intent.createChooser(intent, "Bagikan via"))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddEditTaskBinding.inflate(layoutInflater)

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

            // Text to Copy Clipboard
            copyButton.setOnClickListener {
                val clipboard =
                    requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData =
                    ClipData.newPlainText("Edit text", editTextNamaTugas.text.toString())
                clipboard.setPrimaryClip(clip)

                if (TextUtils.isEmpty(edit_text_nama_tugas.text.toString())) {
                    Toast.makeText(requireActivity(), "Masukkan tugas!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(), "Tugas disalin", Toast.LENGTH_SHORT).show()
                }
            }

            // Text to Share
            shareButton.setOnClickListener {
                textToShare = binding.editTextNamaTugas.text.toString().trim()
                if (textToShare.isEmpty()) {
                    showToast("Masukkan tugas!")
                } else {
                    shareText()
                }
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