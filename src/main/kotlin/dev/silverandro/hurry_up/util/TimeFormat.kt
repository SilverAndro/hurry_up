package dev.silverandro.hurry_up.util

import kotlin.math.min
import kotlin.random.Random

val SECOND = 20
val MINUTE = SECOND * 60

fun ticksToTime(ticks: Int, randomize: Boolean = true): String {
    return if (ticks <= 0) {
        "0:00s"
    } else if (ticks < MINUTE) {
        val seconds = ticks / SECOND
        val remaining = ticks % SECOND
        // this looks really same-y as it counts down otherwise, so we randomize it slightly to keep it looking like a blur and not
        // like its toggling between 5 and 0, and cap it
        if (randomize) {
            "$seconds:${min(remaining * 5 + Random.nextInt(0, 4), 99).toString().padStart(2, '0')}s"
        } else {
            "$seconds:${remaining.toString().padStart(2, '0')}s"
        }
    } else {
        val minutes = ticks / MINUTE
        val remaining = ticks % MINUTE
        val seconds = remaining / SECOND
        "$minutes:${seconds.toString().padStart(2, '0')}m"
    }
}
