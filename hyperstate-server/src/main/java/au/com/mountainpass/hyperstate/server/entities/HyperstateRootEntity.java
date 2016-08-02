package au.com.mountainpass.hyperstate.server.entities;

import java.util.concurrent.CompletableFuture;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.mountainpass.hyperstate.client.RepositoryResolver;
import au.com.mountainpass.hyperstate.core.NavigationalRelationship;
import au.com.mountainpass.hyperstate.core.entities.CreatedEntity;
import au.com.mountainpass.hyperstate.core.entities.VanillaEntity;
import au.com.mountainpass.hyperstate.server.HyperstateController;

public class HyperstateRootEntity extends VanillaEntity {

    private RepositoryResolver resolver;

    protected HyperstateRootEntity() {
    }

    public HyperstateRootEntity(RepositoryResolver resolver,
            final Class<? extends HyperstateController> controllerClass) {
        super(resolver,
                AnnotationUtils
                        .findAnnotation(controllerClass, RequestMapping.class)
                        .value()[0],
                controllerClass.getSimpleName(),
                HyperstateRootEntity.class.getSimpleName());
        this.resolver = resolver;
    }

    public CompletableFuture<CreatedEntity> create(Class<?> type, String path,
            String title, String natures) {
        final VanillaEntity accounts = new VanillaEntity(resolver,
                this.getId() + path, title, "Accounts");
        return resolver.getRepository().save(accounts)
                .thenApplyAsync(entity -> {
                    accounts.setRepository(resolver.getRepository());

                    this.add(
                            new NavigationalRelationship(accounts, "accounts"));
                    return new CreatedEntity(entity);
                });

    }
}
