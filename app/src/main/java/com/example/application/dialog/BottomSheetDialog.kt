package com.example.application.dialog

import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.application.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.setUpBottomSheetDialog(
    onSendbuttonClick :(String)->Unit
){
    val dialog=BottomSheetDialog(requireContext())
    val view=layoutInflater.inflate(R.layout.reset_password_dialog,null)
    dialog.behavior.state=BottomSheetBehavior.STATE_EXPANDED
    dialog.setContentView(view)
    dialog.show()

    val email=view.findViewById<EditText>(R.id.edt_reset_password)
    val send_button=view.findViewById<Button>(R.id.btn_send)
    val cancel_button=view.findViewById<Button>(R.id.btn_cancel)

    send_button.setOnClickListener{
          onSendbuttonClick(email.text.toString().trim())
        dialog.dismiss()
    }

    cancel_button.setOnClickListener {
        dialog.dismiss()
    }
}