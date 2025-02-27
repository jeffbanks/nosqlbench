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

package io.nosqlbench.driver.pulsar.ops;

/**
 * Base type of all Pulsar Operations including Producers and Consumers.
 */
public interface PulsarOp {

    /**
     * Execute the operation, invoke the timeTracker when the operation ended.
     * The timeTracker can be invoked in a separate thread, it is only used for metrics.
     * @param timeTracker
     */
    void run(Runnable timeTracker);
}
