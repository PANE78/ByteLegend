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
package com.bytelegend.client.app.ui.mission

import com.bytelegend.client.utils.jsObjectBackedSetOf
import com.bytelegend.client.app.ui.icon
import kotlinx.html.classes
import react.RBuilder
import react.RComponent
import react.Props
import react.State
import react.dom.span

const val STAR_PNG_BASE64 =
    "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAAXNSR0IB2cksfwAAAAlwSFlzAAALEwAACxMBAJqcGAAACPdJREFUeJy9V2lsXOUVPd/b5s0+41lsx3viNU6ceAkpEJQoQGlLA9QhCAJV1YJooaVSq1JVqvqDVkioqKAuqItEVSi0KU5SQhsKBKqWLWAnbuwkdmI7duyM49k8+/7mva93Aj9QMcIgpU960sy85Z57z7nn3pHwKY+9fWBlQbZzbvCh43r6075H+jQP7eoFK0jiOrvofDGdKi4NbsncfHCEJ/5vABQB9kxceug7376q7fi/5lteOHn664Nb9ccPvstLlx3ALVdAWA6zm27e2bCns78TQkyWTpxb/EEkG3vz1gH+9v5j4JcVANPgaar337/3zp0yRBm1jU5c39vueur10Qes5tIJwMheNgCDvaKQysq3333vjVtM3rWIxDLg5ovo7mnHdenE5189NbWDABy+bAAKZb2q7zPXfGvrVd1SJJZG/frtiBf3waQ1oX/DWsfobOC+PVtSR4ZGsGotrBrA4IAoJ+Li/Z+7/upWLZdFEU1QrR54bVlMhf6DptpNaFvj2zkRyGzc226M/mlqdVpYFYCbNoPl86y3a23td7vrikI0ZkLXZ+8AL05AMHRU14SQCmVxZUeT+Z2zgR/ZHKV77ujhy38e/3gQKwK4pQ+iKEDhIgRooqNc4h2CoH7/3i+tdxQzJbjaroUgmQgAA3SGKpeGZGEOzS0d6G2Y++JbM4FHLGbtb7s3YZLJiMBArmyF9vwb9GklAHv6IBNUWePw53NozOSwDrp5k8/v6vd6lAa/zdR0ZX8jBahHIO9DZ9uW9x8XUMlREAG3fxnJdAm3f2GjuCtlv/v4TPJr0+FiejmTnw3Fcm8LaWP4xm591m7CeRJI7OAoLnWLdMNGoUNSlK/6q2w7mprcnQ01Vbba5lahdUM/s1sALReDrpfBJIZ0rgh/13YKWMGt0UnvEKgKigK3hargOY81DZ1QTPXoypcZ03QHk8qbY7HY5qmp5P3BaFYPLEYz4Whm/o7+4ng8n39MohLvvm1r44MDV3YJ9vZuFLiEMmwoixYkdAWiwwfVJkCq2J/NTgnnKevIJUMAku8BoMoyQ0LLFT6UpTIKhSS0go58UqfWEWBWnNjg8aMfioiy6pwaPtvz5tFzPa+cmR2TtLI+9tyRYMbPqxzmiBtSdQEtvT2w1W4HzGsoEL2E5cG5DsYqmVc6bJG4LxCQ5PvxiQeHH7DVQKJ7bObKjxzuOrq/xMHji4jMTuPcKSd4yo+JsRBePjk/abbzf0hul/FaIJn54f7j0z+502FxcaURCyP70NHwY4hSF7hjG1BzE5hjAxHmeJ97N2U+RqfwngAs9N3fQdeddE0mdkrgiRD0wGnw6ALy+RIuhK+ArNdgdmYKfz8xdcpiKd9jUctnpQPHUNi9Bb8fXYqr/rHph2/xexRR6sdCTENz/SyQmQBOPomyuRnMuQ3MdysEF4FiFBBhqpIC1PVS8GoYkSVg/jT0cADcyEA0yVQACbPLPSTXeqTTEbwwOhPMiblvuOzGsf3D0C91wYERIzfYx558dSp8TdeGyK7OFpkZqT4EsyJqfCkwOQkRAQLzFyrhizCsm6gi28BNKmByQ5+ZhhH9J3hsmbRAgrWYINBZpuCBINGCVqTSSbz8zkwhkEg+7nUZwweGidMP+sDBUR7ftbn8zZ8PHRX2Xrv5hqs76uWyvgGl/DQam2XqAhNlLZEkdHCRMudvIzsehJiZh0wssGKRgDIKbgUjYejkD3OhdgLegVQ0hj8cHo6dXgg+WuMzfkFV11Y0Iiv0Rc2MB/74yuhDjmLsy/2b1pPu1iOgTqKu1QOm1ILLXnrKg/KyjszYI3BWaRDWrwVXSIjkirQggefyOHexFnK5DRfnLuCZ107lp8LBB2t9/LkK5R/phPtOgO8e0BckGA/vOxro8ddVbe6udyOpr6OSt4EzBZQjjLKC6OHfQjaK5IgcxfMhmDobK2UApYySqiJfbENgPo6R8bnydCj8a6eTD1HwzMdaMd1kDF7FZ2Ih7Vdare13Fr9FSCkEiTwVle4qmxF/5RCEyDwktwhmNSBqSehBaslaB1FUgqQylBnHqdkEXj9zcczp4j89dBwr7o0rzgJ+iSHusVbZmU5tpqgUiAkkMBXZyTPQZt6F6mSkcnqcSs9EA0Y8AMFJrUq/iZIEVc3BblXhMJlZiRe1leJ8JIBSkSiVFJ/d5kGuwGGuUchWTDAKDPG3DkElW5ZNNKlogogkOEbWzMiw9OVFiC3tVKg8ubMOq0WCIsu2UoW7TwLAbCJBS1afamlmqXQAjRWXy5sQHnoMplICZg9xTIYYibvgpE70eJchV8ZZKQZEgxCq62lX0KiFfWiscTjHA1FyKgRXXwEB3GaxW2TFxrO8xMiZEDnyLJTkBWiqGWdiDUiTSxZLFlQnBSSKYVjUKdS4A5DzF6gLXKQDJ6w2GV631Zqf5tX02snVV8CAYLOqtbKsMU5Dp7Awicz0GBJ8DQ2ojcgaXmSiJRi5CLzmKuTLtWDcg4ViAFbzSfiUBZoRbdTsRIEiq1wXCYC+egpKumA1W7HGKS+jQENn+KV5xLSbkdecEEsR5BILOLMQMSYWL4brJhzKjr7Wqta2JijWHpQy7Ygl54hDGliVAcZliZabtStG/ygAsgRBNRk05fNIxHxYStpRTMfhdUZxbGbWeOPUTEAXis/brcbTF5ZTjt88v3Sf1zW+8yt3XVfVva6ZpYMMit50aXrKTAUhaNjdD/HA8Q+XYUUAmm6IiiyaFgIpTI4vIx1PYC4S42cXlxYThcRTFpU9a7UYszSjNc45IxZG0tlo78+eOHBv+9q6weu2dpnbqm1MoH3CojBIguwTUCAjWSUASSJhhfPikUPTfHxiqXQ6uDgiKqWn3B5+eI0NoX3v/u9uZ1Qc7o09A9pIKHz+0V8+s3Cb12G/dXtfS6vP7BDIQkyGUNnfVkkBl43EiYnAs6MnAxslRT/o9Rr7yXFDQ+98eKn84DH0ns+P7+kzzuSNzNMH/z1+FzekbYKov+Rx0Ca/WgD734Q+OKB9jzFYyFFz9H/vE/3pHBqt3K9PDQ7gYWaUVNrWin89unIb/BfD0uDLmcThogAAAABJRU5ErkJggg=="
const val HOLLOW_STAR_PNG_BASE64 =
    "data:image/png;base64,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"

interface TitleStarCounterProps : Props {
    var total: Int
    var current: Int
}

class TitleStarCounter : RComponent<TitleStarCounterProps, State>() {
    override fun RBuilder.render() {
        if (props.total > 5) {
            span {
                attrs.classes = jsObjectBackedSetOf("map-title-text")
                +"${props.current}/${props.total}"
            }
            icon(STAR_PNG_BASE64)
        } else {
            repeat(props.current) {
                icon(STAR_PNG_BASE64)
            }
            repeat(props.total - props.current) {
                icon(HOLLOW_STAR_PNG_BASE64)
            }
        }
    }
}
