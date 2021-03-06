package ru.pavel.tomato.wear.timer

import android.os.CountDownTimer

sealed class TimerState {
    data class Active(val timer: CountDownTimer) : TimerState()
    data class Paused(val timeLeft: Long): TimerState()
    object Idle : TimerState()
}