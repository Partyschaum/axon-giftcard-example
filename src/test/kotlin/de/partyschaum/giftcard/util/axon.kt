package de.partyschaum.giftcard.util

import org.axonframework.test.aggregate.TestExecutor

fun <T> TestExecutor<T>.whenever(command: Any) = this.`when`(command)
fun <T> TestExecutor<T>.whenever(command: Any, metaData: MutableMap<String, *>) = this.`when`(command, metaData)
