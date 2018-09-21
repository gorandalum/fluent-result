[![Build Status](https://travis-ci.com/gorandalum/fluent-result.svg?branch=master)](https://travis-ci.com/gorandalum/fluent-result)

# Fluent Result

A result library helping you get rid of exceptions, enabling a more fluent coding style.

- [Motivation](#motivation)
- [Usage](#usage)
- [API](#api)
  - [Result classes](#result-classes)
  - [Instance methods](#instance-methods)

## Motivation

Programming with exceptions can be both tedious and error-prone. Checked exceptions gives much boilerplate code, while unchecked exceptions can be the source of errors if they are let loose in the system without sufficient handling. 
 
Also exceptions should not be used for control-flow, but in the heat of the moment it is often faster to just throw an IllegalArgumentException than encapsulating the error in the returned class. This result library has done the encapsulation of the error for you, making your control flow more fluent and exception-free.

The introduction of Optional was a great step forward for Java, but as a class it does not make it any easier to return and handle error situations. The Result classes from this library is inspired from the Optional-class providing much of the same functionality, enabling utilization of chaining and lambdas for a more fluent style of programming.
 
## Usage

Make methods that can give an error return a `Result<T, E>` where T is the type of the return value when the method is successful, and E is the type of error value when the method is not successful.

```java
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
```java
Result<Customer, String> customerResult = getCustomer(id);
```
However the full benefit of using the result library is more visible when using chaining and utilizing other methods that return Result values.

Some other methods to use while chaining:
```java
public Result<Account, String> findAccount(List<Account> accounts, String accountId)

public void logAccountInfo(Account account)

public void logError(String customerId, String accountId, String errorMsg)
```

Now the above methods can be called by chaining the calls.

```java
public BigDecimal getBalance(String customerId, String accountId) {
    return getCustomer(customerId)
        .map(Customer::getAccounts)
        .flatMap(accounts -> findAccount(accounts, accountId))
        .consume(this::logAccountInfo)
        .map(Account::getBalance)
        .consumeError(errMsg -> logError(customerId, accountId, errMsg))
        .orElse(BigDecimal.ZERO);
}
```

In the above chain the methods `map`, `flatMap` and `consume` is invoked only if there is an actual success value in the Result, and not if an error were returned somewhere in the chain. The method `consumeError` is only invoked if an error is present.

Fluent Result also provides Result classes for some other normal result types. This is the classes _OptionalResult_, _BooleanResult_ and _VoidResult_. These classes provide some additional methods relevant for their type, like `runIfEmpty` on _OptionalResult_ and `runIfTrue` on _BooleanResult_. Or they remove some unnecessary methods like the `map`/`flatMap` methods on _VoidResult_.

Example of a method returning an _OptionalResult_: 
```java
public OptionalResult<Movie, String> findMovie(String title) {
    try {
        Movie movie = service.getMovie(title); // May be null
        return OptionalResult.successNullable(movie);
    } catch (RuntimeException ex) {
        return Result.error(ex.getMessage());
    }
}
```

All Result classes have methods for mapping to the other Result classes.

Example of mapping from a _Result_ to a _BooleanResult_ by using the method `mapToBoolean`:

```java
public BooleanResult<String> isUnderAge(String customerId) {
    return getCustomer(customerId)
        .mapToBoolean(customer -> customer.getAge() >= 18)
        .runIfFalse(() -> LOGGER.warn("Customer is underage"));
}
```

The Result classes does not have methods like `get`, `isPresent` or `isSuccess`. We have seen these kind of methods misused on _Optional_, being the source of errors, or they may be used as an easy way out when the programmer don't want to program in a functional style.

Example of code we do not want:
```java
Result<Customer, String> customerResult = getCustomer(id);
if (customerResult.isSuccess()) {
    doSomethingWithCustomer(customerResult.get());
} else {
    logAnError(customerResult.getError());
}
```

Instead it should be implemented like this:
```java
getCustomer(id).consumeEither(
    this::doSomethingWithCustomer,
    this::logAnError);
```

## API

### Result Classes
 
#### `Result<T, E>`

The _Result_ class may either be in the success state with a non-null success value or be in the error state with a non-null error value.

##### Static factory methods

###### `success(T value)`

- Argument(s)
  - The success value for the result. May not be null.
- Returns
  - A _Result_ containing the success value.

###### `error(E error)`

- Argument(s)
  - The error value for the result. May not be null.
- Returns
  - A _Result_ containing the error value.

#### `OptionalResult<T, E>`

When the _OptionalResult_ class is in the success state it can either have a non-null success value, or be empty. If it is in the error state it has an non-null error value.

##### Static factory methods

###### `success(T value)`

- Argument(s)
  - The success value for the result. May not be null.
- Returns
  - An _OptionalResult_ containing the success value.

###### `success(Optional<T> maybeValue)`

- Argument(s)
  - The optional success value for the result. The optional itself should not be null.
- Returns
  - A _OptionalResult_ containing the success value if present in the _Optional_.
  - An empty _OptionalResult_ if the provided _Optional_ is empty.

###### `successNullable(T value)`

- Argument(s)
  - The success value for the result. May be null.
- Returns
  - A _OptionalResult_ containing the success value if not null.
  - An empty _OptionalResult_ if the value is null.

###### `empty()`

- No arguments
- Returns
  - An empty _OptionalResult_.

###### `error(E error)`

- Argument(s)
  - The error value for the result. May not be null.
- Returns
  - An _OptionalResult_ containing the error value.

#### `BooleanResult<E>`

The _BooleanResult_ class may either be in success state and have a non-null boolean success value, or be in error state and have a non-null error value.

##### Static factory methods

###### `success(boolean value)`

- Argument(s)
  - The success boolean value for the result. May not be null.
- Returns
  - A _BooleanResult_ containing the success value.

###### `successTrue()`

- No arguments
- Returns
  - A _BooleanResult_ containing true as the success value.

###### `successFalse()`

- No arguments
- Returns
  - A _BooleanResult_ containing false as the success value.

###### `error(E error)`

- Argument(s)
  - The error value for the result. May not be null.
- Returns
  - A _BooleanResult_ containing the error value.

#### `VoidResult<E>`

The _VoidResult_ class may either be in success state without a value, or be in error state and have a non-null error value.

##### Static factory methods

###### `success()`

- No arguments
- Returns
  - A _VoidResult_ in success state.

###### `error(E error)`

- Argument(s)
  - The error value for the result. May not be null.
- Returns
  - A _VoidResult_ containing the error value.

### Instance methods

#### Map methods

The `map` methods maps the value of the result to another value, resulting in a new result instance containing the new value. If the result already contained an error, no mapping takes place and the original result instance is returned unaltered.

These methods are not present in _VoidResult_ which does not contain a success value, they are instead replaced with `replace` methods taking suppliers as arguments in stead of functions.

##### `map`

Maps the value of the result to another value.

- Argument(s)
  - A function mapping from `T` to `N`.
- Returns
  - A new `Result<N, E>`.
- Notes 
  - _VoidResult_ contains a variation of this method named `replace` which takes a supplier providing the value the new _Result_ should contain.
  - _OptionalResult_ also contains two variations of this method named `mapValue` and `mapValueToOptional` which maps the _OptionalResult_ success value.
  
##### `mapToOptional`

Maps the value of the result to another optional value.

- Argument(s)
  - A function mapping from `T` to `Optional<N>`.
- Returns
  - A new `OptionalResult<N, E>`.
  - _VoidResult_ contains a variation of this method named `replaceWithOptional` which takes a supplier providing the value the new _OptionalResult_ should contain.
  
##### `mapToBoolean` 

Maps the value of the result to another boolean value.

- Argument(s)
  - A function mapping from `T` to `Boolean`.
- Returns
  - A new `BooleanResult<E>`.
  - _VoidResult_ contains a variation of this method named `replaceWithBoolean` which takes a supplier providing the value the new _OptionalResult_ should contain.

#### FlatMap methods

The flatMap methods maps the value of the result to a new Result class instance. If the result already contained an error, the flatMap function is not run, and the new Result class instance contains the original result error.

These methods are not present in _VoidResult_ which does not contain a success value, they are instead replaced with `flatReplace` methods taking suppliers as arguments in stead of functions.

##### `flatMap` 

Maps the value of the result to a new _Result_.

- Argument(s)
  - A function mapping from `T` to `Result<N, E>`.
- Returns
  - A new `Result<N, E>`.
  - _VoidResult_ contains a variation of this method named `flatReplace` which takes a supplier providing a _Result_.
  - _OptionalResult_ also contains a variation of this method named `flatMapValueWithResult` which maps the _OptionalResult_ success value. The returned result is still an _OptionalResult_ since an empty value is left unaltered.

##### `flatMapToOptionalResult` 

Maps the value of the result to a new _OptionalResult_.

- Argument(s)
  - A function mapping from `T` to `OptionalResult<N, E>`.
- Returns
  - A new `OptionalResult<N, E>`.
  - _VoidResult_ contains a variation of this method named `flatReplaceToOptionalResult` which takes a supplier providing a _OptionalResult_.
  - _OptionalResult_ also contains a variation of this method named `flatMapValueWithOptionalResult` which maps the _OptionalResult_ success value. The returned result is still an _OptionalResult_ since an empty value is left unaltered.

##### `flatMapToBooleanResult` 

Maps the value of the result to a new _BooleanResult_.

- Argument(s)
  - A function mapping from `T` to `BooleanResult<E>`.
- Returns
  - A new `BooleanResult<E>`.
  - _VoidResult_ contains a variation of this method named `flatReplaceToBooleanResult` which takes a supplier providing a _BooleanResult_.
  - _OptionalResult_ also contains a variation of this method named `flatMapValueWithBooleanResult` which maps the _OptionalResult_ success value. The returned result is still an _OptionalResult_ since an empty value is left unaltered.

##### `flatMapToVoidResult`

Maps the value of the result to a new _VoidResult_.

- Argument(s)
  - A function mapping from `T` to `VoidResult<E>`.
- Returns
  - A new `VoidResult<E>`.
  - _VoidResult_ contains a variation of this method named `flatReplaceToVoidResult` which takes a supplier providing a _VoidResult_.

#### Consume Methods

The consume methods uses the values contained values in the result instance, and returns the result instance unaltered.

##### `consume` 

Consumes the success value of the result class. This method is not present in _VoidResult_.

- Argument(s)
  - A consumer accepting the success value of the result.
- Returns
  - The original result unaltered.
- Notes 
  - Not present in _VoidResult_.
  - For _OptionalResult_ this method consumes an _Optional_. Also provided in _OptionalResult_ are a method `consumeValue` which consumes the actual value, and is not run if _OptionalResult_ is empty.

##### `consumeError` 

Consumes the error value of the result class.

- Argument(s)
  - A consumer accepting the error value of the result.
- Returns
  - The original result unaltered.

##### `consumeEither` 

Consumes both the success value and the error value of the result class.

- Argument(s)
  - A consumer accepting the success value of the result.
  - A consumer accepting the error value of the result.
- Returns
  - The original result unaltered.
- Notes 
  - _OptionalResult_ also contains a three argument version of this method where the first argument is a consumer accepting the success value, the second argument is a runnable to run if the _OptionalResult_ is empty and the third is a consumer accepting the error value.
  - _BooleanResult_ also contains a three argument version of this method where the first argument is a runnable to run when the success value is true, the second argument is a runnable to run if the success value is false and the third is a consumer accepting the error value.
  - _VoidResult_ contains a variation of this method where the first argument is a runnable to run if the _VoidResult_ is in success state and the second argument is a consumer accepting the error value.
  
#### Run methods

The run methods are run if the prerequisite in the method name is met, and returns the result instance unaltered.

##### `runIfSuccess` 

The runnable is run if the result is in the success state.

- Argument(s)
  - A runnable which are run if the result is a success.
- Returns
  - The original result unaltered.
- Notes
  - For _OptionalResult_ this method is run both when it has a success value and when it is empty, as both translates to a success state. 
  - Also provided in _OptionalResult_ are a method runIfValue which is only run when there is an actual success value.
  - Also provided in _OptionalResult_ are a method runIfEmpty which is only run if the _OptionalResult_ is empty.
  - For _BooleanResult_ this method is run when the success value is both true or false, as both translates to a success state. 
  - Also provided in _BooleanResult_ are a method runIfTrue which is only run when the success value is true.
  - Also provided in _BooleanResult_ are a method runIfFalse which is only run when the success value is false.

##### `runIfError` 

The runnable is run if the result is in the error state.

- Argument(s)
  - A runnable which are run if the result is an error.
- Returns
  - The original result unaltered.

##### `runIfEither` 

A method which accepts runnables for all the states of the Result class.

- Argument(s)
  - A runnable which are run if the result is a success.
  - A runnable which are run if the result is an error.
- Returns
  - The original result unaltered.
- Notes
    - _OptionalResult_ also contains a three argument version of this method where the first argument is a runnable to run if has a success value, the second argument is a runnable to run if the _OptionalResult_ is empty and the third is a runnable to run if the result is an error.
    - _BooleanResult_ also contains a three argument version of this method where the first argument is a runnable to run when the success value is true, the second argument is a runnable to run if the success value is false and the third is a runnable to run if the result is an error.

##### `run` 

The runnable is run no matter what state the Result class has.

- Argument(s)
  - A runnable which are always run, both in the case of success state or error state.
- Returns
  - The original result unaltered.

##### `verify` 

A method which can verify the current success value of the Result class. If the verification fails, the returned result will contain the supplied error value.

- Argument(s)
  - A predicate which tests the success value of the result.
  - A supplier for the error value if the predicate fails.
- Returns
  - The original result unaltered if the predicate evaluates to true.
  - A new result containing the supplied error if the predicate evaluates to false.
  - The original result unaltered if it already contained an error.
- Notes
  - For _OptionalResult_ the predicate tests an Optional. Also provided in _OptionalResult_ are a method `verifyValue` which tests the actual value, and is not run if _OptionalResult_ is empty.

##### `merge` 

A method to use when you want to merge the result, no matter if success or error, into an actual non-result-wrapped value.

- Argument(s)
  - A function mapping from the success value to a new type `N`.
  - A function mapping from the error value to a new type `N`.
- Returns
  - The value mapped from either the success or error value.
- Notes 
  - _OptionalResult_ also contains a three argument version of this method where the first argument is a function mapping the success value, the second argument is a supplier to provide the value if the _OptionalResult_ is empty and the third is a function mapping the error value.
  - _BooleanResult_ also contains a three argument version of this method where the first argument is a supplier providing a value when the success value is true, the second argument is a supplier providing a value if the success value is false and the third is a function mapping the error value.
  - _VoidResult_ contains a variation of this method where the first argument is a supplier providing the value if the _VoidResult_ is in success state and the second is a function mapping the error value.
  

##### `orElse` 

Returns the success value of the result, or the provided argument value if the result is in error state.

- Argument(s)
  - A value to return if no success value is present.
- Returns
  - The success value of the result, otherwise the argument value.
- Notes:
  - Not present in _VoidResult_.
  - _OptionalResult_ has a variation of this method `valueOrElse` which returns the argument value if the _OptionalResult_ both is empty or contain an error.

##### `orElseGet` 

Returns the success value of the result, or the supplied value from the provided argument supplier if the result is in error state.

- Argument(s)
  - A supplier providing the value to return if no success value is present.
- Returns
  - The success value of the result, otherwise the value provided by the argument supplier.
- Notes
  - Not present in _VoidResult_
  - _OptionalResult_ has a variation of this method `valueOrElseGet` which returns the value provided by the argument supplier if the _OptionalResult_ both is empty or contain an error.

##### `orElseThrow`

Returns the success value of the result, or throws the supplied exception from the provided argument supplier if the result is in error state.

- Argument(s)
  - A supplier providing the exception to throw if no success value is present.
- Returns
  - The success value of the result.
  - Otherwise the exception provided by the argument supplier is thrown.
- Notes
  - In _VoidResult_ this is a void-method.
  - _OptionalResult_ has a variation of this method `valueOrElseThrow` which throws the supplied exception if the _OptionalResult_ both is empty or contain an error.

##### `toOptionalResult`

Returns an _OptionalResult_ containing either the success value or error value of the original result.

- No arguments
- Returns
  - An _OptionalResult_ containing either the success value or the error value from the original result.
- Notes
  - If the original result is a _VoidResult_, then the _OptionalResult_ will either be empty or contain the error value from the _VoidResult_.
  - Not present in _OptionalResult_.
  
##### `toVoidResult` 

Returns an _VoidResult_ either in success state or containing the error value of the original result.

  - No arguments
  - Returns
    - A _VoidResult_ in success state or containing the error value from the original result.
  - Notes
    - Not present in _VoidResult_
  
##### `toResult` 

A method present in _OptionalResult_ for creating a Result from the _OptionalResult_ success value.

  - Argument(s)
    - A supplier providing the error value to create a Result with if the _OptionalResult_ is empty.
  - Returns
    - A Result containing the success value of the _OptionalResult_, or an error if the _OptionalResult_ was empty or contained error.
  - Notes
    - Only present in _OptionalResult_.






