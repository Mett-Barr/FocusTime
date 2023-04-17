package com.example.focustime.ui.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.example.focustime.MainActivity
import java.util.*

val PREF = stringPreferencesKey("time")
val PARAM = ActionParameters.Key<String>("time")


class TapGlanceWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*>?
        get() = super.stateDefinition

//    companion object {
//    }

    @Composable
    override fun Content() {
        val last = currentState(key = PREF)
        Box(
            modifier = GlanceModifier
                .clickable(onClick = actionRunCallback<TapAction>(
                    actionParametersOf(
                        PARAM to (last ?: "0")
                    )
                ))
                .fillMaxSize()
        ) {}
    }


}

class TapAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val now = Calendar.getInstance().timeInMillis
        val str = parameters[PARAM]
        val last = str?.toLong() ?: 0L
        val duration = now - last
//            stringSetPreferencesKey("time")
//        preferencesOf(PREF to now.toString())
        updateAppWidgetState(
            context,
            glanceId,
        ) {
            it[stringPreferencesKey("time")] = now.toString()
        }
        TapGlanceWidget().update(context, glanceId)

        Log.d("!!!", "$now   $last   $duration     $str")

        if (duration <= 500) {
            context.startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

        }
    }
}

//class Start : ActionCallback {
//    override suspend fun onAction(
//        context: Context,
//        glanceId: GlanceId,
//        parameters: ActionParameters
//    ) {
//        context.startActivity(Intent(context, MainActivity::class.java))
//    }
//
//}