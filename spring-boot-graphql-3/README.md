# Spring-Boot and GraphQL
This is an example with Java and GraphQL

##  Query

```graphql
# All available queries on this graphql server
type Query{
    banckAccount(id: ID) : BankAccount
}
```

type **BankAccount**
```graphql
type BankAccount {
    id: ID!
    name: String!
    currency: Currency!
}
```

type enum **Currency**
```graphql
enum Currency {
    PESOS
    USD
}
```

Run code and type:

```sh
http://localhost:8085/gui
```

query:
```graphql
{
  bankAccount(id: "1234") {
    id
    name
    currency
  }
}
```

result:
```json
{
  "data": {
    "bankAccount": {
      "id": "1234",
      "name": "Alejo",
      "currency": "PESOS"
    }
  }
}
```