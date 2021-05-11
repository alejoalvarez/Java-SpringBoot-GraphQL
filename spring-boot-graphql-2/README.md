# Spring-Boot and GraphQL
This is an example with Java and GraphQL

## Dependencies gradle:
- **Web**
- **JPA**
- **graphql**
- **Lombok**
- **h2Database**

## Steps for run the application and test

- Run the java application
- open your browser to http:/localhost:8086/gui and run this example

```sh
{
  foods {
    id
    name
  }
}
```

Another example

```sh
{ food(id: 1) { name } }
```

Other example for create a new food using the saveFood() mutation:

```sh
mutation {
  saveFood(food: { name: "Pasta" }) {
    id
    isGood
  }
}
```
