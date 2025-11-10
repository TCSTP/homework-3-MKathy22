package tcs.app.dev.homework1.data

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CartTab(
    cart: Cart, //current shopping cart (items, discounts, total)
    onUpdateItem: (Item, UInt) -> Unit, // when changing item
    onRemoveDiscount: (Discount) -> Unit, //when deleting discount
    onPay: () -> Unit, //done when clicking pay button
    modifier: Modifier = Modifier //layout
) {
    Column( //vertical area for cart screen
        modifier = modifier
            .fillMaxSize() //fill entire screen
            .padding(16.dp) //spacing around whole screen
    ) {
        if (cart.items.isNotEmpty()) { //1+ items in cart
            Text("Cart Items", style = MaterialTheme.typography.titleMedium) //healine sytle
            Spacer(Modifier.height(8.dp)) //space between headline and list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) { //list scrollable of items
                items(cart.items.keys.toList()) { item -> //list of all item keys
                    val amount = cart.items[item] ?: 0u //item amount
                    val price = cart.shop.prices[item]!! * amount //item price
                    Row( //each cart row: item image, name, amount, price
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, //content spread evenly
                        verticalAlignment = Alignment.CenterVertically //vertically centered content
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image( //image use drawable form Metadata
                                painter = painterResource(id = MockData.getImage(item)),
                                contentDescription = stringResource(id = MockData.getName(item)),
                                modifier = Modifier.size(40.dp) //fixed img size
                            )
                            Spacer(Modifier.width(8.dp)) //space/gap between image and text
                            Text(stringResource(id = MockData.getName(item))) //product name from Mockdata
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) { //quantity amount
                            IconButton(onClick = {
                                if (amount > 0u) onUpdateItem(
                                    item,
                                    amount - 1u
                                ) //decrease amount - update with one item less
                            }) { Text("-") } //minus
                            Text(
                                amount.toString(),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) //increase amount - update with one more item
                            IconButton(onClick = {
                                onUpdateItem(
                                    item,
                                    amount + 1u
                                )
                            }) { Text("+") }//plus
                        }
                        Text(price.toString()) //total price
                    }
                }
            }
        }

        if (cart.discounts.isNotEmpty()) { //if active discounts
            Spacer(Modifier.height(12.dp))
            Text("Discounts", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { //display applied discounts
                cart.discounts.forEach { discount ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween //space between discount and button
                    ) {
                        Text(discount.description())//nicer output text
                        TextButton(onClick = { onRemoveDiscount(discount) }) {//button to remove discount
                            Text("Remove")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Row( //total and pay button row
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Total: ${cart.price}",
                style = MaterialTheme.typography.titleLarge
            ) //display total cart price incl. discounts
            Button(onClick = onPay, enabled = cart.itemCount > 0u) { //pay button
                Text("Pay")
            }
        }
    }
}
