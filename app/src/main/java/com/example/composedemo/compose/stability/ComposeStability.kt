package com.example.composedemo.compose.stability

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composedemo.ui.theme.ComposeDemoTheme
import com.example.domainmodule.DomainModel

@Composable
internal fun ComposeStability(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier.padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val myState2 = MyState2("Android", 2)
        val myState2Name by remember { mutableStateOf(myState2.name) }

        Greeting0(name = myState2Name)
        Greeting1(state = MyState1("Android", 1))
        Greeting2(state = myState2)
        Greeting3(state = MyState3("Android", age = 3, list = listOf()))
        Greeting4(state = MyState4("Android", 4))
        Greeting5(state = MyState5("Android"))
        Greeting6(
            state = MyState6(
                name = "Android",
                age = 6,
                domainModel = DomainModel(name = "domain name"),
            )
        )
//                        RecompositionCounter()
    }
}

@Composable
private fun Greeting0(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
private fun Greeting1(state: MyState1, modifier: Modifier = Modifier) {
    Text(
        text = "Hello ${state.name}!",
        modifier = modifier
    )
}

@Composable
private fun Greeting2(state: MyState2, modifier: Modifier = Modifier) {
    Text(
        text = "Hello ${state.name}!",
        modifier = modifier
    )
}

@Composable
private fun Greeting3(state: MyState3, modifier: Modifier = Modifier) {
    Text(
        text = "Hello ${state.name}!",
        modifier = modifier
    )
}

@Composable
private fun Greeting4(state: MyState4, modifier: Modifier = Modifier) {
    Text(
        text = "Hello ${state.name}!",
        modifier = modifier
    )
}

@Composable
private fun Greeting5(state: MyState5<String>, modifier: Modifier = Modifier) {
    Text(
        text = "Hello ${state.value}!",
        modifier = modifier
    )
}

@Composable
private fun Greeting6(state: MyState6, modifier: Modifier = Modifier) {
    Text(
        text = "Hello ${state.name}!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    ComposeDemoTheme {
        Greeting1(MyState1("Android", 4))
    }
}


private data class MyState1(
    val name: String,
    val age: Int,
    val uiModel: UiModel = UiModel("test"),
)

private data class UiModel(val name: String)

private data class MyState2(
    var name: String,
    val age: Int,
)

private data class MyState3(
    val name: String,
    val age: Int,
    val list: List<String>,
)

@Stable
private data class MyState4(
    val name: String,
    val age: Int,
) {
    private var count = 0

    fun increment() {
        count++
    }

    fun count(): Int = count

    fun reset() {
        count = 0
    }
}

private interface MyStateInterface<out T> {
    val value: T
}

private data class MyState5<T>(override val value: T) : MyStateInterface<T>

@Immutable
private data class MyState6(
    val name: String,
    val age: Int,
    val domainModel: DomainModel,
)


@Stable
private data class MyStableState3(
    val name: String,
    val age: Int,
    val list: List<String>,
)

private data class MyStableState3Vol2(
    val name: String,
    val age: Int,
    val immutableWrapper: ImmutableWrapper,
)

@Immutable
private data class ImmutableWrapper(
    val list: List<String>,
)
