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

package io.nosqlbench.engine.core.script;

import com.codahale.metrics.Histogram;
import io.nosqlbench.api.engine.activityimpl.ActivityDef;
import io.nosqlbench.api.engine.metrics.ActivityMetrics;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class MetricsIntegrationTest {

    @Test
    public void testHistogramLogger() {
        ActivityDef ad = ActivityDef.parseActivityDef("alias=foo;driver=diag;op=noop");
        Histogram testhistogram = ActivityMetrics.histogram(ad, "testhistogram", 4);
        ActivityMetrics.addHistoLogger("testsession", ".*","testhisto.log","1s");
        testhistogram.update(400);
        testhistogram.getSnapshot();
        File logfile = new File("testhisto.log");
        assertThat(logfile).exists();
        assertThat(logfile.lastModified()).isGreaterThan(System.currentTimeMillis()-10000);

    }
}
