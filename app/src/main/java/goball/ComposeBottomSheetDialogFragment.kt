package goball

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import goball.uz.ui.theme.GoBallTheme

class ComposeBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(),theme).apply {
            setOnShowListener {dialog->
                val bottomSheet=(dialog as BottomSheetDialog).findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet
                )
                bottomSheet?.background=null
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                GoBallTheme {
                    BottomSheetContent()
                }
            }
        }

    }
}
@Composable
fun BottomSheetContent() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(
            color = Color.Green,
            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)
        )
        .padding(16.dp)) {
        Text(text = "Top stadiums")
        // Add your bottom sheet content here, like a list of stadiums
        // Example:
        repeat(10) { index ->
            Text(text = "Stadium #$index")
        }
    }
}