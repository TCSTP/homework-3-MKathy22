package tcs.app.dev.homework1

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import tcs.app.dev.homework1.data.Cart
import tcs.app.dev.homework1.data.Discount
import tcs.app.dev.homework1.data.DiscountsTab
import tcs.app.dev.homework1.data.Shop
import tcs.app.dev.homework1.data.ShopTab
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import tcs.app.dev.homework1.data.CartTab
import tcs.app.dev.homework1.data.minus
import tcs.app.dev.homework1.data.plus
import tcs.app.dev.homework1.data.update

/**
 * # Homework 3 — Shop App
 *
 * Build a small shopping UI with ComposeUI using the **example data** from the
 * `tcs.app.dev.homework.data` package (items, prices, discounts, and ui resources).
 * The goal is to implement three tabs: **Shop**, **Discounts**, and **Cart**.
 *
 * ## Entry point
 *
 * The composable function [ShopScreen] is your entry point that holds the UI state
 * (selected tab and the current `Cart`).
 *
 * ## Data
 *
 * - Use the provided **example data** and data types from the `data` package:
 *   - `Shop`, `Item`, `Discount`, `Cart`, and `Euro`.
 *   - There are useful resources in `res/drawable` and `res/values/strings.xml`.
 *     You can add additional ones.
 *     Do **not** hard-code strings in the UI!
 *
 * ## Requirements
 *
 * 1) **Shop item tab**
 *    - Show all items offered by the shop, each row displaying:
 *      - item image + name,
 *      - item price,
 *      - an *Add to cart* button.
 *    - Tapping *Add to cart* increases the count of that item in the cart by 1.
 *
 * 2) **Discount tab**
 *    - Show all available discounts with:
 *      - an icon + text describing the discount,
 *      - an *Add to cart* button.
 *    - **Constraint:** each discount can be added **at most once**.
 *      Disable the button (or ignore clicks) for discounts already in the cart.
 *
 * 3) **Cart tab**
 *    - Only show the **Cart** tab contents if the cart is **not empty**. Within the cart:
 *      - List each cart item with:
 *        - image + name,
 *        - per-row total (`price * amount`),
 *        - an amount selector to **increase/decrease** the quantity (min 0, sensible max like 99).
 *      - Show all selected discounts with a way to **remove** them from the cart.
 *      - At the bottom, show:
 *        - the **total price** of the cart (items minus discounts),
 *        - a **Pay** button that is enabled only when there is at least one item in the cart.
 *      - When **Pay** is pressed, **simulate payment** by clearing the cart and returning to the
 *        **Shop** tab.
 *
 * ## Navigation
 * - **Top bar**:
 *      - Title shows either the shop name or "Cart".
 *      - When not in Cart, show a cart icon.
 *        If you feel fancy you can add a badge to the icon showing the total count (capped e.g. at "99+").
 *      - The cart button is enabled only if the cart contains items. In the Cart screen, show a back
 *        button to return to the shop.
 *
 * - **Bottom bar**:
*       - In Shop/Discounts, show a 2-tab bottom bar to switch between **Shop** and **Discounts**.
*       - In Cart, hide the tab bar and instead show the cart bottom bar with the total and **Pay**
*         action as described above.
 *
 * ## Hints
 * - Keep your cart as a single source of truth and derive counts/price from it.
 *   Rendering each list can be done with a `LazyColumn` and stable keys (`item.id`, discount identity).
 * - Provide small reusable row components for items, cart rows, and discount rows.
 *   This keeps the screen implementation compact.
 *
 * ## Bonus (optional)
 * Make the app feel polished with simple animations, such as:
 * - `AnimatedVisibility` for showing/hiding the cart,
 * - `animateContentSize()` on rows when amounts change,
 * - transitions when switching tabs or updating the cart badge.
 *
 * These can help if want you make the app feel polished:
 * - [NavigationBar](https://developer.android.com/develop/ui/compose/components/navigation-bar)
 * - [Card](https://developer.android.com/develop/ui/compose/components/card)
 * - [Swipe to dismiss](https://developer.android.com/develop/ui/compose/touch-input/user-interactions/swipe-to-dismiss)
 * - [App bars](https://developer.android.com/develop/ui/compose/components/app-bars#top-bar)
 * - [Pager](https://developer.android.com/develop/ui/compose/layouts/pager)
 *
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    shop: Shop, //shop with item data + prices
    availableDiscounts: List<Discount>, //list of discount options
    modifier: Modifier = Modifier //layout
) {
    var selectedTab by rememberSaveable { mutableStateOf(0) } // what tab is currently selected: 0 = Shop, 1 = Discounts, 2 = Cart
    var cart by rememberSaveable { mutableStateOf(Cart(shop = shop)) } //current cart state (items + discounts)

    Scaffold( //overall structure
        topBar = {
            // Top Nav Bar
            TopAppBar( //shows title + evtl. cart icon
                title = {
                    when (selectedTab) {
                        0 -> Text("Shop")
                        1 -> Text("Discounts")
                        2 -> Text("Cart")
                    }
                },
                actions = {
                    if (selectedTab != 2) { //cart icon, except if selected tab is cart (2)
                        IconButton(
                            onClick = { selectedTab = 2 }, //navigate to cart tab (2)
                            enabled = cart.itemCount > 0u //cart empty - icon disabled
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping Cart") //accessibility description - not necessary, like HTML alt)
                        }
                    }
                }
            )
        },
        //Bottom Nav Bar - switching between shop and discounts
        bottomBar = {
            if (selectedTab != 2) { //only shown, if not already cart tab
                NavigationBar {
                    NavigationBarItem( //SHop Tab
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Text("🛍️") },
                        label = { Text("Shop") }
                    )
                    NavigationBarItem( //Discount Tab
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Text("💸") },
                        label = { Text("Discounts") }
                    )
                }
            } else { // already in  / selected cart tab
                BottomAppBar {
                    //left: total price
                    Text(
                        text = "Total: ${cart.price}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f) //leftover space
                            .padding(start = 16.dp)
                    )
                    //right: pay button
                    Button(
                        onClick = { //checkout/leave
                            cart = Cart(shop = shop) // clear cart
                            selectedTab = 0 //back to shop tab
                        },
                        enabled = cart.itemCount > 0u, //only active if cart is not empty
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text("Pay")
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues -> //ensures the content isn't hidden under bars
        when (selectedTab) {
            //Tab 0/Shop: list of addible products
            0 -> ShopTab(
                cart = cart, //current cart
                onAddToCart = { item -> cart = cart + item }, //add item to cart
                modifier = Modifier.padding(paddingValues)
            )
            //Tab 1/discounts: shows all available discounts
            1 -> DiscountsTab(
                discounts = availableDiscounts, //discount list
                cart = cart, //current cart
                onAddDiscount = { discount -> cart = cart + discount }, //add discount to cart
                modifier = Modifier.padding(paddingValues)
            )
            // Tab 2/Cart: content, used discounts,total price
            2 -> CartTab(
                cart = cart, // current cart
                onUpdateItem = { item, amount -> cart = cart.update(item to amount) }, //update item amount
                onRemoveDiscount = { discount -> cart = cart - discount }, //remove a discount
                onPay = { // Same as Pay button in BottomAppBar — resets cart and returns to Shop
                    cart = Cart(shop = shop)
                    selectedTab = 0
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}
