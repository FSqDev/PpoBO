package com.example.test.ppobo;

import android.app.Application;
import android.content.Context;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
        mv = {1, 1, 15},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004J\b\u0010\u0006\u001a\u00020\u0007H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.¢\u0006\u0002\n\u0000¨\u0006\b"},
        d2 = {"Lcom/example/test/ppobo/ReminderApp;", "Landroid/app/Application;", "()V", "repository", "Lcom/example/test/ppobo/ReminderRepository;", "getRepository", "onCreate", "", "app_debug"}
)
public final class ReminderApp extends Application {
  private ReminderRepository repository;

  public void onCreate() {
    super.onCreate();
    this.repository = new ReminderRepository((Context)this);
  }

  @NotNull
  public final ReminderRepository getRepository() {
    ReminderRepository var10000 = this.repository;
    if (var10000 == null) {
      Intrinsics.throwUninitializedPropertyAccessException("repository");
    }

    return var10000;
  }
}
