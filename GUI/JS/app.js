// Data & Model

const menu = {
  itemTypes: [
    { name: "Wheat", basePrice: 5.0 },
    { name: "White", basePrice: 4.5 },
    { name: "Rye", basePrice: 5.5 }
  ],
  sizes: ["Small", "Medium", "Large"],
  regularToppings: ["Lettuce", "Tomato", "Onion", "Pickle", "Mustard"],
  premiumToppings: [
    { name: "Bacon", price: 1.0 },
    { name: "Turkey", price: 1.5 },
    { name: "Cheddar Cheese", price: 1.25 },
    { name: "Swiss Cheese", price: 1.25 }
  ],
  drinkFlavors: ["Cola", "Lemonade", "Iced Tea"],
  sides: [
    { name: "Fries", price: 2.5 },
    { name: "Salad", price: 3.0 },
    { name: "Onion Rings", price: 3.0 }
  ],
  sizePriceMultiplier: {
    "Small": 1.0,
    "Medium": 1.5,
    "Large": 2.0
  }
};

let order = {
  items: [],
  drinks: [],
  sides: []
};

// Utility Functions

function calculateItemPrice(item) {
  let base = item.basePrice * menu.sizePriceMultiplier[item.size];
  let premiumExtraCost = 0;
  for (const topping of item.toppings) {
    if (topping.type === "premium" && topping.quantity > 1) {
      premiumExtraCost += topping.price * (topping.quantity - 1);
    }
  }
  return base + premiumExtraCost;
}

function calculateDrinkPrice(drink) {
  return 2.0 * menu.sizePriceMultiplier[drink.size]; // fixed base price 2.0
}

function calculateTotal() {
  let total = 0;
  for (const item of order.items) {
    total += calculateItemPrice(item);
  }
  for (const drink of order.drinks) {
    total += calculateDrinkPrice(drink);
  }
  for (const side of order.sides) {
    total += side.price;
  }
  return total;
}

function formatOrderSummary() {
  let lines = [];
  if (order.items.length > 0) {
    lines.push("Items:");
    order.items.forEach((item, i) => {
      lines.push(` ${i + 1}) ${item.size} ${item.type}${item.specialOption ? ` with ${item.specialOption}` : ''} - $${calculateItemPrice(item).toFixed(2)}`);
      lines.push("    Toppings:");
      item.toppings.forEach(t => {
        lines.push(`     - ${t.name} x${t.quantity}`);
      });
    });
  }
  if (order.drinks.length > 0) {
    lines.push("\nDrinks:");
    order.drinks.forEach((drink, i) => {
      lines.push(` ${i + 1}) ${drink.size} ${drink.flavor} - $${calculateDrinkPrice(drink).toFixed(2)}`);
    });
  }
  if (order.sides.length > 0) {
    lines.push("\nSides:");
    order.sides.forEach((side, i) => {
      lines.push(` ${i + 1}) ${side.name} - $${side.price.toFixed(2)}`);
    });
  }
  lines.push(`\nTotal: $${calculateTotal().toFixed(2)}`);
  return lines.join("\n");
}

function downloadReceipt() {
  const now = new Date();
  const timestamp = now.toISOString().replace(/[:.]/g, '-');
  const receiptContent = `--- Order Receipt ---\nDate: ${now.toLocaleString()}\n\n${formatOrderSummary()}`;
  const blob = new Blob([receiptContent], { type: "text/plain" });
  const url = URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = `receipt_${timestamp}.txt`;
  a.click();
  URL.revokeObjectURL(url);
}

function resetOrder() {
  order = { items: [], drinks: [], sides: [] };
}

// UI Logic

const screens = {
  home: document.getElementById("home-screen"),
  order: document.getElementById("order-screen"),
  addItem: document.getElementById("add-item-screen"),
  addDrink: document.getElementById("add-drink-screen"),
  addSide: document.getElementById("add-side-screen"),
  checkout: document.getElementById("checkout-screen"),
  exit: document.getElementById("exit-screen")
};

function showScreen(screenName) {
  for (const key in screens) {
    screens[key].classList.remove("active");
  }
  screens[screenName].classList.add("active");
  if (screenName === "order") {
    renderOrderSummary();
  }
  if (screenName === "add-item") {
    populateItemForm();
  }
  if (screenName === "add-drink") {
    populateDrinkForm();
  }
  if (screenName === "add-side") {
    populateSideForm();
  }
  if (screenName === "checkout") {
    document.getElementById("checkout-summary").textContent = formatOrderSummary();
  }
}

// Populate Forms

function populateItemForm() {
  const itemTypeSelect = document.getElementById("item-type");
  itemTypeSelect.innerHTML = "";
  menu.itemTypes.forEach(type => {
    const option = document.createElement("option");
    option.value = type.name;
    option.textContent = `${type.name} ($${type.basePrice.toFixed(2)} base)`;
    itemTypeSelect.appendChild(option);
  });

  // Regular toppings checkboxes
  const regToppingsDiv = document.getElementById("regular-toppings");
  regToppingsDiv.innerHTML = "";
  menu.regularToppings.forEach(topping => {
    const label = document.createElement("label");
    label.className = "topping-option";
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.value = topping;
    label.appendChild(checkbox);
    label.appendChild(document.createTextNode(topping));
    regToppingsDiv.appendChild(label);
  });

  // Premium toppings checkboxes + quantity inputs
  const premToppingsDiv = document.getElementById("premium-toppings");
  premToppingsDiv.innerHTML = "";
  menu.premiumToppings.forEach(topping => {
    const container = document.createElement("div");
    container.className = "topping-option";

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.value = topping.name;
    checkbox.className = "premium-checkbox";

    const label = document.createElement("label");
    label.textContent = topping.name + ` ($${topping.price.toFixed(2)} for extras)`;

    const quantityInput = document.createElement("input");
    quantityInput.type = "number";
    quantityInput.min = 1;
    quantityInput.value = 1;
    quantityInput.className = "topping-quantity";
    quantityInput.disabled = true;

    checkbox.addEventListener("change", () => {
      quantityInput.disabled = !checkbox.checked;
      if (!checkbox.checked) quantityInput.value = 1;
    });

    container.appendChild(checkbox);
    container.appendChild(label);
    container.appendChild(quantityInput);
    premToppingsDiv.appendChild(container);
  });
}

function populateDrinkForm() {
  const drinkFlavorSelect = document.getElementById("drink-flavor");
  drinkFlavorSelect.innerHTML = "";
  menu.drinkFlavors.forEach(flavor => {
    const option = document.createElement("option");
    option.value = flavor;
    option.textContent = flavor;
    drinkFlavorSelect.appendChild(option);
  });
}

function populateSideForm() {
  const sideSelect = document.getElementById("side-type");
  sideSelect.innerHTML = "";
  menu.sides.forEach(side => {
    const option = document.createElement("option");
    option.value = side.name;
    option.textContent = `${side.name} ($${side.price.toFixed(2)})`;
    sideSelect.appendChild(option);
  });
}

function renderOrderSummary() {
  const summaryDiv = document.getElementById("order-summary");
  if (order.items.length + order.drinks.length + order.sides.length === 0) {
    summaryDiv.textContent = "No items in order.";
  } else {
    summaryDiv.textContent = formatOrderSummary();
  }
}

// Event Listeners

document.getElementById("new-order-btn").addEventListener("click", () => {
  resetOrder();
  showScreen("order");
});

document.getElementById("exit-btn").addEventListener("click", () => {
  showScreen("exit");
});

document.getElementById("add-item-btn").addEventListener("click", () => showScreen("add-item"));
document.getElementById("add-drink-btn").addEventListener("click", () => showScreen("add-drink"));
document.getElementById("add-side-btn").addEventListener("click", () => showScreen("add-side"));

document.getElementById("cancel-order-btn").addEventListener("click", () => {
  if (confirm("Cancel current order?")) {
    resetOrder();
    showScreen("home");
  }
});

document.getElementById("back-to-order-from-item").addEventListener("click", () => showScreen("order"));
document.getElementById("back-to-order-from-drink").addEventListener("click", () => showScreen("order"));
document.getElementById("back-to-order-from-side").addEventListener("click", () => showScreen("order"));

document.getElementById("item-form").addEventListener("submit", e => {
  e.preventDefault();
  const form = e.target;
  const type = form["item-type"].value;
  const size = form["item-size"].value;
  const specialOption = form["special-option"].value.trim();

  const basePrice = menu.itemTypes.find(i => i.name === type).basePrice;

  // Gather toppings
  const toppings = [];

  // Regular toppings
  form.querySelectorAll("#regular-toppings input[type=checkbox]").forEach(cb => {
    if (cb.checked) {
      toppings.push({ name: cb.value, type: "regular", quantity: 1, price: 0 });
    }
  });

  // Premium toppings
  const premContainers = form.querySelectorAll("#premium-toppings .topping-option");
  premContainers.forEach(div => {
    const cb = div.querySelector("input[type=checkbox]");
    const qtyInput = div.querySelector("input[type=number]");
    if (cb.checked) {
      const toppingInfo = menu.premiumToppings.find(t => t.name === cb.value);
      const quantity = parseInt(qtyInput.value) || 1;
      toppings.push({ name: cb.value, type: "premium", quantity, price: toppingInfo.price });
    }
  });

  order.items.push({ type, size, specialOption, toppings, basePrice });
  alert("Item added to order.");
  form.reset();
  showScreen("order");
});

document.getElementById("drink-form").addEventListener("submit", e => {
  e.preventDefault();
  const form = e.target;
  const flavor = form["drink-flavor"].value;
  const size = form["drink-size"].value;

  order.drinks.push({ flavor, size });
  alert("Drink added to order.");
  form.reset();
  showScreen("order");
});

document.getElementById("side-form").addEventListener("submit", e => {
  e.preventDefault();
  const form = e.target;
  const name = form["side-type"].value;
  const sideData = menu.sides.find(s => s.name === name);

  order.sides.push({ name, price: sideData.price });
  alert("Side added to order.");
  form.reset();
  showScreen("order");
});

document.getElementById("checkout-btn").addEventListener("click", () => {
  if (
    order.items.length === 0 &&
    order.drinks.length === 0 &&
    order.sides.length === 0
  ) {
    alert("Your order is empty! Add items, drinks, or sides before checkout.");
    return;
  }
  if (order.items.length === 0 && order.sides.length === 0 && order.drinks.length === 0) {
    alert("You must order at least one item or a side/drink.");
    return;
  }
  showScreen("checkout");
});

document.getElementById("cancel-checkout-btn").addEventListener("click", () => {
  if (confirm("Cancel checkout and go back to order?")) {
    showScreen("order");
  }
});

document.getElementById("confirm-order-btn").addEventListener("click", () => {
  downloadReceipt();
  alert("Order confirmed! Receipt downloaded.");
  resetOrder();
  showScreen("home");
});

// Start app

showScreen("home");