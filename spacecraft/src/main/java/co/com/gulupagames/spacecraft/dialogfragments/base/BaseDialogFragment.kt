package co.com.gulupagames.spacecraft.dialogfragments.base

import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import co.com.gulupagames.gamecore.game.gameactivity.BaseGameActivity

open class BaseDialogFragment : DialogFragment() {
    @Deprecated("Deprecated in Java")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NO_TITLE, 0)
    }

    protected val gameActivity: BaseGameActivity
        get() = activity as BaseGameActivity

    @Deprecated("Deprecated in Java")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}