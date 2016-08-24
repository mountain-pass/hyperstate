package au.com.mountainpass.hyperstate.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpMethod;

import au.com.mountainpass.hyperstate.annotations.PresentationType;
import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.DeletedEntity;
import au.com.mountainpass.hyperstate.core.entities.Entity;
import au.com.mountainpass.hyperstate.core.entities.EntityWrapper;
import au.com.mountainpass.hyperstate.core.entities.UpdatedEntity;

public class JavaAction<T extends Entity> extends Action<T> {

    public static HttpMethod determineMethodNature(final Method method) {
        final Type type = method.getGenericReturnType();
        if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            final Type rawType = parameterizedType.getRawType();
            final Class<? extends Type> rawTypeClass = rawType.getClass();
            if (Class.class.isAssignableFrom(rawTypeClass)
                    && CompletableFuture.class
                            .isAssignableFrom((Class<?>) rawType)) {
                final Type[] typeParams = parameterizedType
                        .getActualTypeArguments();
                if (typeParams.length == 1) {
                    final Type typeParam = typeParams[0];
                    if (Class.class.isAssignableFrom(typeParam.getClass())
                            && DeletedEntity.class
                                    .isAssignableFrom((Class<?>) typeParam)) {
                        return HttpMethod.DELETE;
                    } else
                        if (Class.class.isAssignableFrom(typeParam.getClass())
                                && CreatedEntity.class.isAssignableFrom(
                                        (Class<?>) typeParam)) {
                        return HttpMethod.POST;
                    } else if (Class.class
                            .isAssignableFrom(typeParam.getClass())
                            && UpdatedEntity.class
                                    .isAssignableFrom((Class<?>) typeParam)) {
                        return HttpMethod.PUT;
                    } else if (ParameterizedType.class
                            .isAssignableFrom(typeParam.getClass())
                            && EntityWrapper.class.isAssignableFrom(
                                    (Class<?>) ((ParameterizedType) typeParam)
                                            .getRawType())) {
                        return HttpMethod.GET;
                    }
                }
            }
        }
        return null;
    }

    private static au.com.mountainpass.hyperstate.core.Parameter[] extractParameters(
            final Method method) {
        final List<Parameter> params = Arrays.asList(method.getParameters());

        final List<au.com.mountainpass.hyperstate.core.Parameter> rval = new ArrayList<>();
        for (int i = 0; i < params.size(); ++i) {
            // todo add type support here
            rval.add(new au.com.mountainpass.hyperstate.core.Parameter(
                    params.get(i).getName()));
        }
        rval.add(new au.com.mountainpass.hyperstate.core.Parameter("action",
                PresentationType.SUBMIT, method.getName()));
        return rval
                .toArray(new au.com.mountainpass.hyperstate.core.Parameter[0]);
    }

    private EntityWrapper<?> entity;

    private Method method;

    private HttpMethod nature;

    protected JavaAction() {
    }

    public JavaAction(final RepositoryResolver resolver,
            final EntityWrapper<?> entity, final Method method) {
        super(method.getName(), new JavaAddress(resolver, entity),
                extractParameters(method));
        this.method = method;
        this.entity = entity;
        this.nature = determineMethodNature(method);
    }

    @Override
    public CompletableFuture<T> doInvoke(
            final Map<String, Object> filteredParameters) {
        // TODO, this should use the resovler;
        final List<Object> args = new ArrayList<>(getParameters().size());
        for (final au.com.mountainpass.hyperstate.core.Parameter param : getParameters()) {
            if (!PresentationType.SUBMIT.equals(param.getType())) {
                args.add(filteredParameters.get(param.getIdentifier()));
            }
        }
        try {
            return (CompletableFuture<T>) method.invoke(entity, args.toArray());
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public HttpMethod getNature() {
        return nature;
    }

}
