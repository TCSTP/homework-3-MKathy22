package tcs.app.dev.homework1.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tcs.app.dev.homework1.data.Cart
import tcs.app.dev.homework1.data.Discount
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*

@Composable
fun DiscountsTab(
    discounts: List<Discount>, //discounts
    cart: Cart, //current shopping cart (items, discounts, total)
    onAddDiscount: (Discount) -> Unit, //when adding discount
    modifier: Modifier = Modifier //layout
) {
    LazyColumn( //vertical area for discount list
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(discounts) { discount -> //iterate every discount in list
            Card( //card for every discount for clarity
                modifier = Modifier.fillMaxWidth(), //card  = full width
                elevation = CardDefaults.cardElevation(4.dp) //small shadow effect
            ) {
                Row( //horizontal layout - text left, button right
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp), //internal padding
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(discount.description()) //left: discount description text
                    Button(
                        onClick = { onAddDiscount(discount) }, //apply/add discount at click
                        enabled = !cart.discounts.contains(discount) //if discount already applied - disable
                    ) {
                        Text("Add to Cart")
                    }
                }
            }
        }
    }
}
fun Discount.description(): String = when (this) {
    is Discount.Fixed -> "Save ${amount}"
    is Discount.Percentage -> "${value}% off your total"
    is Discount.Bundle -> "Buy $amountItemsGet ${item.id}s, pay for $amountItemsPay"
}