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

package io.nosqlbench.engine.api.activityimpl;

import io.nosqlbench.engine.api.activityapi.cyclelog.buffers.results.ResultReadable;
import io.nosqlbench.engine.api.activityapi.cyclelog.inputs.cyclelog.CanFilterResultValue;
import io.nosqlbench.engine.api.activityapi.output.Output;
import io.nosqlbench.engine.api.activityapi.output.OutputDispenser;

import java.util.function.Predicate;

public class FilteringOutputDispenser implements OutputDispenser {

    private final OutputDispenser outputDispenser;
    private final Predicate<ResultReadable> resultReadablePredicate;

    public FilteringOutputDispenser(OutputDispenser outputDispenser, Predicate<ResultReadable> resultReadablePredicate) {
        this.outputDispenser = outputDispenser;
        this.resultReadablePredicate = resultReadablePredicate;
    }

    @Override
    public Output getOutput(long slot) {
        Output output = outputDispenser.getOutput(slot);
        if (output instanceof CanFilterResultValue) {
            ((CanFilterResultValue)output).setFilter(resultReadablePredicate);
        } else {
            throw new RuntimeException("Unable to set result filterPredicate on output '" + output + ", filterPredicate '" + resultReadablePredicate + "'");
        }
        return output;
    }
}
