[![Build Status](https://travis-ci.com/gorandalum/fluent-result.svg?branch=master)](https://travis-ci.com/gorandalum/fluent-result)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/no.gorandalum/fluent-result.svg)](https://mvnrepository.com/artifact/no.gorandalum/fluent-result)

# Fluent Result

A Java result library helping you get rid of exceptions, enabling a more fluent coding style.

- [Motivation](#motivation)
- [Usage](#usage)
  - [Creating a Result](#creating-a-result)
  - [Chaining](#chaining)
  - [Additional Result Classes](#additional-result-classes)
  - [Verifying the Value](#verifying-the-value)
  - [Extracting the Value](#extracting-the-value)
- [API](#api)
  - [Result](#result)
    - [Static Factory Methods](#static-factory-methods)
    - [Instance Methods](#instance-methods)
  - [OptionalResult](#optionalresult)
    - [Static Factory Methods](#static-factory-methods-1)
    - [Instance Methods](#instance-methods-1)
  - [BooleanResult](#booleanresult)
    - [Static Factory Methods](#static-factory-methods-2)
    - [Instance Methods](#instance-methods-2)
  - [OptionalResult](#voidresult)
    - [Static Factory Methods](#static-factory-methods-3)
    - [Instance Methods](#instance-methods-3)

## Motivation

Programming with exceptions can be both tedious and error-prone. Checked exceptions gives much boilerplate code, while unchecked exceptions can be the source of errors if they are let loose in the system without sufficient handling. 
 
Also exceptions should not be used for control-flow, but in the heat of the moment it is often faster to just throw an IllegalArgumentException than encapsulating the error in the returned class. This result library has done the encapsulation of the error for you, making your control flow more fluent and exception-free.

The introduction of Optional was a great step forward for Java, but as a class it does not make it any easier to return and handle error situations. The Result classes from this library is inspired from the Optional-class providing much of the same functionality, enabling utilization of chaining and lambdas for a more fluent style of programming.
 
## Usage

### Creating a Result

Make methods that can give an error return a `Result<T, E>` where T is the type of the return value when the result is in success state, and E is the type of error value when the result is in error state.

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

You may also use the static factory method `handle` which runs a given _Callable_. If it runs successfully the return value of the _Callable_ will be the success value of the _Result_. If an exception is thrown it will be the error value of the _Result_. _Callable_ is used as parameter as it supports checked exceptions.

```java
Result<Customer, Exception> customerResult = Result.handle(
        () -> service.getCustomer(id));
```

An exception mapper may also be provided to the `handle` method for mapping the exception if thrown.
```java
Result<Customer, String> customerResult = Result.handle(
        () -> service.getCustomer(id), 
        Exception::getMessage);
```


### Chaining

However the full benefit of using the Fluent Result library is more visible when using chaining and utilizing other methods that return _Result_ values.

Some other methods to use while chaining:
```java
public Result<Account, String> findAccount(List<Account> accounts, String accountId) {...}

public void logAccountInfo(Account account) {...}

public void logError(String customerId, String accountId, String errorMsg) {...}
```

Now the above methods can be called by chaining the calls.

```java
public Result<BigDecimal, String> getBalance(String customerId, String accountId) {
    return  Result.handle(() -> service.getCustomer(id)
        .mapError(Exception::getMessage)
        .map(Customer::getAccounts)
        .flatMap(accounts -> findAccount(accounts, accountId))
        .consume(this::logAccountInfo)
        .map(Account::getBalance)
        .consumeError(errMsg -> logError(customerId, accountId, errMsg));
}
```

In the above chain the methods `map`, `flatMap` and `consume` are invoked only if the _Result_ is in success state and contains a success value, and not if an error were returned somewhere in the chain, giving a _Result_ in error state. The methods `mapError` and `consumeError` is only invoked if error state.

### Additional Result Classes

Some cases of Result value types frequently happen, like `Result<Optional<T>, E>`, `Result<Boolean, E>` and `Result<Void, E>`. For these frequent result types the additional Result classes _OptionalResult_, _BooleanResult_ and _VoidResult_ are provided.

These classes provide some additional methods relevant for their type, and remove methods not relevant for their type. They create a better facade for some of the recurring patterns happening when using the regular _Result_.

Comparing using `Result<Optional<T>, E>` and `OptionalResult<T, E>`, first with `Result<Optional<Customer>, E>`:
```java
public Result<Optional<Integer>, String> getAge(String customerId) {
    return getCustomer(customerId) // Returns Result<Optional<Customer>, String>
        .consume(maybeCustomer -> maybeCustomer.ifPresentOrElse(
                customer -> LOGGER.info("Customer + " customer.getName() + " found"),
                () -> LOGGER.warn("Customer not found")
        ))
        .map(maybeCustomer -> maybeCustomer.map(Customer::getAge));
}
```
By using `OptionalResult<Customer, E>` the chain is more readable when methods like `consumeValue`, `runIfEmpty` and `mapValue` may be used:
```java
public OptionalResult<Integer, String> getAge(String customerId) {
    return getCustomer(customerId) // Returns OptionalResult<Customer, String>
        .consumeValue(customer -> LOGGER.info("Customer + " customer.getName() + " found"))
        .runIfEmpty(() -> LOGGER.warn("Customer not found"))
        .mapValue(Customer::getAge);
}
```

Comparing using `Result<Boolean, E>` and `BooleanResult<E>`, first with `Result<Boolean, E>`:
```java
public boolean isOldEnough(String customerId) {
    return getCustomer(customerId) // Returns Result<Customer, String>
        .map(customer -> customer.getAge() >= 18) // Returns Result<Boolean, String>
        .runIfSuccess(oldEnough -> {
            if (oldEnough) {
                LOGGER.info("Customer is old enough");
            } else {
                LOGGER.warn("Customer is underage");  
            }
        })
        .runIfError(errMsg -> LOGGER.warn("Error getting customer: " + errMsg))
        .orElse(false);
}
```
By using `BooleanResult<E>` the chain is more readable when methods like `runIfTrue`, `runIfFalse` and `orElseFalse` may be used:
```java
public boolean isOldEnough(String customerId) {
    return getCustomer(customerId) // Returns Result<Customer, String>
        .mapToBoolean(customer -> customer.getAge() >= 18) // Returns BooleanResult<String>
        .runIfTrue(() -> LOGGER.info("Customer is old enough"))
        .runIfFalse(() -> LOGGER.warn("Customer is underage"))
        .runIfError(() -> LOGGER.warn("Error getting customer"))
        .orElseFalse();
}
```

_OptionalResult_ and _BooleanResult_ also have three argument versions of the `consumeEither`, `runEither` and `fold` methods.

The above example, modified to `runEither`:
```java
public boolean isOldEnough(String customerId) {
    return getCustomer(customerId) // Returns Result<Customer, String>
        .mapToBoolean(customer -> customer.getAge() >= 18) // Returns BooleanResult<String>
        .runEither(
                () -> LOGGER.info("Customer is old enough"),
                () -> LOGGER.warn("Customer is underage"),
                () -> LOGGER.warn("Error getting customer"))
        .orElseFalse();
}
```

The additional _Result_ classes also have static factory methods relevant for their value type, examples include `OptionalResult.successNullable(T value)`, `OptionalResult.empty()`, `BooleanResult.successTrue()` and `VoidResult.success()`.

Creating an _OptionalResult_: 
```java
public OptionalResult<Customer, String> getCustomer(String id) {
    try {
        Customer customer = service.getCustomer(id); // May be null
        return OptionalResult.successNullable(customer);
    } catch (RuntimeException ex) {
        return OptionalResult.error(ex.getMessage());
    }
}
```

All _Result_ classes have methods for mapping and flat mapping to the other _Result_ classes. Example of mapping from a _Result_ to a _BooleanResult_ by using the method `mapToBoolean`:

```java
public BooleanResult<String> isOldEnough(String customerId) {
    return getCustomer(customerId)
        .mapToBoolean(customer -> customer.getAge() >= 18) // Returns BooleanResult<String>
        .runIfFalse(() -> LOGGER.warn("Customer is underage"));
}
```

### Verifying the Value

The method `verify` can be used for verifying the value of the _Result_, and if the verification fails the returned _Result_ will contain an error value.

There are two methods for verifying the result. The first method takes a single function mapping the success value to a _VoidResult_. The other method takes two arguments, a predicate taking in the success value, and an error supplier providing the value if the predicate evaluates to false.

Example of verifying the age of a customer by using a method which returns _VoidResult_. If the customer is under 18 then the returned _Result_ will contain the error message from the _VoidResult_.

```java
public Result<Customer, String> getGrownUpCustomer(String customerId) {
    return getCustomer(customerId) // Returns Result<Customer, String>
        .verify(this::isCustomerGrownUp);
}

private VoidResult<String> isCustomerGrownUp(Customer customer) {
    if (customer.getAge() >= 18) {
        return VoidResult.success();
    }    
    return VoidResult.error("Customer is not a grown up");
}
```

Sometimes you want to verify something without the overhead of creating a method returning a _VoidResult_, then you may use a predicate and error supplier instead.

Example of verifying the age of a customer. If the customer is under 18 then the returned _Result_ will contain the error message provided by the error supplier.

```java
public Result<Customer, String> getGrownUpCustomer(String customerId) {
    return getCustomer(customerId) // Returns Result<Customer, String>
        .verify(
                customer -> customer.getAge() >= 18,
                () -> "Customer is not a grown up");
}
```

For _OptionalResult_ you may also verify the actual value if it is present. If the _OptionalResult_ was already empty it will remain empty. Verifying the value also supports both providing a function mapping to _VoidResult_, or using a predicate and error supplier.

```java
public Result<Customer, String> getGrownUpCustomer(String customerId) {
    return getCustomer(customerId) // Returns OptionalResult<Customer, String>
        .verifyValue(
                customer -> customer.getAge() >= 18,
                () -> "Customer is not a grown up");
}
```

### Extracting the value

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

If you want to extract the value from the Result-object without consuming, the methods `orElse`, `orElseGet` and `orElseThrow` known from _Optional_ may be used. Also provided are a method `fold` which can be used for mapping both the success value and the error value to a single return value.

Example of `fold`:
```java
public Status getCustomerStatus() {
    return getCustomer(id) // Returns Result<Customer, String>
        .fold(
                customer -> Status.CUSTOMER_EXISTS,
                error -> Status.CUSTOMER_FETCH_ERROR);
}
```

There are also three argument versions of the `fold` method on _OptionalResult_ and _BooleanResult_. Example with _OptionalResult_:
```java
public Status getCustomerStatus() {
    return getCustomer(id) // Returns OptionalResult<Customer, String>
        .fold(
                customer -> Status.CUSTOMER_EXISTS,
                () -> Status.CUSTOMER_NOT_FOUND,
                error -> Status.CUSTOMER_FETCH_ERROR);
}
```

Example with _BooleanResult_:
```java
public Status getCustomerStatus() {
    return getCustomer(id) // Returns Result<Customer, String>
            .mapToBoolean(customer -> customer.getAge() >= 18) // Returns Booleanesult<String>
            .fold(
                      () -> Status.CUSTOMER_APPROVED,
                      () -> Status.CUSTOMER_UNDERAGE,
                      error -> Status.CUSTOMER_FETCH_ERROR);
}
```


## API
    
### [Result](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html)

#### Static Factory Methods

[`success(T value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#success(T))<br/>
[`error(E value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#error(E))

[`handle(Callable<T> callable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#handle(java.util.concurrent.Callable))
[`handle(Callable<T> callable, Function<Exception, E> exceptionMapper)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#handle(java.util.concurrent.Callable,java.util.function.Function)))

#### Instance Methods

[`map(Function<T, N> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#map(java.util.function.Function))<br/>
[`mapToOptional(Function<T, Optional<N>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#mapToOptional(java.util.function.Function))<br/>
[`mapToBoolean(Function<T, Boolean> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#mapToBoolean(java.util.function.Function))<br/>
[`mapError(Function<E, N> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#mapError(java.util.function.Function))<br/>

[`flatMap(Function<T, Result<N, E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#flatMap(java.util.function.Function))<br/>
[`flatMapToOptionalResult(Function<T, OptionalResult<N, E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#flatMapToOptionalResult(java.util.function.Function))<br/>
[`flatMapToBooleanResult(Function<T, BooleanResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#flatMapToBooleanResult(java.util.function.Function))<br/>
[`flatMapToVoidResult(Function<T, VoidResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#flatMapToVoidResult(java.util.function.Function))<br/>

[`consume(Consumer<T> consumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#consume(java.util.function.Consumer))<br/>
[`consumeError(Consumer<E> consumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#consumeError(java.util.function.Consumer))<br/>
[`consumeEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<T> valueConsumer,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<E> errorConsumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#consumeEither(java.util.function.Consumer,java.util.function.Consumer))<br/>

[`runIfSuccess(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#runIfSuccess(java.lang.Runnable))<br/>
[`runIfError(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#runIfError(java.lang.Runnable))<br/>
[`runEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable successRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable errorRunnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#runEither(java.lang.Runnable,java.lang.Runnable))<br/>
[`run(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#run(java.lang.Runnable))<br/>

[`verify(Function<T, VoidResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#verify(java.util.function.Function))<br/>
[`verify(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Predicate<T> predicate,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<E> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#verify(java.util.function.Predicate,java.util.function.Supplier))<br/>

[`fold(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<T, N> valueFunction,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<E, N> errorFunction)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#fold(java.util.function.Function,java.util.function.Function))<br/>
[`orElse(T other)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#orElse(T))<br/>
[`orElseGet(Function<E, T> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#orElseGet(java.util.function.Function))<br/>
[`orElseThrow(Function<E, X> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#orElseThrow(java.util.function.Function))<br/>

[`toOptionalResult()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#toOptionalResult())<br/>
[`toVoidResult()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#toVoidResult())
    
### [OptionalResult](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html)

#### Static Factory Methods

[`success(Optional<T> maybeValue)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#success(java.util.Optional))<br/>
[`success(T value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#success(T))<br/>
[`successNullable(T value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#successNullable(T))<br/>
[`empty()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#empty())<br/>
[`error(E value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#error(E))

[`handle(Callable<T> callable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#handle(java.util.concurrent.Callable))
[`handle(Callable<T> callable, Function<Exception, E> exceptionMapper)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#handle(java.util.concurrent.Callable,java.util.function.Function)))

#### Instance Methods

[`map(Function<Optional<T>, N> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#map(java.util.function.Function))<br/>
[`mapToOptional(Function<Optional<T>, Optional<N>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#mapToOptional(java.util.function.Function))<br/>
[`mapToBoolean(Function<Optional<T>, Boolean> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#mapToBoolean(java.util.function.Function))<br/>
[`mapError(Function<E, N> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#mapError(java.util.function.Function))<br/>
[`mapValue(Function<T, N> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#mapValue(java.util.function.Function))<br/>
[`mapValueToOptional(Function<T, Optional<N>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#mapValueToOptional(java.util.function.Function))<br/>

[`flatMap(Function<Optional<T>, Result<N, E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#flatMap(java.util.function.Function))<br/>
[`flatMapToOptionalResult(Function<Optional<T>, OptionalResult<N, E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#flatMapToOptionalResult(java.util.function.Function))<br/>
[`flatMapToBooleanResult(Function<Optional<T>, BooleanResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#flatMapToBooleanResult(java.util.function.Function))<br/>
[`flatMapToVoidResult(Function<Optional<T>, VoidResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#flatMapToVoidResult(java.util.function.Function))<br/>

[`flatMapValueWithResult(Function<T, Result<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#flatMapValueWithResult(java.util.function.Function))<br/>
[`flatMapValueWithOptionalResult(Function<T, OptionalResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#flatMapValueWithOptionalResult(java.util.function.Function))<br/>
[`flatMapValueWithBooleanResult(Function<T, BooleanResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#flatMapValueWithBooleanResult(java.util.function.Function))<br/>

[`consume(Consumer<Optional<T>> consumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#consume(java.util.function.Consumer))<br/>
[`consumeValue(Consumer<T> consumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#consumeValue(java.util.function.Consumer))<br/>
[`consumeError(Consumer<E> consumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#consumeError(java.util.function.Consumer))<br/>
[`consumeEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<Optional<T>> successConsumer,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<E> errorConsumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#consumeEither(java.util.function.Consumer,java.util.function.Consumer))<br/>
[`consumeEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<T> valueConsumer,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable emptyRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<E> errorConsumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#consumeEither(java.util.function.Consumer,java.lang.Runnable,java.util.function.Consumer))<br/>

[`runIfSuccess(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runIfSuccess(java.lang.Runnable))<br/>
[`runIfValue(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runIfValue(java.lang.Runnable))<br/>
[`runIfNoValue(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runIfNoValue(java.lang.Runnable))<br/>
[`runIfEmpty(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runIfEmpty(java.lang.Runnable))<br/>
[`runIfError(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runIfError(java.lang.Runnable))<br/>
[`runEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable successRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable errorRunnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runEither(java.lang.Runnable,java.lang.Runnable))<br/>
[`runEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable valueRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable emptyRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable errorRunnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runEither(java.lang.Runnable,java.lang.Runnable,java.lang.Runnable))<br/>
[`run(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#run(java.lang.Runnable))<br/>

[`verify(Function<Optional<T>, VoidResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#verify(java.util.function.Function))<br/>
[`verify(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Predicate<Optional<T>> predicate,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<E> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#verify(java.util.function.Predicate,java.util.function.Supplier))<br/>
[`verifyValue(Function<T, VoidResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#verifyValue(java.util.function.Function))<br/>
[`verifyValue(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Predicate<T> predicate,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<E> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#verifyValue(java.util.function.Predicate,java.util.function.Supplier))<br/>

[`fold(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<Optional<T>, N> successFunction,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<E, N> errorFunction)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#fold(java.util.function.Function,java.util.function.Function))<br/>
[`fold(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<T, N> valueFunction,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<N> emptySupplier,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<E, N> errorFunction)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#fold(java.util.function.Function,java.util.function.Supplier,java.util.function.Function))<br/>
[`orElse(Optional<T> other)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#orElse(java.util.Optional))<br/>
[`valueOrElse(T other)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#valueOrElse(T))<br/>
[`orElseGet(Function<E, Optional<T>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#orElseGet(java.util.function.Function))<br/>
[`valueOrElseGet(Supplier<T> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#valueOrElseGet(java.util.function.Supplier))<br/>
[`orElseThrow(Function<E, X> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#orElseThrow(java.util.function.Function))<br/>
[`valueOrElseThrow(Supplier<X> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#valueOrElseThrow(java.util.function.Supplier))<br/>

[`toResult(Supplier<E> errorSupplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#toResult(java.util.function.Supplier))<br/>
[`toVoidResult()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#toVoidResult())
    
### [BooleanResult](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html)

#### Static Factory Methods

[`success(boolean value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#success(boolean))<br/>
[`successTrue()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#successTrue())<br/>
[`successFalse()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#successFalse())<br/>
[`error(E value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#error(E))

[`handle(Callable<T> callable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#handle(java.util.concurrent.Callable))
[`handle(Callable<T> callable, Function<Exception, E> exceptionMapper)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#handle(java.util.concurrent.Callable,java.util.function.Function)))

#### Instance Methods

[`map(Function<Boolean, N> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#map(java.util.function.Function))<br/>
[`mapToOptional(Function<Boolean, Optional<N>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#mapToOptional(java.util.function.Function))<br/>
[`mapToBoolean(Function<Boolean, Boolean> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#mapToBoolean(java.util.function.Function))<br/>
[`mapError(Function<E, N> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#mapError(java.util.function.Function))<br/>

[`flatMap(Function<Boolean, Result<N, E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#flatMap(java.util.function.Function))<br/>
[`flatMapToOptionalResult(Function<Boolean, OptionalResult<N, E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#flatMapToOptionalResult(java.util.function.Function))<br/>
[`flatMapToBooleanResult(Function<Boolean, BooleanResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#flatMapToBooleanResult(java.util.function.Function))<br/>
[`flatMapToVoidResult(Function<Boolean, VoidResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#flatMapToVoidResult(java.util.function.Function))<br/>

[`consume(Consumer<Boolean> consumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#consume(java.util.function.Consumer))<br/>
[`consumeError(Consumer<E> consumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#consumeError(java.util.function.Consumer))<br/>
[`consumeEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<Boolean> successConsumer,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<E> errorConsumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#consumeEither(java.util.function.Consumer,java.util.function.Consumer))<br/>
[`consumeEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable trueRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable falseRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<E> errorConsumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#consumeEither(java.lang.Runnable,java.lang.Runnable,java.util.function.Consumer))<br/>

[`runIfSuccess(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#runIfSuccess(java.lang.Runnable))<br/>
[`runIfTrue(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#runIfTrue(java.lang.Runnable))<br/>
[`runIfFalse(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#runIfFalse(java.lang.Runnable))<br/>
[`runIfError(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#runIfError(java.lang.Runnable))<br/>
[`runEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable successRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable errorRunnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#runEither(java.lang.Runnable,java.lang.Runnable))<br/>
[`runEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable trueRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable falseRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable errorRunnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#runEither(java.lang.Runnable,java.lang.Runnable,java.lang.Runnable))<br/>
[`run(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#run(java.lang.Runnable))<br/>

[`verify(Function<Boolean, VoidResult<E>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#verify(java.util.function.Function))<br/>
[`verify(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Predicate<Boolean> predicate,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<E> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#verify(java.util.function.Predicate,java.util.function.Supplier))<br/>

[`fold(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<Boolean, N> successFunction,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<E, N> errorFunction)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#fold(java.util.function.Function,java.util.function.Function))<br/>
[`fold(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<N> trueSupplier,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<N> falseSupplier,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<E, N> errorFunction)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#fold(java.util.function.Supplier,java.util.function.Supplier,java.util.function.Function))<br/>
[`orElse(Boolean other)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#orElse(java.lang.Boolean))<br/>
[`orElseTrue()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#orElseTrue())<br/>
[`orElseFalse()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#orElseFalse())<br/>
[`orElseGet(Function<E, Boolean> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#orElseGet(java.util.function.Function))<br/>
[`orElseThrow(Function<E, X> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#orElseThrow(java.util.function.Function))<br/>

[`toOptionalResult()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#toOptionalResult())<br/>
[`toVoidResult()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/BooleanResult.html#toVoidResult())

### [VoidResult](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html)

#### Static Factory Methods

[`success()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#success())<br/>
[`error(E value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#error(E))

[`handle(CheckedRunnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#handle(no.gorandalum.fluentresult.CheckedRunnable))
[`handle(CheckedRunnable runnable, Function<Exception, E> exceptionMapper)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#handle(no.gorandalum.fluentresult.CheckedRunnable,java.util.function.Function))

#### Instance Methods

[`mapError(Function<E, N> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#mapError(java.util.function.Function))<br/>

[`replace(Supplier<N> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#replace(java.util.function.Supplier))<br/>
[`replaceWithOptional(Supplier<Optional<N>> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#replaceWithOptional(java.util.function.Supplier))<br/>
[`replaceWithBoolean(Supplier<Boolean> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#replaceWithBoolean(java.util.function.Supplier))<br/>

[`flatReplace(Supplier<Result<N, E>> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#flatReplace(java.util.function.Supplier))<br/>
[`flatReplaceToOptionalResult(Supplier<OptionalResult<N, E>> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#flatReplaceToOptionalResult(java.util.function.Supplier))<br/>
[`flatReplaceToBooleanResult(Supplier<BooleanResult<E>> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#flatReplaceToBooleanResult(java.util.function.Supplier))<br/>
[`flatReplaceToVoidResult(Supplier<VoidResult<E>> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#flatReplaceToVoidResult(java.util.function.Supplier))<br/>

[`consumeError(Consumer<E> consumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#consumeError(java.util.function.Consumer))<br/>
[`consumeEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable successRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Consumer<E> errorConsumer)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#consumeEither(java.lang.Runnable,java.util.function.Consumer))<br/>

[`runIfSuccess(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#runIfSuccess(java.lang.Runnable))<br/>
[`runIfError(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#runIfError(java.lang.Runnable))<br/>
[`runEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable successRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable errorRunnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#runEither(java.lang.Runnable,java.lang.Runnable))<br/>
[`run(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#run(java.lang.Runnable))<br/>

[`fold(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<N> valueSupplier,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<E, N> errorFunction)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#fold(java.util.function.Supplier,java.util.function.Function))<br/>
[`orElseThrow(Function<E, X> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#orElseThrow(java.util.function.Function))<br/>

[`toOptionalResult()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/VoidResult.html#toOptionalResult())<br/>
    