package com.kapirti.dhikr

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kapirti.dhikr.ui.theme.DhikrTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DhikrTheme {
                ScoreScreen()
            }
        }
    }
}

@Composable
fun ScoreScreen(){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreScore(context)
    Column (
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val userScore = dataStore.getScore.collectAsState(initial = "dele")
        Text(text=userScore.value!!.toString())
        Button(
            onClick = {
                scope.launch {
                    dataStore.increaseScore()
                }
            }
        ) {
            Text(text="change")
        }
        Button(
            onClick = {
                scope.launch {
                    dataStore.deleteScore()
                }
            }
        ) {
            Text(text="0")
        }
    }

}
class StoreScore(private val context: Context){

    companion object {
        private val Context.dataStoree: DataStore<Preferences> by preferencesDataStore("userScore")
        val USER_SCORE = intPreferencesKey("user_email")
    }

    val getScore: Flow<Int?> = context.dataStoree.data
        .map { preferences->
            preferences[USER_SCORE] ?: 8
        }

    suspend fun increaseScore(){
        context.dataStoree.edit { preferences->
            val currentCounterValue = preferences[USER_SCORE] ?: 0
            preferences[USER_SCORE] = currentCounterValue + 1
        }
    }

    suspend fun deleteScore(){
        context.dataStoree.edit { preferences->
            val deleteScoreZero = 0
            preferences[USER_SCORE] = deleteScoreZero
        }
    }

}