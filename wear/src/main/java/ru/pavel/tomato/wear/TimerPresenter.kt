package ru.pavel.tomato.wear

import android.util.Log
import ru.pavel.tomato.wear.timer.TimerListener

interface TimerPresenter {
    fun onStart(timeInSeconds: Int)
    fun onPauseTimer()
    fun onResumeTimer()
    fun onCancelTimer()
    fun onEveryTimerSecond(secondsLeft: Int)
    fun onDestroy()
    fun onTimerFinish()
}

class TimerPresenterImpl(private val timerView: TimerView) : TimerPresenter{

    private val timerInteractor = TimerInteractor.create()
    private val timerListener = BasicTimerListener(this)

    override fun onStart(timeInSeconds: Int) {
        timerInteractor.startOrJoin(timeInSeconds, timerListener)
        timerView.setPauseButtonVisibility(isVisible = true)
        timerView.setResumeButtonVisibility(isVisible = false)
    }

    override fun onCancelTimer() {
        timerInteractor.cancel()
        timerView.goToChooseTimeView()
    }

    override fun onPauseTimer() {
        Log.d("D-TIMER", "presenter: pause")
        setButtonsVisibility(isPaused = true)
        timerInteractor.pause()
    }

    override fun onResumeTimer() {
        Log.d("D-TIMER", "presenter: resume")
        setButtonsVisibility(isPaused = false)
        timerInteractor.resume()
    }

    override fun onDestroy() {
        timerInteractor.stopListening(timerListener)
    }

    override fun onEveryTimerSecond(secondsLeft: Int) {
        timerView.setTimerText(secondsLeft.toString())
    }

    override fun onTimerFinish() {
        timerView.signalOnComplete()
        timerView.goToChooseTimeView()
    }

    private fun setButtonsVisibility(isPaused: Boolean) {
        timerView.setPauseButtonVisibility(!isPaused)
        timerView.setResumeButtonVisibility(isPaused)
    }

    private class BasicTimerListener(private val timerPresenter: TimerPresenter)
        : TimerListener {

        override fun onEverySecond(secondsLeft: Int) {
            timerPresenter.onEveryTimerSecond(secondsLeft)
        }

        override fun onFinish() {
            timerPresenter.onTimerFinish()
        }

    }
}