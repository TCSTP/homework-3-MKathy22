package tcs.app.dev.homework1.data

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ShopTab(
    cart: Cart, //current cart
    onAddToCart: (Item) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn( //renders shop items
        modifier = modifier
            .fillMaxSize() //full screen
            .padding(top = 56.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) //space between item cards
    ) {
        items(MockData.ExampleShop.items.toList()) { item -> //all items from shop - list of all available products
            val price = MockData.ExampleShop.prices[item]!! //check item price for job price

            Card( //each item shown in a card
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp) //shadow
            ) {
                Row( // layout for item details (image, name, price, button)
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), //internal padding in card
                    horizontalArrangement = Arrangement.SpaceBetween, //space between details and button
                    verticalAlignment = Alignment.CenterVertically //align vertical center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) { // left section: item image and info
                        Image(  // Product image, fetched using MockData image reference
                            painter = painterResource(id = MockData.getImage(item)),
                            contentDescription = stringResource(id = MockData.getName(item)), //accessibility label
                            modifier = Modifier.size(48.dp) //mini sized image
                        )
                        Spacer(Modifier.width(8.dp)) // Space between image and text
                        Column { //product name and price
                            Text( //item name
                                text = stringResource(id = MockData.getName(item)),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text( //item price
                                text = price.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Button(onClick = { onAddToCart(item) }) { //add to cart button
                        Text("Add to Cart")
                    }
                }
            }
        }
    }
}
