package com.example.emittingtoomanyonnext

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class BuggyClassTest {

    @Test
    fun analytics_called_once() {
        // Given
        val fakeAnalytics = mock(FakeAnalytics::class.java)

        val buggyClass = BuggyClass(fakeAnalytics)

        // When

        buggyClass.putEvent(Event.SomeEvent)

        // Then

        verify(fakeAnalytics).callOnce()
    }
}