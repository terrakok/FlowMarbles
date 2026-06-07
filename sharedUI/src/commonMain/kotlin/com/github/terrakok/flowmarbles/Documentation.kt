package com.github.terrakok.flowmarbles

val FilterDoc = """
    Returns a flow containing only values of the original flow that match the given [predicate].
""".trimIndent()

val DropDoc = """
   Returns a flow that ignores first [count] elements.
   Throws [IllegalArgumentException] if [count] is negative.
""".trimIndent()

val DropWhileDoc = """
    Returns a flow containing all elements except first elements that satisfy the given predicate.
""".trimIndent()

val TakeDoc = """
    Returns a flow that contains first [count] elements.
    When [count] elements are consumed, the original flow is cancelled.
    Throws [IllegalArgumentException] if [count] is not positive.
""".trimIndent()

val TakeWhileDoc = """
    Returns a flow that contains first elements satisfying the given [predicate].
    
    Note, that the resulting flow does not contain the element on which the [predicate] returned `false`.
    See [transformWhile] for a more flexible operator.
""".trimIndent()

val DebounceDoc = """
    Returns a flow that mirrors the original flow, but filters out values
    that are followed by the newer values within the given [timeout].
    The latest value is always emitted.
    
    Note that the resulting flow does not emit anything as long as the original flow emits
    items faster than every [timeout] milliseconds.
""".trimIndent()

val SampleDoc = """
    Returns a flow that emits only the latest value emitted by the original flow during the given sampling [period].
    
    Note that the latest element is not emitted if it does not fit into the sampling window.
""".trimIndent()

val DistinctUntilChangedDoc = """
    Returns flow where all subsequent repetitions of the same key are filtered out, where
    key is extracted with [keySelector] function.
    
    Note that repeated application of `distinctUntilChanged` operator with the same parameter has no effect.
""".trimIndent()

val MapDoc = """
    Returns a flow containing the results of applying the given [transform] function to each value of the original flow.
""".trimIndent()

val MapLatestDoc = """
    Returns a flow that emits elements from the original flow transformed by [transform] function.
    When the original flow emits a new value, computation of the [transform] block for previous value is cancelled.
    
    This operator is [buffered] by default and size of its output buffer can be changed by applying subsequent [buffer] operator.
""".trimIndent()

val TransformDoc = """
    Applies [transform] function to each value of the given flow.
    
    The receiver of the `transform` is [FlowCollector] and thus `transform` is a
    flexible function that may transform emitted element, skip it or emit it multiple times.
    
    This operator generalizes [filter] and [map] operators and
    can be used as a building block for other operators.
""".trimIndent()

val TransformLatestDoc = """
    Returns a flow that produces element by [transform] function every time the original flow emits a value.
    When the original flow emits a new value, the previous `transform` block is cancelled, thus the name `transformLatest`.
    
    This operator is [buffered] by default
    and size of its output buffer can be changed by applying subsequent [buffer] operator.
""".trimIndent()

val TransformWhileDoc = """
    Applies [transform] function to each value of the given flow while this
    function returns `true`.
    
    The receiver of the `transformWhile` is [FlowCollector] and thus `transformWhile` is a
    flexible function that may transform emitted element, skip it or emit it multiple times.
    
    This operator generalizes [takeWhile] and can be used as a building block for other operators.
""".trimIndent()

val WithIndexDoc = """
    Returns a flow that wraps each element into [IndexedValue], containing value and its index (starting from zero).
""".trimIndent()

val RunningReduceDoc = """
    Reduces the given flow with [operation], emitting every intermediate result, including initial value.
    The first element is taken as initial value for operation accumulator.
    This operator has a sibling with initial value -- [scan].
""".trimIndent()

val MergeDoc = """
    Merges the given flows into a single flow without preserving an order of elements.
    All flows are merged concurrently, without limit on the number of simultaneously collected flows.
    
    ### Operator fusion
    
    Applications of [flowOn], [buffer], and [produceIn] _after_ this operator are fused with
    its concurrent merging so that only one properly configured channel is used for execution of merging logic.
""".trimIndent()

val CombineDoc = """
    Returns a [Flow] whose values are generated with [transform] function by combining
    the most recently emitted values by each flow.
    
    This function is a shorthand for `flow.combineTransform(flow2) { a, b -> emit(transform(a, b)) }`
""".trimIndent()

val ZipDoc = """
    Zips values from the current flow (`this`) with [other] flow using provided [transform] function applied to each pair of values.
    The resulting flow completes as soon as one of the flows completes and cancel is called on the remaining flow.
    
    ### Buffering
    
    The upstream flow is collected sequentially in the same coroutine without any buffering, while the
    [other] flow is collected concurrently as if `buffer(0)` is used. See documentation in the [buffer] operator
    for explanation. You can use additional calls to the [buffer] operator as needed for more concurrency.
""".trimIndent()

val FlatMapMergeDoc = """
    Transforms elements emitted by the original flow by applying [transform], that returns another flow,
    and then merging and flattening these flows.
    
    This operator calls [transform] *sequentially* and then merges the resulting flows with a [concurrency]
    limit on the number of concurrently collected flows.
    It is a shortcut for `map(transform).flattenMerge(concurrency)`.
    See [flattenMerge] for details.
    
    Note that even though this operator looks very familiar, we discourage its usage in a regular application-specific flows.
    Most likely, suspending operation in [map] operator will be sufficient and linear transformations are much easier to reason about.
    
    ### Operator fusion
    
    Applications of [flowOn], [buffer], and [produceIn] _after_ this operator are fused with
    its concurrent merging so that only one properly configured channel is used for execution of merging logic.
""".trimIndent()

val FlatMapConcatDoc = """
    Transforms elements emitted by the original flow by applying [transform], that returns another flow,
    and then concatenating and flattening these flows.
    
    This method is a shortcut for `map(transform).flattenConcat()`. See [flattenConcat].
    
    Note that even though this operator looks very familiar, we discourage its usage in a regular application-specific flows.
    Most likely, suspending operation in [map] operator will be sufficient and linear transformations are much easier to reason about.
""".trimIndent()

val FlatMapLatestDoc = """
    Returns a flow that switches to a new flow produced by [transform] function every time the original flow emits a value.
    When the original flow emits a new value, the previous flow produced by `transform` block is cancelled.
    
    This operator is [buffered] by default and size of its output buffer can be changed by applying subsequent [buffer] operator.
""".trimIndent()