package edu.gatech.cs2340.thericks.utils;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * Created by Cameron on 11/16/2017.
 * Allows us to create Predicates that are also Serializable in order
 * to pass Predicates through Intents and Bundles
 */
public interface SerializablePredicate<T> extends Serializable, Predicate<T> {
}
