package co.com.gulupagames.spacecraft.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import co.com.gulupagames.spacecraft.R
import co.com.gulupagames.spacecraft.dialogfragments.base.BaseDialogFragment

class BackToMainMenuDialogFragment : BaseDialogFragment() {
    @Deprecated("Deprecated in Java")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val baseGameActivity = gameActivity
        val view =
            inflater.inflate(R.layout.back_to_main_menu_confirmation_dialog, container, false)
        val accept = view.findViewById<View>(R.id.accept_button) as Button
        accept.text = getString(R.string.accept_text)
        accept.setOnClickListener {
            baseGameActivity.isPause = false
            baseGameActivity.backToMainMenu()
            dismiss()
        }
        val cancel = view.findViewById<View>(R.id.cancel_button) as Button
        cancel.text = getString(R.string.cancel_text)
        cancel.setOnClickListener {
            baseGameActivity.isPause = false
            baseGameActivity.resumeGame()
            dismiss()
        }
        return view
    }

    companion object {
        fun newInstance(): BackToMainMenuDialogFragment {
            return BackToMainMenuDialogFragment()
        }
    }
}