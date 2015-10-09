/*
 * Copyright 2014 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.codewise.hyperion.histogram.hyperion;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import sun.misc.Unsafe;

public class UnsafeAccess {

    public static final Unsafe UNSAFE;

    static {
        Unsafe unsafe = null;
        try {
            final PrivilegedExceptionAction<Unsafe> action =
                    () ->
                    {
                        final Field f = Unsafe.class.getDeclaredField("theUnsafe");
                        f.setAccessible(true);

                        return (Unsafe) f.get(null);
                    };

            unsafe = AccessController.doPrivileged(action);
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }

        UNSAFE = unsafe;
    }
}
