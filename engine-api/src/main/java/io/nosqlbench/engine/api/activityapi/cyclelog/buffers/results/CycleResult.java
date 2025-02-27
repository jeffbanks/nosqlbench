/*
 * Copyright (c) 2022 nosqlbench
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.nosqlbench.engine.api.activityapi.cyclelog.buffers.results;



/**
 * A readable interface for (cycle, result) tuple types.
 */
public interface CycleResult extends Comparable<CycleResult>, CycleReadable, ResultReadable {

    /**
     * By default, allow cycle results to be ordered according to the cycle number.
     * @param o CycleResult to compare to
     * @return -1, 0, or 1, depending on ordering
     */
    default int compareTo( CycleResult o) {
        return Long.compare(getCycle(),o.getCycle());
    }

}
