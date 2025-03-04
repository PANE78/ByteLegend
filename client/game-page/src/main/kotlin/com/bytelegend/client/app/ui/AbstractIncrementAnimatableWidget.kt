/*
 * Copyright 2021 ByteLegend Technologies and the original author or authors.
 *
 * Licensed under the GNU Affero General Public License v3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://github.com/ByteLegend/ByteLegend/blob/master/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.bytelegend.client.app.ui

import com.bytelegend.app.client.api.EventListener
import com.bytelegend.app.client.ui.bootstrap.BootstrapListGroupItem
import com.bytelegend.client.app.script.effect.numberIncrementEffect
import com.bytelegend.client.utils.jsObjectBackedSetOf
import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.DIV
import kotlinx.html.classes
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.Node
import react.RBuilder
import react.RefObject
import react.State
import react.createRef
import react.dom.RDOMBuilder
import react.dom.div
import react.setState

data class NumberIncrementEvent(
    val inc: Int,
    val newValue: Int
)

/**
 * A special widget which can show a "+X" animation when updated.
 */
abstract class AbstractIncrementAnimatableWidget<P : GameProps, S : State>(
    private val iconClassName: String
) : GameUIComponent<P, S>() {
    abstract val eventName: String
    private var div: RefObject<HTMLDivElement> = createRef()

    private val incrementEventListener: EventListener<NumberIncrementEvent> = this::onIncrement

    override fun RBuilder.render() {
        BootstrapListGroupItem {
            div {
                renderDiv()
                ref = div
            }
        }
    }

    abstract fun RDOMBuilder<DIV>.renderDiv()
    abstract fun onIncrementNewValue(event: NumberIncrementEvent)
    protected fun RBuilder.renderIcon() {
        div {
            attrs.classes = jsObjectBackedSetOf(iconClassName, "inline-icon")
        }
    }

    private fun getIncrementAnimationDiv(event: NumberIncrementEvent): Node {
        val div = document.createElement("div")
        div.appendChild(document.createTextNode("+${event.inc}"))
        div.appendChild(
            document.createElement("div").unsafeCast<HTMLDivElement>().apply {
                className = "$iconClassName inline-icon"
            }
        )
        return div
    }

    private fun onIncrement(event: NumberIncrementEvent) {
        GlobalScope.launch {
            numberIncrementEffect(
                getIncrementAnimationDiv(event),
                div.current!!.getBoundingClientRect().x.toInt(),
                div.current!!.getBoundingClientRect().y.toInt(),
                div.current!!.getBoundingClientRect().width.toInt(),
                div.current!!.getBoundingClientRect().height.toInt(),
            )
        }
        onIncrementNewValue(event)
        setState { }
    }

    override fun componentDidMount() {
        super.componentDidMount()
        props.game.eventBus.on(eventName, incrementEventListener)
    }

    override fun componentWillUnmount() {
        super.componentWillUnmount()
        props.game.eventBus.remove(eventName, incrementEventListener)
    }
}
