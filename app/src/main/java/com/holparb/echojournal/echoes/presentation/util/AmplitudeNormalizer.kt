package com.holparb.echojournal.echoes.presentation.util

import kotlin.math.roundToInt

object AmplitudeNormalizer {

    private const val AMPLITUDE_MIN_OUTPUT_TRESHOLD = 0.1f
    private const val MIN_OUTPUT = 0.25f
    private const val MAX_OUTPUT = 1f

    fun normalize(
        sourceAmplitudes: List<Float>,
        trackWidth: Float,
        barWidth: Float,
        spacing: Float
    ): List<Float> {
        require(trackWidth >= 0) {
            "Track width must be positive"
        }
        require(trackWidth >= barWidth + spacing) {
            "Track size must be at least the size of one bar and spacing"
        }
        if(sourceAmplitudes.isEmpty()) {
            return emptyList()
        }

        val barCount = (trackWidth / (barWidth + spacing)).roundToInt()
        val resampled = resampleAmplitudes(sourceAmplitudes, barCount)

        return remapAmplitudes(resampled)
    }

    private fun remapAmplitudes(amplitudes: List<Float>): List<Float> {
        val outputRange = MAX_OUTPUT - MIN_OUTPUT
        val scaleFactor = MAX_OUTPUT - AMPLITUDE_MIN_OUTPUT_TRESHOLD

        return amplitudes.map { amplitude ->
            if(amplitude <= AMPLITUDE_MIN_OUTPUT_TRESHOLD) {
                MIN_OUTPUT
            } else {
                val amplitudeRange = amplitude - AMPLITUDE_MIN_OUTPUT_TRESHOLD
                MIN_OUTPUT + (amplitudeRange * outputRange / scaleFactor)
            }
        }
    }

    private fun resampleAmplitudes(
        sourceAmplitudes: List<Float>,
        targetSize: Int
    ): List<Float> {
        return when {
            targetSize == sourceAmplitudes.size -> sourceAmplitudes
            targetSize < sourceAmplitudes.size -> downsample(sourceAmplitudes,targetSize)
            else -> upsample(sourceAmplitudes,targetSize)
        }
    }

    private fun downsample(
        sourceAmplitudes: List<Float>,
        targetSize: Int
    ): List<Float> {
        // Size of subgroups to divide the source list and then take the max of those subgroups
        val subGroupSize = sourceAmplitudes.size.toFloat() / targetSize
        return List(targetSize) { index ->
            val start = (index * subGroupSize).toInt()
            val end = ((index + 1) * subGroupSize).toInt().coerceAtMost(sourceAmplitudes.size)

            sourceAmplitudes.subList(start, end).max()
        }
    }

    private fun upsample(
        sourceAmplitudes: List<Float>,
        targetSize: Int
    ): List<Float> {
        val result = mutableListOf<Float>()
        val step = (sourceAmplitudes.size - 1).toFloat() / (targetSize - 1)

        for(i in 0 until targetSize) {
            // How far we moved in the source list
            val pos = i * step
            // Which existing element lies exactly to the left of the current position
            val index = pos.toInt()

            // How far we are past the item as a percentage of the gap to the next one
            val fraction = pos - index

            val value = if(index + 1 < sourceAmplitudes.size) {
                (1 - fraction) * sourceAmplitudes[index] + fraction * sourceAmplitudes[index + 1]
            } else {
                // last element of the source list
                sourceAmplitudes[index]
            }
            result.add(value)
        }

        return result.toList()
    }
}