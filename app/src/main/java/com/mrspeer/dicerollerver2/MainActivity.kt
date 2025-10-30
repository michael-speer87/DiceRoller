package com.mrspeer.dicerollerver2

import android.media.Image
import android.os.Bundle
import android.text.Layout
import android.widget.NumberPicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrspeer.dicerollerver2.ui.theme.DiceRollerVer2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var hasRolled by remember { mutableStateOf(false) }
            var diceSize by remember { mutableIntStateOf(6) }
            var diceRoll by remember { mutableIntStateOf(0) }
            var numberInput by remember { mutableStateOf("") }

            DiceRollerVer2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        NavBar(
                            diceSize,
                            onDiceSizeChange = { newSize ->
                                diceSize = newSize
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                        if (diceSize == 100 || diceSize == 20) {
                            // empty
                        } else {
                            TextField(
                                value = numberInput,
                                onValueChange = { newValue ->
                                    if (newValue.all { it.isDigit() }) {
                                        numberInput = newValue
                                    }
                                },
                                label = { Text("Enter the amount of Dice") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        DiceRoller(
                            hasRolled,
                            diceSize,
                            diceRoll,
                            onDiceRollChange = { newRoll ->
                                diceRoll = newRoll
                                hasRolled = true
                            },
                            onDiceNumberChange = { newNumber ->
                                numberInput = newNumber.toString()
                            },
                            numberInput,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavBar(
    diceSize: Int,
    onDiceSizeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            Button(
                onClick = { onDiceSizeChange(100) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                ),
                modifier = Modifier

            ) {
                Text(text = "D100")
            }
            Button(
                onClick = { onDiceSizeChange(20) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier
            ) {
                Text(text = "D20")
            }
            Button(
                onClick = { onDiceSizeChange(12) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier
            ) {
                Text(text = "D12")
            }
            Button(
                onClick = { onDiceSizeChange(10) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier
            ) {
                Text(text = "D10")
            }
        }
        Row (
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { onDiceSizeChange(8) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier
            ) {
                Text(text = "D8")
            }
            Button(
                onClick = { onDiceSizeChange(6) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier
            ) {
                Text(text = "D6")
            }
            Button(
                onClick = { onDiceSizeChange(4) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier
            ) {
                Text(text = "D4")
            }
        }
    }
}

@Composable
fun DiceRoller(
    hasRolled: Boolean,
    diceSize: Int,
    diceRoll: Int,
    onDiceRollChange: (Int) -> Unit,
    onDiceNumberChange: (Int) -> Unit,
    numberInput: String,
    modifier: Modifier = Modifier
) {
    var rollList by remember { mutableStateOf(emptyList<Int>()) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        if (rollList.isNotEmpty()) {
            Text(text = "Rolled a ${rollList.sum()}", fontSize = 24.sp)
        } else {
            Text(text = "Select Dice", fontSize = 24.sp)
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (diceSize == 100) {
                Row {
                    Image(
                        painter = painterResource(getDiceImage(diceRoll, diceSize, 1)),
                        contentDescription = "A die showing $diceRoll",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(horizontal = 8.dp)
                    )
                    Image(
                        painter = painterResource(getDiceImage(diceRoll, diceSize, 2)),
                        contentDescription = "A die showing $diceRoll",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
            } else {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(rollList.size) { index ->
                        val roll = rollList[index]
                        Image(
                            painter = painterResource(getDiceImage(roll, diceSize)),
                            contentDescription = "A die showing $roll",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
        Button(
            onClick = {
                val numberOfDice = numberInput.toIntOrNull() ?: 1
                val newRolls = mutableListOf<Int>()
                for (i in 1..numberOfDice) {
                    newRolls.add(rollDice(diceSize))
                }
                rollList = newRolls
                onDiceRollChange(newRolls.sum())
                onDiceNumberChange(1)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray
            ),
        ) {
            Text(text = "Roll a D${diceSize}")
        }
    }
}

fun getDiceImage(diceRoll: Int, diceSize: Int, imageNumber: Int = 1): Int {
    when (diceSize) {
        4 -> {
            return when (diceRoll) {
                1 -> R.drawable.d4_1
                2 -> R.drawable.d4_2
                3 -> R.drawable.d4_3
                4 -> R.drawable.d4_4
                else -> R.drawable.d4_1
            }
        }
        6 -> {
            return when (diceRoll) {
                1 -> R.drawable.d6_1
                2 -> R.drawable.d6_2
                3 -> R.drawable.d6_3
                4 -> R.drawable.d6_4
                5 -> R.drawable.d6_5
                6 -> R.drawable.d6_6
                else -> R.drawable.d6_1
            }
        }
        8 -> {
            return when (diceRoll) {
                1 -> R.drawable.d8_1
                2 -> R.drawable.d8_2
                3 -> R.drawable.d8_3
                4 -> R.drawable.d8_4
                5 -> R.drawable.d8_5
                6 -> R.drawable.d8_6
                7 -> R.drawable.d8_7
                8 -> R.drawable.d8_8
                else -> R.drawable.d8_1
            }
        }
        10 -> {
            return when (diceRoll) {
                1 -> R.drawable.d10_1
                2 -> R.drawable.d10_2
                3 -> R.drawable.d10_3
                4 -> R.drawable.d10_4
                5 -> R.drawable.d10_5
                6 -> R.drawable.d10_6
                7 -> R.drawable.d10_7
                8 -> R.drawable.d10_8
                9 -> R.drawable.d10_9
                10 -> R.drawable.d10_10
                else -> R.drawable.d10_1
            }
        }
        12 -> {
            return when (diceRoll) {
                1 -> R.drawable.d12_1
                2 -> R.drawable.d12_2
                3 -> R.drawable.d12_3
                4 -> R.drawable.d12_4
                5 -> R.drawable.d12_5
                6 -> R.drawable.d12_6
                7 -> R.drawable.d12_7
                8 -> R.drawable.d12_8
                9 -> R.drawable.d12_9
                10 -> R.drawable.d12_10
                11 -> R.drawable.d12_11
                12 -> R.drawable.d12_12
                else -> R.drawable.d12_1
            }
        }
        20 -> {
            return when (diceRoll) {
                1 -> R.drawable.d20_1
                2 -> R.drawable.d20_2
                3 -> R.drawable.d20_3
                4 -> R.drawable.d20_4
                5 -> R.drawable.d20_5
                6 -> R.drawable.d20_6
                7 -> R.drawable.d20_7
                8 -> R.drawable.d20_8
                9 -> R.drawable.d20_9
                10 -> R.drawable.d20_10
                11 -> R.drawable.d20_11
                12 -> R.drawable.d20_12
                13 -> R.drawable.d20_13
                14 -> R.drawable.d20_14
                15 -> R.drawable.d20_15
                16 -> R.drawable.d20_16
                17 -> R.drawable.d20_17
                18 -> R.drawable.d20_18
                19 -> R.drawable.d20_19
                20 -> R.drawable.d20_20
                else -> R.drawable.d20_1
            }
        }
        100 -> {
            if (imageNumber == 1) {
                return when (diceRoll) {
                    in 1..9 -> R.drawable.d10_00
                    in 10..19 -> R.drawable.d10_10
                    in 20..29 -> R.drawable.d10_20
                    in 30..39 -> R.drawable.d10_30
                    in 40..49 -> R.drawable.d10_40
                    in 50..59 -> R.drawable.d10_50
                    in 60..69 -> R.drawable.d10_60
                    in 70..79 -> R.drawable.d10_70
                    in 80..89 -> R.drawable.d10_80
                    in 90..99 -> R.drawable.d10_90
                    else -> R.drawable.d10_00
                }
            }
            else {
                return when (diceRoll % 10) {
                    1 -> R.drawable.d10_1
                    2 -> R.drawable.d10_2
                    3 -> R.drawable.d10_3
                    4 -> R.drawable.d10_4
                    5 -> R.drawable.d10_5
                    6 -> R.drawable.d10_6
                    7 -> R.drawable.d10_7
                    8 -> R.drawable.d10_8
                    9 -> R.drawable.d10_9
                    else -> R.drawable.d10_0
                }
            }
        }
    }
    return R.drawable.d6_1
}

fun rollDice(diceSize: Int): Int {
    return (1..diceSize).random()
}
