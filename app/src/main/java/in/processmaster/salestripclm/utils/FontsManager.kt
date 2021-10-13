import android.content.Context
import android.graphics.Typeface


object FontsManager {
    val OPENSANS_LIGHT = "opensans_light.ttf"
    val OPENSANS_REGULAR = "opensans_regular.ttf"
    val OPENSANS_SEMIBOLD = "opensans_semibold.ttf"
    fun getTypeface(font: String = "", context: Context): Typeface = Typeface.createFromAsset(context.assets,"font/$font")
}