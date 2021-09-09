package com.sera.memorygame.extentions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import com.sera.memorygame.R
import com.sera.memorygame.database.entity.TriviaEntity
import com.sera.memorygame.network.mapping.TriviaNetworkMapper
import com.sera.memorygame.network.model.TriviaObjectModel
import com.sera.memorygame.ui.flag_quiz.FlagQuizFragment
import com.sera.memorygame.ui.memory.MemoryFragment
import com.sera.memorygame.ui.settings.SettingsFragment
import com.sera.memorygame.ui.size.CardsSizeChooseFragment
import com.sera.memorygame.ui.start.StartFragment
import com.sera.memorygame.ui.theme.GameThemeFragment
import com.sera.memorygame.ui.trivia.TriviaChooseFragment
import com.sera.memorygame.ui.trivia.TriviaFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 */
fun Context.themeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

/**
 *
 */
fun List<TriviaObjectModel>.toTriviaEntity(): List<TriviaEntity> {
    return ArrayList<TriviaEntity>().apply {
        this@toTriviaEntity.map {
            this.add(TriviaNetworkMapper().mapToEntity(domainModel = it))
        }
    }

}

/**
 *
 */
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
val String.toResId: Int
    get() {
        return when (this) {
            StartFragment::class.java.simpleName -> R.string.start_fragment_choose_game
            TriviaChooseFragment::class.java.simpleName -> R.string.trivia_choose_fragment_choose_trivia
            TriviaFragment::class.java.simpleName -> R.string.trivia_fragment_trivia
            FlagQuizFragment::class.java.simpleName -> R.string.flag_fragment_quize
            GameThemeFragment::class.java.simpleName -> R.string.game_theme_fragment_chosse_theme
            CardsSizeChooseFragment::class.java.simpleName -> R.string.card_size_fragment_chosse_size
            MemoryFragment::class.java.simpleName -> R.string.memory_fragment_memory
            SettingsFragment::class.java.simpleName -> R.string.settings_fragment_Settings
            else -> R.string.welcome
        }
    }

/**
 *
 */
fun String.normalizeText(): String {
    return this.replace("&quot;", "\"").replace("&#039;", "'")
}

/**
 *
 */
fun String.toAbbreviation(): String {
    return when (this.toLowerCase(Locale.getDefault())) {
        "hebrew" -> {
            "https://he."
        }
        "english" -> {
            "https://en."
        }
        else -> {
            "https://en."
        }
    }
}
