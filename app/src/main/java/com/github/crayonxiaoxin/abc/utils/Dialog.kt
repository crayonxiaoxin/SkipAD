package com.github.crayonxiaoxin.abc.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.github.crayonxiaoxin.abc.R
import com.github.crayonxiaoxin.abc.databinding.DialogInputBinding

fun Context?.showKeywordDialog(
    hint: String = "",
    value: String? = "",
    callback: (String) -> Unit = {},
) {
    if (this == null) return
    val dialogBinding = DialogInputBinding.inflate(LayoutInflater.from(this))
    dialogBinding.tilText.hint = hint
    dialogBinding.etText.setText(value ?: "")
    AlertDialog.Builder(this)
        .setView(dialogBinding.root)
        .setNegativeButton(
            R.string.cancel
        ) { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
            val text = dialogBinding.etText.text?.toString() ?: ""
            callback.invoke(text)
        }
        .show()
}


fun Context?.showDeleteDialog(title: String? = null, callback: () -> Unit = {}) {
    if (this == null) return
    AlertDialog.Builder(this)
        .setTitle(title ?: getString(R.string.delete_or_not))
        .setNegativeButton(
            R.string.cancel
        ) { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
            callback.invoke()
        }
        .show()
}