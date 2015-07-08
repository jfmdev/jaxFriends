API documentation
=================

The services provided by **jaxFriends** are divided in four groups: _session_, _friends_, _groups_ and _users_, whose specific services are detailed below.

All responses are returned as JSON objects using the following format:

```javascript
{
    status: 'success',
    data: { /* ... */ }
}
```

The `status` attribute can hold the values _success_ or _error_. If the status is _error_, then the `data` object is going to hold the attributes `code` and `message`:

```javascript
{
    status: 'error',
    data: {
        code: 'InvalidParameters',
        message: 'The name is a required value'
    }
}
```

Session
------

This services are related to the login and logout operations within the system.

### **POST** `rest/session/login`

Authenticates the user into the system.

#### Parameters:

Type  | Name     | Values
----- | -------- |--------
POST  | username | string
POST  | password | string

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```


### **GET** `rest/session/logout`

Log out the user from the system.

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

### **PUT** `rest/session/update`

Updates the username and/or password of the logged user.

#### Parameters:

Type  | Name     | Values
----- | -------- |--------
POST  | username | string
POST  | password | string

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

Groups
------

This services are related to the administrator of groups, in which friends can be organized.


### **GET** `rest/groups`

Get the list of groups that belongs to the user.

#### Response (example):

```javascript
{
    status: 'success',
    data: [
        {id: 1, name: 'Friends'},
        {id: 2, name: 'Work'}
    ]
}
```

### **GET** `rest/groups/:id`

Get the group whose id is equal to ":id".

#### Response (example):

```javascript
{
    status: 'success',
    data: {
        id: 1, 
        name: 'Friends'
    }
}
```

### **POST** `rest/users/:id`

Creates a new group.

#### Parameters:

Type  | Name | Values
----- | ---- |-------
POST  | name | string

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

### **PUT** `rest/groups/:id`

Updates an existing group.

#### Parameters:

Type  | Name | Values
----- | ---- |-------
POST  | name | string

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

### **DELETE** `rest/groups/:id`

Deletes an existing group.

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

Friends
-------

This services are related to the administrator of an user's friends.


### **GET** `rest/friends`

Get the list of user's friends.

#### Response (example):

```javascript
{
    status: 'success',
    data: [
        {id: 1, firstname: 'John', lastname: 'Smith', telephone: '555-1234', email: 'john@smith.com', group: {id: 2}},
        {id: 2, firstname: 'Pancho', lastname: 'Lopez', telephone: null, email: 'pancho@lopez.com', group: {id: 3}}
    ]
}
```

### **GET** `rest/friends/:id`

Get the friend whose id is equal to ":id".

#### Response (example):

```javascript
{
    status: 'success',
    data: {
        id: 1, 
        firstname: 'John', 
        lastname: 'Smith', 
        telephone: '555-1234', 
        email: 'john@smith.com', 
        group: {id: 2}
    }
}
```

### **POST** `rest/friends/:id`

Creates a new friend.

#### Parameters:

Type  | Name       | Values
----- | ---------- |--------
POST  | group_id   | integer
POST  | first_name | string
POST  | last_name  | string
POST  | telephone  | string
POST  | email      | boolean

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

### **PUT** `rest/friends/:id`

Updates an existing friend.

#### Parameters:

Type  | Name       | Values
----- | ---------- |--------
POST  | group_id   | integer
POST  | first_name | string
POST  | last_name  | string
POST  | telephone  | string
POST  | email      | boolean

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

### **DELETE** `rest/friends/:id`

Deletes an existing friend.

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

Users
-----

This services are related to the administrations of user's accounts. Note that only users with administrator privileges can consume this services.

### **GET** `rest/users`

Get the list of users.

#### Response (example):

```javascript
{
    status: 'success',
    data: [
        {id: 1, username: 'john', password: 'a51dda7c7ff50b61eaea0444371f4a6a9301e501', admin: true},
        {id: 2, username: 'jane', password: '8a8deed44623d4c44268c26652d80945851c4f7f', admin: false}
    ]
}
```

### **GET** `rest/users/:id`

Get the user whose id is equal to ":id".

#### Response (example):

```javascript
{
    status: 'success',
    data: {
        id: 1, 
        username: 'john', 
        password: 'a51dda7c7ff50b61eaea0444371f4a6a9301e501', 
        admin: true
    }
}
```

### **POST** `rest/users/:id`

Creates a new user.

#### Parameters:

Type  | Name     | Values
----- | -------- |--------
POST  | username | string
POST  | password | string
POST  | admin    | boolean

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

### **PUT** `rest/users/:id`

Updates an existing user.

#### Parameters:

Type  | Name     | Values
----- | -------- |--------
POST  | username | string
POST  | password | string
POST  | admin    | boolean

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}
```

### **DELETE** `rest/users/:id`

Deletes an existing user.

#### Response (example):

```javascript
{
    status: 'success',
    data: null
}