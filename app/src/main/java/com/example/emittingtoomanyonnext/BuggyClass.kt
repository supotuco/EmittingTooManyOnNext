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
            .buggyMethod()
            .share()
            .subscribe()
    }

    fun putEvent(event: Event) {
        emitter.onNext(event)
    }

    private fun Observable<Event>.buggyMethod(): Observable<LocalResult> {
        return Observable.merge(
            this.ofType(Event.SomeEvent.javaClass).map { LocalResult.SomeResult },
            this.ofType(Event.SomeOtherEvent.javaClass).map { LocalResult.SomeResult }
        )
            .cast(LocalResult::class.java)
            .distinctUntilChanged()
    }
}

sealed class Event {
    object SomeEvent : Event()
    object SomeOtherEvent: Event()
}

sealed class LocalResult {
    object SomeResult: LocalResult()
    object SomeOtherResult: LocalResult()
}