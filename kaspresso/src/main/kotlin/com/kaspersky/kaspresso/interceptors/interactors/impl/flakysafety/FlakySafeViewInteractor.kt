package com.kaspersky.kaspresso.interceptors.interactors.impl.flakysafety

import androidx.test.espresso.ViewInteraction
import com.kaspersky.kaspresso.flakysafety.FlakySafetyParams
import com.kaspersky.kaspresso.flakysafety.FlakySafetyProvider
import com.kaspersky.kaspresso.interceptors.interactors.ViewInteractor
import com.kaspersky.kaspresso.logger.UiTestLogger

/**
 * An implementation of [Interactor] that performs multiple attempts to interact an action or an assertion to
 * provide flaky safety.
 */
class FlakySafeViewInteractor(
    override val params: FlakySafetyParams,
    override val logger: UiTestLogger
) : ViewInteractor, FlakySafetyProvider {

    /**
     * Performs multiple attempts to interact an action or an assertion.
     *
     * @param execution a function-wrapper of an action or an assertion to be invoked.
     */
    override fun <R> interact(interaction: ViewInteraction, action: () -> R): R = flakySafely(action = action)
}