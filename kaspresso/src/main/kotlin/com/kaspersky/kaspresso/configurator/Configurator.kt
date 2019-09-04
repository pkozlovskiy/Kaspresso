package com.kaspersky.kaspresso.configurator

import androidx.test.espresso.Espresso
import androidx.test.espresso.FailureHandler
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.agoda.kakao.Kakao
import com.kaspersky.kaspresso.device.Device
import com.kaspersky.kaspresso.device.accessibility.Accessibility
import com.kaspersky.kaspresso.device.accessibility.AccessibilityImpl
import com.kaspersky.kaspresso.device.activities.Activities
import com.kaspersky.kaspresso.device.activities.ActivitiesImpl
import com.kaspersky.kaspresso.device.apps.Apps
import com.kaspersky.kaspresso.device.apps.AppsImpl
import com.kaspersky.kaspresso.device.exploit.Exploit
import com.kaspersky.kaspresso.device.exploit.ExploitImpl
import com.kaspersky.kaspresso.device.files.Files
import com.kaspersky.kaspresso.device.files.FilesImpl
import com.kaspersky.kaspresso.device.internet.Internet
import com.kaspersky.kaspresso.device.internet.InternetImpl
import com.kaspersky.kaspresso.device.keyboard.Keyboard
import com.kaspersky.kaspresso.device.keyboard.KeyboardImpl
import com.kaspersky.kaspresso.device.location.Location
import com.kaspersky.kaspresso.device.location.LocationImpl
import com.kaspersky.kaspresso.device.permissions.Permissions
import com.kaspersky.kaspresso.device.permissions.PermissionsImpl
import com.kaspersky.kaspresso.device.phone.Phone
import com.kaspersky.kaspresso.device.phone.PhoneImpl
import com.kaspersky.kaspresso.device.screenshots.Screenshots
import com.kaspersky.kaspresso.device.screenshots.ScreenshotsImpl
import com.kaspersky.kaspresso.flakysafety.FlakySafetyParams
import com.kaspersky.kaspresso.interceptors.interaction.impl.DataInteractionInterceptor
import com.kaspersky.kaspresso.interceptors.interaction.impl.ViewInteractionInterceptor
import com.kaspersky.kaspresso.interceptors.interaction.impl.WebInteractionInterceptor
import com.kaspersky.kaspresso.interceptors.interactors.DataInteractor
import com.kaspersky.kaspresso.interceptors.interactors.ViewInteractor
import com.kaspersky.kaspresso.interceptors.interactors.WebInteractor
import com.kaspersky.kaspresso.interceptors.interactors.impl.autoscroll.AutoscrollViewInteractor
import com.kaspersky.kaspresso.interceptors.interactors.impl.autoscroll.AutoscrollWebInteractor
import com.kaspersky.kaspresso.interceptors.interactors.impl.flakysafety.FlakySafeDataInteractor
import com.kaspersky.kaspresso.interceptors.interactors.impl.flakysafety.FlakySafeViewInteractor
import com.kaspersky.kaspresso.interceptors.interactors.impl.flakysafety.FlakySafeWebInteractor
import com.kaspersky.kaspresso.interceptors.testcase.StepInterceptor
import com.kaspersky.kaspresso.interceptors.testcase.TestRunInterceptor
import com.kaspersky.kaspresso.interceptors.testcase.impl.logging.LoggingStepInterceptor
import com.kaspersky.kaspresso.interceptors.testcase.impl.logging.TestRunLoggerInterceptor
import com.kaspersky.kaspresso.interceptors.testcase.impl.report.BuildStepReportInterceptor
import com.kaspersky.kaspresso.interceptors.testcase.impl.screenshot.ScreenshotStepInterceptor
import com.kaspersky.kaspresso.interceptors.testcase.impl.screenshot.TestRunnerScreenshotInterceptor
import com.kaspersky.kaspresso.interceptors.view.AtomInterceptor
import com.kaspersky.kaspresso.interceptors.view.ViewActionInterceptor
import com.kaspersky.kaspresso.interceptors.view.ViewAssertionInterceptor
import com.kaspersky.kaspresso.interceptors.view.WebAssertionInterceptor
import com.kaspersky.kaspresso.interceptors.view.impl.logging.LoggingAtomInterceptor
import com.kaspersky.kaspresso.failure.LoggingFailureHandler
import com.kaspersky.kaspresso.interceptors.interactors.impl.failure.FailureLoggingDataInteractor
import com.kaspersky.kaspresso.interceptors.interactors.impl.failure.FailureLoggingViewInteractor
import com.kaspersky.kaspresso.interceptors.interactors.impl.failure.FailureLoggingWebInteractor
import com.kaspersky.kaspresso.interceptors.view.impl.logging.LoggingViewActionInterceptor
import com.kaspersky.kaspresso.interceptors.view.impl.logging.LoggingViewAssertionInterceptor
import com.kaspersky.kaspresso.interceptors.view.impl.logging.LoggingWebAssertionInterceptor
import com.kaspersky.kaspresso.logger.UiTestLogger
import com.kaspersky.kaspresso.logger.UiTestLoggerImpl
import com.kaspersky.kaspresso.report.impl.AllureReportWriter
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

/**
 * A class that keeps all settings.
 *
 * @param apps Holds an implementation of [Apps] interface. If it was not specified in [Configurator.Builder], the
 * default implementation is used.
 * @param activities Holds an implementation of [Activities] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param files Holds an implementation of [Files] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param internet Holds an implementation of [Internet] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param phone Holds an implementation of [Phone] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param location Holds an implementation of [Location] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param keyboard Holds an implementation of [Keyboard] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param screenshots Holds an implementation of [Screenshots] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param accessibility Holds an implementation of [Accessibility] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param permissions Holds an implementation of [Permissions] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param exploit Holds an implementation of [Exploit] interface. If it was not specified in [Configurator.Builder],
 * the default implementation is used.
 * @param viewActionInterceptors Interceptors that are called by [com.kaspersky.kaspresso.proxy.ViewActionProxy]
 * before actually [androidx.test.espresso.ViewAction.perform] call.
 * @param viewAssertionInterceptors Interceptors that are called by [com.kaspersky.kaspresso.proxy.ViewAssertionProxy]
 * before actually [androidx.test.espresso.ViewAssertion.check] call.
 * @param atomInterceptors Interceptors that are called by [com.kaspersky.kaspresso.proxy.AtomProxy]
 * before actually [androidx.test.espresso.web.model.Atom.transform] call.
 * @param webAssertionInterceptors Interceptors that are called by [androidx.test.espresso.web.assertion.WebAssertionProxy]
 * before actually [androidx.test.espresso.web.assertion.WebAssertion.checkResult] call.
 * @param executingInterceptor An interceptor that actually manages the execution of actions or assertions. For example,
 * [FlakySafeExecutionInterceptor] performs multiple attempting to interact an action or assertion.
 * @param stepInterceptors An interceptors set that actually manages the execution of steps [TestContext.step].
 * Interceptors work using decorator pattern. First interceptor wraps others.
 * @param testRunInterceptors An interceptors set that actually manages the execution of test sections
 * [com.kaspersky.kaspresso.testcases.models.TestInfo]. Interceptor works using decorator pattern. First interceptor wraps others.
 * @param testLogger Holds an implementation of [UiTestLogger] interface for external usage.
 */
data class Configurator(
    internal val libLogger: UiTestLogger,
    internal val testLogger: UiTestLogger,
    internal val device: Device,
    internal val flakySafetyParams: FlakySafetyParams,
    internal val viewActionInterceptors: List<ViewActionInterceptor>,
    internal val viewAssertionInterceptors: List<ViewAssertionInterceptor>,
    internal val atomInterceptors: List<AtomInterceptor>,
    internal val webAssertionInterceptors: List<WebAssertionInterceptor>,
    internal val viewInteractors: List<ViewInteractor>,
    internal val dataInteractors: List<DataInteractor>,
    internal val webInteractors: List<WebInteractor>,
    internal val stepInterceptors: List<StepInterceptor>,
    internal val testRunInterceptors: List<TestRunInterceptor>
) {
    private companion object {
        private const val DEFAULT_LIB_LOGGER_TAG: String = "KASPRESSO"
        private const val DEFAULT_TEST_LOGGER_TAG: String = "KASPRESSO_TEST"
    }

    /**
     * A class for [Configurator] initialization. The right way to change [Configurator] settings is to use [Builder].
     */
    class Builder {

        companion object {

            /**
             * Puts the default settings pack to [Builder].
             * Please be aware if you add some settings after [default] method. You can catch inconsistent state of the
             * [Builder]. For example if you change [libLogger] after [default] method than all interceptors will work with
             * old [libLogger].
             *
             * @return an existing instance of [Builder].
             */
            fun default(): Builder {
                return Builder().apply {

                    viewActionInterceptors = mutableListOf(
                        LoggingViewActionInterceptor(libLogger)
                    )

                    viewAssertionInterceptors = mutableListOf(
                        LoggingViewAssertionInterceptor(libLogger)
                    )

                    atomInterceptors = mutableListOf(
                        LoggingAtomInterceptor(libLogger)
                    )

                    webAssertionInterceptors = mutableListOf(
                        LoggingWebAssertionInterceptor(libLogger)
                    )

                    viewInteractors = mutableListOf(
                        AutoscrollViewInteractor(libLogger),
                        FlakySafeViewInteractor(flakySafetyParams, libLogger),
                        FailureLoggingViewInteractor(libLogger)
                    )

                    dataInteractors = mutableListOf(
                        FlakySafeDataInteractor(flakySafetyParams, libLogger),
                        FailureLoggingDataInteractor(libLogger)
                    )

                    webInteractors = mutableListOf(
                        AutoscrollWebInteractor(libLogger),
                        FlakySafeWebInteractor(flakySafetyParams, libLogger),
                        FailureLoggingWebInteractor(libLogger)
                    )

                    stepInterceptors = mutableListOf(
                        LoggingStepInterceptor(libLogger),
                        ScreenshotStepInterceptor(screenshots)
                    )

                    testRunInterceptors = mutableListOf(
                        TestRunLoggerInterceptor(libLogger),
                        TestRunnerScreenshotInterceptor(screenshots),
                        BuildStepReportInterceptor(AllureReportWriter(libLogger))
                    )

                    failureHandler = LoggingFailureHandler(libLogger)
                }
            }
        }

        var libLogger: UiTestLogger = UiTestLoggerImpl(DEFAULT_LIB_LOGGER_TAG)
        var testLogger: UiTestLogger = UiTestLoggerImpl(DEFAULT_TEST_LOGGER_TAG)

        var apps: Apps =
            AppsImpl(
                libLogger,
                InstrumentationRegistry.getInstrumentation().context,
                UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            )
        var activities: Activities = ActivitiesImpl(libLogger)
        var files: Files = FilesImpl()
        var internet: Internet = InternetImpl(InstrumentationRegistry.getInstrumentation().targetContext)
        var phone: Phone = PhoneImpl()
        var location: Location = LocationImpl()
        var keyboard: Keyboard = KeyboardImpl()
        var screenshots: Screenshots = ScreenshotsImpl(libLogger, activities)
        var accessibility: Accessibility = AccessibilityImpl()
        var permissions: Permissions =
            PermissionsImpl(libLogger, UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()))
        var exploit: Exploit =
            ExploitImpl(activities, UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()))

        var flakySafetyParams: FlakySafetyParams = FlakySafetyParams()

        var viewActionInterceptors: MutableList<ViewActionInterceptor> = mutableListOf()
        var viewAssertionInterceptors: MutableList<ViewAssertionInterceptor> = mutableListOf()
        var atomInterceptors: MutableList<AtomInterceptor> = mutableListOf()
        var webAssertionInterceptors: MutableList<WebAssertionInterceptor> = mutableListOf()

        var viewInteractors: MutableList<ViewInteractor> = mutableListOf()
        var dataInteractors: MutableList<DataInteractor> = mutableListOf()
        var webInteractors: MutableList<WebInteractor> = mutableListOf()

        var stepInterceptors: MutableList<StepInterceptor> = mutableListOf()
        var testRunInterceptors: MutableList<TestRunInterceptor> = mutableListOf()

        /**
         * An interceptor that is called on failures. It's [FailureHandler.intercept] method is being
         * provide as the default [androidx.test.espresso.FailureHandler].
         */
        var failureHandler: FailureHandler = LoggingFailureHandler(libLogger)

        /**
         * Terminating method to build built [Configurator] settings. Can be called only inside the framework
         * package. Actually called when the base [com.kaspersky.kaspresso.testcases.api.BaseTestCase] class is
         * constructed.
         */
        internal fun build(): Configurator {

            val configurator = Configurator(
                libLogger = libLogger,
                testLogger = testLogger,

                device = Device(
                    apps = apps,
                    activities = activities,
                    files = files,
                    internet = internet,
                    phone = phone,
                    location = location,
                    keyboard = keyboard,
                    screenshots = screenshots,
                    accessibility = accessibility,
                    permissions = permissions,
                    exploit = exploit
                ),

                flakySafetyParams = flakySafetyParams,

                viewActionInterceptors = viewActionInterceptors,
                viewAssertionInterceptors = viewAssertionInterceptors,
                atomInterceptors = atomInterceptors,
                webAssertionInterceptors = webAssertionInterceptors,

                viewInteractors = viewInteractors,
                dataInteractors = dataInteractors,
                webInteractors = webInteractors,

                stepInterceptors = stepInterceptors,
                testRunInterceptors = testRunInterceptors
            )

            val viewInteractionInterceptor = ViewInteractionInterceptor(configurator)
            val dataInteractionInterceptor = DataInteractionInterceptor(configurator)
            val webInteractionInterceptor = WebInteractionInterceptor(configurator)

            Kakao.intercept {
                onViewInteraction {
                    onCheck(isOverride = true, interceptor = viewInteractionInterceptor::interceptCheck)
                    onPerform(isOverride = true, interceptor = viewInteractionInterceptor::interceptPerform)
                }
                onDataInteraction {
                    onCheck(isOverride = true, interceptor = dataInteractionInterceptor::interceptCheck)
                }
                onWebInteraction {
                    onCheck(isOverride = true, interceptor = webInteractionInterceptor::interceptCheck)
                    onPerform(isOverride = true, interceptor = webInteractionInterceptor::interceptPerform)
                }
            }

            Espresso.setFailureHandler(failureHandler)

            return configurator
        }
    }

    internal fun reset(): Unit = Kakao.reset()
}