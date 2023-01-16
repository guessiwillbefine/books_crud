## Assignment 5
## Task : 
Обрати предметну область на свій розсуд. Книги, Товари, Клієнти, Туризм, або ін. В рамках
предметної області повинно бути 2 сутності, що співвідносяться одне до одного як багато-до-одного 
(Сутність 1 *-- Сутність 2, наприклад, Товар *-- Категорія).
Змоделювати цю предметну область в реляційній БД: створити відповідні таблиці, скрипт створення
додати до вихідних кодів проекту.
Розробити бекенд web-застосунку з REST API, який би дозволяв виконувати такі функції з цими сутностями:
- створювати, читати, оновляти і видаляти записи Сутності 1
- робити пошук елементів Сутності 1 по 2-х полях і з постраничником
- повертати повний список елементів Сутності 2
- Додати інтеграційні тести для всіх REST-endpoints.

## Start
To start the project you need to create an empty MySql database with schema name "crud_db". 
There is a .mwb file in /src/main/resources with correct schema. Or you can specify you own database,
but for this you need to change an url in src/main/resources/application.yaml for dev profile.
If you do all right liquibase migration file will generate a new database with two entities : 
#### Book *--- Author

# Endpoints

# Authors

- ### (GET) /authors
Endpoint for finding author by his name and surname. 

Example:
```http request 
http://localhost:8080//authors?name=steven&surname=king 
```
Response body:
```json
{
  "id": 9,
  "name": "Steven",
  "surname": "King",
  "age": 75
}
```
This request is case-insensitive, so you can write "Steven" or "steven", both are correct
- ## (GET) authors/{id}
Method to find book by its Id int DB. Example :
### Request:
```http request
http://localhost:8080//books/3
```
### Response
```json
{
    "id": 3,
    "name": "Chuck",
    "surname": "Palahniuk",
    "age": 60
}

```
- ## (GET) authors/all
Endpoint will return a list of all existing authors

- ## (POST) authors/create
Endpoint for creating new authors. Request have valid body with author data:
```json
{
    "name" : "newAuthor",
    "surname" : "newAuthorSurname",
    "age" : "21"
}
```
- ## (DELETE) authors/delete
Request for deleting authors. Must contain body with name and surname of author:
```json
{
    "name" : "newAuthor",
    "surname" : "newAuthorSurname"
}
```
- ## (Patch) authors/delete
Updating authors entity. Request must have new author's data and his id : 
```json
{
    "id" : 20,
    "name" : "newAuthor2",
    "surname" : "newAuthorSurname2"
}
```
# Books

- ### (GET) /books
Requires body, contains name of book and its author :
```json
{
  "name": "The Name of Rose",
  "author" : {
      "name" : "Umberto",
      "surname" : "Eco"
  }
}
```
- ### (GET) /books/{id}
Method to find book by its Id int DB. Example :

```http request
http://localhost:8080/books/5
```

```json
{
    "id": 5,
    "year": 1994,
    "pageAmount": 624,
    "name": "Chronicle Wind Up Bird",
    "description": "Japan's most highly regarded novelist now vaults into the first ranks of international fiction writers with this heroically imaginative novel, which is at once a detective story, an account of a disintegrating marriage, and an excavation of the buried secrets of World War II.",
    "author": {
        "id": 2,
        "name": "Haruki",
        "surname": "Murakami",
        "age": 73
    }
}
```

- ### (GET) /books/all
has two params:
- size - size of list that wll pe returned
- page - page of elements (starts from 0)
if one of these params won't be present, api will use its own default values (page size will be 15, page 0 will be returned)

### example:
#### ``` http://localhost:8080//books/all?size=3&page=1 ```

- ### (POST) /books/create
Endpoint for creating new book in DB. Requires body with book data. Example:
```json
{
  "name" : "new book",
  "year" : 2022,
  "description" : "very long description for your book (max 500 symbols)",
  "pageAmount" : 120,
  "author" : {
    "name" : "Steven",
    "surname" : "King"
  }
}
```
### (DELETE) /books
Has the same body as create method, deletes book if it is present in database

### (GET) /books/search
Method can be used for searching, using certain fields and their values.
With request body like this:
```json
{
  "params": [
    {
      "name": "year",
      "value": "1980"
    },
    {
      "name": "name",
      "value": "The Name of Rose"
    }
  ]
}
```
method will generate SQL query like this:
```sql
select * from Book where 'year' = '1980' and 'name' = 'The Name of Rose';
```
Amount of params doesn't have any limits, but must be greater than 0.

- ### (PATCH) /books/update
Endpoint for updating books in DB. Requires body with book data (Id value must be present). Example:
```json
{
  "id" : "123",
  "name" : "new book",
  "year" : 2022,
  "description" : "very long description for your book (max 500 symbols)",
  "pageAmount" : 120,
  "author" : {
    "name" : "Steven",
    "surname" : "King"
  }
}
```
return 200_OK if updating is done correctly.

# Possible Errors:

- ## AuthorDuplicateException and BookDuplicateException
after attempt to add one book or author two times, this action will produce 400_BAD_REQUEST:
```json
{
  "response" : "Book[bookname] is already exists"
}
```
```json
{
  "response" : "Author[name surname] is already exists"
}
```

- ## AuthorNotFoundException and BookNotFoundException
if entities wasn't found in DB for some reason you will have next response: 
```json
{
  "response" : "Book[bookname] wasn't found"
}
```
```json
{
  "response" : "Author[name surname] wasn't found"
}
```
- ## AuthorValidationException and BookValidationException
if entities in request body doesn't have valid data you will have a response with list of errors:
```json
{
  "response": "Field error in object 'authorDto' on field 'surname': rejected value [newAuthorNotValidSurname]; default message [author's surname must be between 1 and 20]]"
}
```
- ## Semantic Exception
while searching for Book entity you may write field name incorrectly, it will produce next response:
```json
{
    "response": "Could not resolve attribute 'yearr' of 'assignment_five.entity.Book'"
}
```