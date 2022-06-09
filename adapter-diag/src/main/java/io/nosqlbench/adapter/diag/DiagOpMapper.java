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

package io.nosqlbench.adapter.diag;

import io.nosqlbench.engine.api.activityimpl.OpDispenser;
import io.nosqlbench.engine.api.activityimpl.OpMapper;
import io.nosqlbench.engine.api.activityimpl.uniform.DriverSpaceCache;
import io.nosqlbench.engine.api.templating.ParsedOp;
import io.nosqlbench.nb.api.config.standard.NBConfigModel;
import io.nosqlbench.nb.api.config.standard.NBConfiguration;
import io.nosqlbench.nb.api.config.standard.NBReconfigurable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.LongFunction;

public class DiagOpMapper implements OpMapper<DiagOp>, NBReconfigurable {
    private final DriverSpaceCache<? extends DiagSpace> spaceCache;
    private final Map<String,DiagOpDispenser> dispensers = new LinkedHashMap<>();

    public DiagOpMapper(DriverSpaceCache<? extends DiagSpace> spaceCache) {
        this.spaceCache = spaceCache;
    }

    @Override
    public OpDispenser<? extends DiagOp> apply(ParsedOp cmd) {
        DiagOpDispenser dispenser = new DiagOpDispenser(cmd);
        LongFunction<String> spaceName = cmd.getAsFunctionOr("space", "default");
        LongFunction<DiagSpace> spacef = l -> spaceCache.get(spaceName.apply(l));
        dispensers.put(cmd.getName(),dispenser);
        return dispenser;
    }


    @Override
    public void applyReconfig(NBConfiguration recfg) {
        NBReconfigurable.applyMatching(recfg, dispensers.values());
    }

    @Override
    public NBConfigModel getReconfigModel() {
        return NBReconfigurable.collectModels(this.getClass(),new ArrayList<>(dispensers.values()));
    }
}
