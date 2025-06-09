package com.holparb.echojournal.core.database.echo

import androidx.room.TypeConverter
import com.holparb.echojournal.echoes.domain.data_source.Mood

class MoodTypeConverter {

    @TypeConverter
    fun fromMoodUi(mood: Mood): String {
        return mood.name
    }

    @TypeConverter
    fun toMoodUi(moodName: String): Mood {
        return Mood.valueOf(moodName)
    }
}