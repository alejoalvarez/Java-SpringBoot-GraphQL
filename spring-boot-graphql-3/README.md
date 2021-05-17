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
    client: Client!
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

type **Client**
````graphql
type Client {
    id: ID!
    firstName: String!
    middleNames: [String!]
    lastName: String!
}
````

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