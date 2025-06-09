package com.holparb.echojournal.core.database.echo

import androidx.room.TypeConverter
import com.holparb.echojournal.echoes.presentation.models.MoodUi

class MoodUiTypeConverter {

    @TypeConverter
    fun fromMoodUi(moodUi: MoodUi): String {
        return moodUi.name
    }

    @TypeConverter
    fun toMoodUi(moodName: String): MoodUi {
        return MoodUi.valueOf(moodName)
    }
}