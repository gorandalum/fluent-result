[![Build Status](https://travis-ci.com/gorandalum/fluent-result.svg?branch=master)](https://travis-ci.com/gorandalum/fluent-result)

# Fluent Result

A result library helping you get rid of exceptions, enabling a more fluent coding style.

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
    
### [Result](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html)

#### Static factory methods

[`success(T value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#success(T))<br/>
[`error(E value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#error(E))

#### Instance methods

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

[`verify(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Predicate<T> predicate,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<E> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#verify(java.util.function.Predicate,java.util.function.Supplier))<br/>

[`merge(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<T, N> valueFunction,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<E, N> errorFunction)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#merge(java.util.function.Function,java.util.function.Function))<br/>
[`orElse(T other)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#orElse(T))<br/>
[`orElseGet(Function<E, T> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#orElseGet(java.util.function.Function))<br/>
[`orElseThrow(Function<E, X> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#orElseThrow(java.util.function.Function))<br/>

[`toOptionalResult()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#toOptionalResult())<br/>
[`toVoidResult()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/Result.html#toVoidResult())
    
### [OptionalResult](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html)

#### Static factory methods

[`success(Optional<T> maybeValue)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#success(java.util.Optional))<br/>
[`success(T value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#success(T))<br/>
[`successNullable(T value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#successNullable(T))<br/>
[`empty()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#empty())<br/>
[`error(E value)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#error(E))

#### Instance methods

[`map(Function<Optional<T>, N> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#map(java.util.function.Function))<br/>
[`mapToOptional(Function<Optional<T>, Optional<N>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#mapToOptional(java.util.function.Function))<br/>
[`mapToBoolean(Function<<Optional<T>, Boolean> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#mapToBoolean(java.util.function.Function))<br/>
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
[`runIfEmpty(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runIfEmpty(java.lang.Runnable))<br/>
[`runIfError(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runIfError(java.lang.Runnable))<br/>
[`runEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable successRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable errorRunnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runEither(java.lang.Runnable,java.lang.Runnable))<br/>
[`runEither(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable valueRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable emptyRunnable,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Runnable errorRunnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#runEither(java.lang.Runnable,java.lang.Runnable))<br/>
[`run(Runnable runnable)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#run(java.lang.Runnable))<br/>

[`verify(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Predicate<Optional<T>> predicate,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<E> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#verify(java.util.function.Predicate,java.util.function.Supplier))<br/>
[`verifyValue(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Predicate<T> predicate,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<E> supplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#verifyValue(java.util.function.Predicate,java.util.function.Supplier))<br/>

[`merge(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<Optional<T>, N> successFunction,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<E, N> errorFunction)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#merge(java.util.function.Function,java.util.function.Function))<br/>
[`merge(`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<T, N> valueFunction,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Supplier<N> emptySupplier,`<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`Function<E, N> errorFunction)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#merge(java.util.function.Function,java.util.function.Supplier,java.util.function.Function))<br/>
[`orElse(Optional<T> other)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#orElse(java.util.Optional)(T))<br/>
[`valueOrElse(T other)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#valueOrElse(T))<br/>
[`orElseGet(Function<E, Optional<T>> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#orElseGet(java.util.function.Function))<br/>
[`valueOrElseGet(Supplier<T> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#valueOrElseGet(java.util.function.Supplier))<br/>
[`orElseThrow(Function<E, X> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#orElseThrow(java.util.function.Function))<br/>
[`valueOrElseThrow(Supplier<X> function)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#valueOrElseThrow(java.util.function.Supplier))<br/>

[`flatten(Supplier<E> errorSupplier)`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#flatten(java.util.function.Supplier))
[`toVoidResult()`](https://gorandalum.github.io/fluent-result/no/gorandalum/fluentresult/OptionalResult.html#toVoidResult())


