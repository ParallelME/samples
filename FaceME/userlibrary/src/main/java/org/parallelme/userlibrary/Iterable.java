/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary;

import org.parallelme.userlibrary.datatypes.UserData;
import org.parallelme.userlibrary.function.Foreach;

/**
 * Base interface for all iterable types existent on ParallelME. Generic iteration operations are
 * defined here.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public interface Iterable<E extends UserData> {
    /**
     * Applies an user function over all elements of this iterable.
     *
     * @param userFunction
     *            The user function that must be applied.
     */
    void foreach(Foreach<E> userFunction);
}
