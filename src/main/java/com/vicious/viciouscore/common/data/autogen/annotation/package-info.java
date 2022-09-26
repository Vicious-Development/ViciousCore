package com.vicious.viciouscore.common.data.autogen.annotation;

/**
 * SyncableValue Fields labelled with these annotations will be automatically processed by SyncAutomator.
 *
 * The purpose of this is to provide users with an easier time reading code and reducing the amount of mistakes made by explicitly stating what should do what.
 *
 * Annotation Persist: effectively does the same as calling compound.add(SyncableValue).
 * Annotation Exposed: adds an ExposableSyncableValue which stores capability exposure information and adds the annotated SyncableValue to the compound generated.
 * Annotation Obscured: does the same as @Persist, but also sets sendRemote and readRemote to false. This is intended for server end information only.
 * Annotation ReadOnly: does the same as @Persist, but also sets sendRemote to true and readRemote to false. This allows clients to see server information but not edit it.
 * Annotation Editable: does the same as @Persist, but also sets sendRemote and readRemote to true. This is intended for client-server interaction.
 */