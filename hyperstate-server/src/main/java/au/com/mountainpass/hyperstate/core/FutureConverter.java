package au.com.mountainpass.hyperstate.core;

import java.util.concurrent.CompletableFuture;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class FutureConverter {

  public static <T> CompletableFuture<T> convert(final ListenableFuture<T> listenableFuture) {
    // create an instance of CompletableFuture
    final CompletableFuture<T> completable = new CompletableFuture<T>() {
      @Override
      public boolean cancel(final boolean mayInterruptIfRunning) {
        // propagate cancel to the listenable future
        final boolean result = listenableFuture.cancel(mayInterruptIfRunning);
        super.cancel(mayInterruptIfRunning);
        return result;
      }
    };

    // add callback
    listenableFuture.addCallback(new ListenableFutureCallback<T>() {
      @Override
      public void onFailure(final Throwable t) {
        completable.completeExceptionally(t);
      }

      @Override
      public void onSuccess(final T result) {
        completable.complete(result);
      }
    });
    return completable;
  }

}
