# Class Diagram — Soul Food POS

The repo README must include the class diagram (spec requirement). Embed this Mermaid in your README — GitHub renders it natively. Export a PNG too via <https://mermaid.live>.

```mermaid
classDiagram
    direction LR

    class Program {
        +main(args: String[])
    }

    class UserInterface {
        -order: Order
        +display()
        -homeScreen()
        -orderScreen()
        -addItem()
        -addDrink()
        -addMainSide()
        -checkout()
    }

    class Order {
        -timestamp: LocalDateTime
        -items: List~OrderItem~
        +add(item: OrderItem)
        +getItems(): List~OrderItem~
        +getTotal(): double
        +isEmptyOfPlates(): boolean
        +hasDrinkOrSide(): boolean
    }

    class OrderItem {
        <<interface>>
        +getPrice(): double
        +getDescription(): String
    }

    class Item {
        <<abstract>>
        #size: Size
        #type: String
        #toppings: List~Topping~
        #special: boolean
        +addTopping(t: Topping)
        +getPrice()* double
        +getDescription()* String
    }

    class SoulFoodPlate {
        +getPrice(): double
        +getDescription(): String
    }

    class Size {
        <<enum>>
        SMALL
        MEDIUM
        LARGE
    }

    class Topping {
        <<interface>>
        +getName(): String
        +priceFor(size: Size): double
    }

    class Meat {
        -name: String
        -extra: boolean
        +priceFor(size: Size): double
    }

    class PremiumTopping {
        -name: String
        -extra: boolean
        +priceFor(size: Size): double
    }

    class RegularTopping {
        -name: String
        +priceFor(size: Size): double
    }

    class Condiment {
        -name: String
        +priceFor(size: Size): double
    }

    class IncludedSide {
        -name: String
        +priceFor(size: Size): double
    }

    class Drink {
        -size: Size
        -flavor: String
        +getPrice(): double
        +getDescription(): String
    }

    class MainSide {
        -name: String
        +getPrice(): double
        +getDescription(): String
    }

    class ReceiptWriter {
        +write(order: Order, shopName: String): Path
    }

    Program --> UserInterface
    UserInterface --> Order
    UserInterface --> ReceiptWriter
    Order o-- OrderItem
    Item ..|> OrderItem
    SoulFoodPlate --|> Item
    Drink ..|> OrderItem
    MainSide ..|> OrderItem
    Item o-- Topping
    Item --> Size
    Meat ..|> Topping
    PremiumTopping ..|> Topping
    RegularTopping ..|> Topping
    Condiment ..|> Topping
    IncludedSide ..|> Topping
    Drink --> Size
```

## Why this design

- **`OrderItem` interface** — lets `Order` hold a mixed list of plates, drinks, and main sides under one type. Loose coupling.
- **`Item` abstract class** — pulls common state (size/type/toppings) out of `SoulFoodPlate`. Sets you up for **Phase 6 bonus** `SignaturePlate extends SoulFoodPlate`.
- **`Topping` interface w/ 5 implementations** — pricing rule lives inside each topping class (polymorphism). `Item.getPrice()` just iterates.
- **`Size` enum** — type-safe size carrying base price per size for plates, drinks, etc. via a method.
- **`ReceiptWriter` separated** — UI does not write files directly; matches the dealership `DealershipFileManager` / `ContractFileManager` split from Workbook 5w.
