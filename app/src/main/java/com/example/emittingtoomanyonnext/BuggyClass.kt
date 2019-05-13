package com.example.emittingtoomanyonnext

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface FakeAnalytics {
    fun callOnce()
}

class BuggyClass(private val analytics: FakeAnalytics) {

    private val emitter = PublishSubject.create<Event>()

    init {
        emitter.doOnNext { analytics.callOnce() }
            .map { event ->
                when (event) {
                    is Event.SomeEvent, is Event.SomeOtherEvent -> LocalResult.SomeResult
                }
            }
            .share()
            .subscribe()
    }

    fun putEvent(event: Event) {
        emitter.onNext(event)
    }
}

sealed class Event {
    object SomeEvent : Event()
    object SomeOtherEvent : Event()
}

sealed class LocalResult {
    object SomeResult : LocalResult()
    object SomeOtherResult : LocalResult()
}