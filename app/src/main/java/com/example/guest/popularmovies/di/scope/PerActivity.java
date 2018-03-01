package com.example.guest.popularmovies.di.scope;

/**
 * Created by l1maginaire on 3/1/18.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {}