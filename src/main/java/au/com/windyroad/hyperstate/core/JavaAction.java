package au.com.windyroad.hyperstate.core;

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

import au.com.windyroad.hyperstate.annotations.PresentationType;
import au.com.windyroad.hyperstate.core.entities.CreatedEntity;
import au.com.windyroad.hyperstate.core.entities.EntityWrapper;
import au.com.windyroad.hyperstate.core.entities.UpdatedEntity;

public class JavaAction<T> extends Action<T> {

    private Method method;
    private EntityWrapper<?> entity;
    private HttpMethod nature;

    protected JavaAction() {
    }

    public JavaAction(EntityWrapper<?> entity, Method method) {
        super(null, method.getName(), new JavaLink(entity),
                extractParameters(method));
        this.method = method;
        this.entity = entity;
        this.nature = determineMethodNature(method);
    }

    public static HttpMethod determineMethodNature(Method method) {
        Type type = method.getGenericReturnType();
        if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            Class<? extends Type> rawTypeClass = rawType.getClass();
            if (Class.class.isAssignableFrom(rawTypeClass)
                    && CompletableFuture.class
                            .isAssignableFrom((Class<?>) rawType)) {
                Type[] typeParams = parameterizedType.getActualTypeArguments();
                if (typeParams.length == 1) {
                    Type typeParam = typeParams[0];
                    if (Class.class.isAssignableFrom(typeParam.getClass())
                            && Void.class
                                    .isAssignableFrom((Class<?>) typeParam)) {
                        return HttpMethod.DELETE;
                    } else if (Class.class
                            .isAssignableFrom(typeParam.getClass())
                            && CreatedEntity.class
                                    .isAssignableFrom((Class<?>) typeParam)) {
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

    private static au.com.windyroad.hyperstate.core.Parameter[] extractParameters(
            Method method) {
        List<Parameter> params = Arrays.asList(method.getParameters());

        List<au.com.windyroad.hyperstate.core.Parameter> rval = new ArrayList<>();
        for (int i = 0; i < params.size(); ++i) {
            rval.add(new au.com.windyroad.hyperstate.core.Parameter(
                    params.get(i).getName()));
        }
        rval.add(new au.com.windyroad.hyperstate.core.Parameter("action",
                PresentationType.SUBMIT, method.getName()));
        return rval.toArray(new au.com.windyroad.hyperstate.core.Parameter[0]);
    }

    @Override
    public CompletableFuture<T> doInvoke(Resolver resolver,
            Map<String, Object> filteredParameters) {
        List<Object> args = new ArrayList<>(getParameters().size());
        for (au.com.windyroad.hyperstate.core.Parameter param : getParameters()) {
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
