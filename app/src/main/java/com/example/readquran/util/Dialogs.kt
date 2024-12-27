import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.readquran.R
import com.example.readquran.util.ArabicTranslator


object Dialogs {
    fun showAlertDialog(
        context: Context,
        message: String,
        title: String,
        positiveButton: String,
        negativeButton: String,
        onConfirm: () -> Unit,
        onCancel: () -> Unit,
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setCancelable(true)
        builder.setTitle(title)

        builder.setPositiveButton(positiveButton) { dialog, _ ->
            onConfirm()
            dialog.dismiss()
        }
        builder.setNegativeButton(negativeButton) { dialog, _ ->
            onCancel()
            dialog.dismiss()

        }
        val dialog = builder.create()

        dialog.show()

    }

    fun showToastLong(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    fun showToastShort(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun showTafserDialog(context: Context, message:String,suraName:String,num:Int) {
        val builder = Dialog(context)
        builder.setContentView(R.layout.custom_tafseer_dialog)
        val dialogMessage = builder.findViewById<TextView>(R.id.textOfTafser)
        val ayahNum = builder.findViewById<TextView>(R.id.numberOfAyahMofasser)
        val suraNameText = builder.findViewById<TextView>(R.id.dialog_surahName)
        suraNameText.text = suraName
        dialogMessage.text = message
        ayahNum.text = ArabicTranslator.toArabicNumerals(num)
        builder.setCancelable(true)
        builder.create()
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent);
        builder.show()
    }





}