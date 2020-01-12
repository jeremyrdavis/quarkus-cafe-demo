# HTTP

## Create Orders

### 2 Beverages
```json
{
    "beverages": [
        {
            "item": "COFFEE_WITH_ROOM",
            "itemId": "d141c73c-ac62-4534-b5db-7c989040fecb",
            "name": "Mickey"
        },
        {
            "item": "COFFEE_BLACK",
            "itemId": "d141c73c-ac62-4534-b5db-7c989040fecc",
            "name": "Minnie"
        }
    ],
    "id": "d141c73c-ac62-4534-b5db-7c989040feca"
}
```
```json
{
    "item": "CAKEPOP",
    "itemId": "c3d154ba-13e8-4e3c-acee-c63df90f34aa",
    "name": "Goofy",
    "orderId": "1e435117-1649-42f9-9a5f-2fe71854e56c"
}
```
```json
{
    "beverages": [
        {
            "item": "COFFEE_WITH_ROOM",
            "name": "Mickey"
        },
        {
            "item": "COFFEE_BLACK",
            "name": "Minnie"
        }
    ],
    "kitchenOrders": [
        {
            "item": "CAKEPOP",
            "name": "Mickey"
        },
        {
            "item": "CROISSANT",
            "name": "Minnie"
        }
    ]
}
```

Kitchen Order Only
```json
{"eventType":"KITCHEN_ORDER_IN","item":"MUFFIN","itemId":"4e3e194f-961a-4a02-923b-26704cf30097","name":"Laurel","orderId":"6593f77c-8d36-4570-8b27-a0bccacf0bfb"}
```
