# Fluent Result

A result library helping you get rid of exceptions, enabling a more fluent coding style.

## Motivation

Programming with exceptions can be both tedious and error-prone. Checked exceptions gives much boilerplate code, while unchecked exceptions can be the source of errors if they are let loose in the system without sufficient handling. 
 
Exceptions should not be used for control-flow, but in the heat of the moment it is often faster to just throw an IllegalArgumentException than encapsulating the error in the returned class. This result library has done the encapsulation of the error for you, making your control flow more fluent and exception-free.

The introduction of Optional was a great step forward for Java, but as a class it does not make it any easier to return and handle error situations. The Result-class from this library is inspired from the Optional-class providing much of the same functionality, enabling utilization of chaining and lambdas for a more fluent style of programming.
 
## Usage

Make methods that can give an error return a `Result<T, E>` where T is the type of the return value when the method is successful, and E is the type of error value when the method is not successful.

```
public Result<Customer, String> getCustomer(String id) {
    try {
        Customer customer = service.getCustomer(id);
        return Result.success(customer);
    } catch (RuntimeException ex) {
        return Result.error(ex.getMessage());
    }
}
```

Now the code can be used like this:
```
Result<Customer, String> customerResult = getCustomer(id);
```
However the full benefit of using the result library is more visible when using chaining, and utilizing other methods that return Result values.


```

public Result<Account, String> findAccount(Customer customer, String accountId) {
... 
}

public void logAccountInfo(Account account) {
    ...
}

public void logError(String customerId, String accountId, String errorMsg) {
    ...
}

```

Now the above methods can be called by chaining the calls.

```
public long getBalance(String customerId, String accountId) {
    return getCustomer(customerId)
        .map(Customer::getAccounts)
        .flatMap(customer -> findAccount(customer, accountId)
        .consume(this::logAccountInfo)
        .map(Account::getBalance)
        .consumeError(errMsg -> logError(customerId, accountId, errMsg))
        .orElse(0);
}
```

In the above chain the methods _map_, _flatMap_ and _consume_ is invoked only if there is an actual success value in the Result, and not if an error were returned somewhere in the chain. The method _consumeError_ is only invoked if an error is present.


Fluent Result also provides Result-classes for some normal result types. This is the classes _OptionalResult_, _BooleanResult_ and _VoidResult_. 

```
public OptionalResult<Movie, String> findMovie(String title) {
    try {
        Movie movie = service.getMovie(title);
        return OptionalResult.successNullable(movie);
    } catch (RuntimeException ex) {
        return Result.error(ex.getMessage());
    }
}
```

All result types have methods for mapping between to other result types.

```
public BooleanResult<String> isUnderAge(String customerId) {
    return getCustomer(customerId)
        .mapToBoolean(customer -> customer.age >= 18)
        .runIfFalse(() -> LOGGER.warn("Customer is underage"));
}
```

## API

### Result Classes
 
#### `Result<T, E>`

The Result-class may either have a non-null success value or an non-null error value.

##### Static factory methods:

###### `success(T value)`

- Argument(s)
  - The success value for the result. May not be null.
- Returns
  - A new Result containing the success value

####### `error(E error)`

- Argument(s)
  - The error value for the result. May not be null.
- Returns
  - A new Result containing the error value

#### `OptionalResult<T, E>`

The OptionalResult-class may either have a non-null success value, be empty, or have an non-null error value.

##### Static factory methods:

###### `success(T value)`

- Argument(s)
  - The success value for the result. May not be null.
- Returns
  - A new OptionalResult containing the success value

###### `success(Optional<T> maybeValue)`

- Argument(s)
  - The optional success value for the result. The optional itself should not be null.
- Returns
  - A new OptionalResult containing the success value if present in the optional
  - A new empty OptionalResult if the provided Optional is empty

###### `successNullable(T value)`

- Argument(s)
  - The success value for the result. May be null.
- Returns
  - A new OptionalResult containing the success value if not null
  - A new empty OptionalResult if the value is null

###### `empty()`

- No qrguments
- Returns
  - A new empty OptionalResult

###### `error(E error)`

- Argument(s)
  - The error value for the result. May not be null.
- Returns
  - A new OptionalResult containing the error value

#### `BooleanResult<E>`

The BooleanResult-class may either have a non-null boolean success value, or have an non-null error value.

##### Static factory methods:

###### `success(boolean value)`

- Argument(s)
  - The success boolean value for the result. May not be null.
- Returns
  - A new BoolenResult containing the success value

###### `successTrue()`

- No arguments
- Returns
  - A new BooleanResult containing true as the success value

###### `successFalse()`

- No arguments
- Returns
  - A new BooleanResult containing false as the success value

###### `error(E error)`

- Argument(s)
  - The error value for the result. May not be null.
- Returns
  - A new BooleanResult containing the error value

#### `VoidResult<E>`

The VoidResult-class may either be in success state without a value, or have an non-null error value.

##### Static factory methods:

###### `success()`

- No arguments
- Returns
  - A new VoidResult in success state

###### `error(E error)`

- Argument(s)
  - The error value for the result. May not be null.
- Returns
  - A new VoidResult containing the error value

### Instance methods

#### Map methods

The map methods maps the value of the result to another value, resulting in a new result instance containing the new value. If the result already contained an error, no mapping takes place and the original result instance is returned unaltered.

These methods are not present in VoidResult which does not contain a success value.

##### `map`

Maps the value of the result to another value.

- Argument(s)
  - A function mapping from T to N
- Returns
  - A new `Result<N, E>`
- Notes
  - Not present in VoidResult
  
##### `mapToOptional`

Maps the value of the result to another optional value.

- Argument(s)
  - A function mapping from T to Optional<N>
- Returns
  - A new `OptionalResult<N, E>`
  
##### `mapToBoolean` 

Maps the value of the result to another boolean value.

- Argument(s)
  - A function mapping from T to Boolean
- Returns
  - A new `BooleanResult<E>`

#### FlatMap methods

The flatMap methods maps the value of the result to a new Result-class instance. If the result already contained an error, the flatMap function is not run, and the new Result-class instance contains the original result error.

These methods are not present in VoidResult which does not contain a success value.

##### `flatMap` 

Maps the value of the result to a new Result.

- Argument(s)
  - A function mapping from T to Result<N, E>
- Returns
  - A new `Result<N, E>`

##### `flatMapToOptionalResult` 

Maps the value of the result to a new OptionalResult.

- Argument(s)
  - A function mapping from T to OptionalResult<N, E>
- Returns
  - A new `OptionalResult<N, E>`

##### `flatMapToBooleanResult` 

Maps the value of the result to a newBooleanResult.
- Argument(s)
  - A function mapping from T to BooleanResult<E>
- Returns
  - A new `BooleanResult<E>`

##### `flatMapToVoidResult`

Maps the value of the result to a new VoidResult. This method is not present in VoidResult. 

- Argument(s)
  - A function mapping from T to VoidResult<E>
- Returns
  - A new `VoidResult<E>`

#### Consume Methods

The consume methods uses the values contained values in the result instance, and returns the result instance unaltered.

##### `consume` 

Consumes the success value of the result class. This method is not present in VoidResult.

- Argument(s)
  - A consumer accepting the success value of the result
- Returns
  - The result unaltered
  
- Notes 
  - For OptionalResult this method consumes an Optional. Also provided in OptionalResult are a method consumeValue which consumes the actual value, and is not run if OptionalResult is empty.

##### `consumeError` 

Consumes the error value of the result class.

- Argument(s)
  - A consumer accepting the error value of the result
- Returns
  - The result unaltered

##### `consumeEither` 

Consumes both the success value and the error value of the result class.

- Argument(s)
  - A consumer accepting the success value of the result
  - A consumer accepting the error value of the result
- Returns
  - The result unaltered

- Notes 
  - OptionalResult also contains a three argument version of this method where the first argument is a consumer accepting the success value, the second argument is a runnable to run if the OptionalResult is empty and the third is a consumer accepting the error value.
  - BooleanResult also contains a three argument version of this method where the first argument is a runnable to run when the success value is true, the second argument is a runnable to run if the success value is false and the third is a consumer accepting the error value.
  - VoidResult contains a variation of this method where the first argument is a runnable to run if the VoidResult is in success state.
  
#### Run methods

The run methods are run in the prerequisite in the method name is met, and returns the result instance unaltered.

##### `runIfSuccess` 

The runnable is run if the result is in the success state.

- Argument(s)
  - A runnable which are run if the result is a success
- Returns
  - The result unaltered
- Notes
  - For OptionalResult this method is run both when it has a success value and when it is empty, as both translates to a success state. 
  - Also provided in OptionalResult are a method runIfValue which is only run when there is an actual success value.
  - Also provided in OptionalResult are a method runIfEmpty which is only run if the OptionalResult is empty.
  - For BooleanResult this method is run when the success value is both true or false, as both translates to a success state. 
  - Also provided in BooleanResult are a method runIfTrue which is only run when the success value is true.
  - Also provided in BooleanResult are a method runIfFalse which is only run when the success value is false.

##### `runIfError` 

The runnable is run if the result is in the error state.

- Argument(s)
  - A runnable which are run if the result is an error
- Returns
  - The result unaltered

##### `runIfEither` 

A method which accepts runnables for all the states of the Result-class.

- Argument(s)
  - A runnable which are run if the result is a success
  - A runnable which are run if the result is an error
- Returns
  - The result unaltered
- Notes
    - OptionalResult also contains a three argument version of this method where the first argument is a runnable to run if has a success value, the second argument is a runnable to run if the OptionalResult is empty and the third is a runnable to run if the result is an error.
    - BooleanResult also contains a three argument version of this method where the first argument is a runnable to run when the success value is true, the second argument is a runnable to run if the success value is false and the third is a runnable to run if the result is an error.

##### `run` 

The runnable is run no matter what state the Result-class has.

- Argument(s)
  - A runnable which are always run, both in the case of success state or error state
- Returns
  - The result unaltered

##### `verify` 

A method which can verify the current success value of the Result-class. If the verification fails, the returned result will contain the supplied error value.

- Argument(s)
  - A predicate which tests the success value of the result
  - A supplier for the error value if the predicate fails
- Returns
  - The result unaltered if the predicate evaluates to true
  - A new result containing the supplied error if the predicate evaluates to false
  - The result unaltered if it already contained an error
- Notes
  - For OptionalResult the predicate tests an Optional. Also provided in OptionalResult are a method verifyValue which tests the actual value, and is not run if OptionalResult is empty.

##### `merge` 

A method to use when you want to merge the result, no matter if success or error, into an actual non-result-wrapped value.

- Argument(s)
  - A function mapping from the success value to a new type N
  - A function mapping from the error value to a new type N
- Returns
  - The value mapped from either the success or error value
- Notes 
  - OptionalResult also contains a three argument version of this method where the first argument is a function mapping the success value, the second argument is a supplier to provide the value if the OptionalResult is empty and the third is a function mapping the error value.
  - BooleanResult also contains a three argument version of this method where the first argument is a supplier providing a value when the success value is true, the second argument is a supplier providing a value if the success value is false and the third is a function mapping the error value.
  - VoidResult contains a variation of this method where the first argument is a runnable to run if the VoidResult is in success state.
  

##### `orElse` 

Returns the success value of the result, or the provided argument value if the result is in error state.

- Argument(s)
  - A value to return if no success value is present
- Returns
  - The success value of the result, otherwise the argument value
- Notes:
  - Not present in VoidResult
  - OptionalResult has a variation of this method `valueOrElse` which returns the argument value if the OptionalResult both is empty or contain an error.

##### `orElseGet` 

Returns the success value of the result, or the supplied value from the provided argument supplier if the result is in error state.

- Argument(s)
  - A supplier providing the value to return if no success value is present
- Returns
  - The success value of the result, otherwise the value provided by the argument supplier
- Notes
  - Not present in VoidResult
  - OptionalResult has a variation of this method `valueOrElseGet` returns the value provided by the argument supplier if the OptionalResult both is empty or contain an error.

##### `orElseThrow`

Returns the success value of the result, or throws the supplied exception from the provided argument supplier if the result is in error state.

- Argument(s)
  - A supplier providing the exception to throw if no success value is present
- Returns
  - The success value of the result
  - Otherwise the exception provided by the argument supplier is thrown
- Notes
  - In VoidResult this is a void-method.
  - OptionalResult has a variation of this method `valueOrElseThrow` which throws the supplied exception if the OptionalResult both is empty or contain an error.

##### `toOptionalResult`

Returns an OptionalResult containing either the success value or error value of the original result.

- No arguments
- Returns
  - An OptionalResult containing either the success value or the error value from the original result.
- Notes
  - If the original result is a VoidResult, then the OptionalResult will either be empty or contain the error value from the VoidResult.
  - Not present in OptionalResult
  
##### `toVoidResult` 

Returns an VoidResult either in success state or containing the error value of the original result.

  - No arguments
  - Returns
    - A VoidResult in success state or containing the error value from the original result.
  - Notes
    - Not present in VoidResult
  
##### `toResult` 

A method present in OptionalResult for creating a Result from the OptionalResult success value.

  - Argument(s)
    - A supplier providing the error value to create a Result with if the OptionalResult is empty.
  - Returns
    - A Result containing the success value of the OptionalResult, or an error if the OptionalResult was empty or contained error.
  - Notes
    - Only present in OptionalResult






