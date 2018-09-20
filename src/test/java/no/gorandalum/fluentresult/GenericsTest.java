package no.gorandalum.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

public class GenericsTest {

    @Test
    public void result_map() {
        Result<Type, Type> result = Result.success(new SubType());
        Function<ParentType, SubType> function = p -> new SubType();
        Result<Type, Type> mapped = result.map(function);
    }

    @Test
    public void result_successToSuccess_flatMap() {
        Result<Type, Type> result = Result.success(new SubType());
        Function<ParentType, Result<SubType, Type>> function = p -> Result.success(new SubType());
        Result<Type, Type> mapped = result.flatMap(function);
    }

    @Test
    public void result_errorToSuccess_flatMap() {
        Result<Type, Type> result = Result.error(new SubType());
        Function<ParentType, Result<SubType, Type>> function = p -> Result.success(new SubType());
        Result<Type, Type> mapped = result.flatMap(function);
    }

    @Test
    public void result_successToError_flatMap() {
        Result<Type, Type> result = Result.success(new SubType());
        Function<ParentType, Result<SubType, Type>> function = p -> Result.error(new SubType());
        Result<Type, Type> mapped = result.flatMap(function);
    }

    @Test
    public void optionalResult_successToSuccess_map() {
        OptionalResult<Type, Type> result = OptionalResult.success(new SubType());
        Function<Optional<Type>, Optional<SubType>> function = p -> Optional.of(new SubType());
        OptionalResult<Type, Type> mapped = result.mapToOptional(function);
    }

    private static class ParentType {}
    private static class Type extends ParentType {}
    private static class SubType extends Type {}

}
