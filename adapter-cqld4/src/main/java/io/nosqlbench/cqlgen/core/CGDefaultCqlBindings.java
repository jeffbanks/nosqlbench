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

package io.nosqlbench.cqlgen.core;

import io.nosqlbench.cqlgen.model.CqlColumnBase;
import io.nosqlbench.cqlgen.binders.Binding;
import io.nosqlbench.cqlgen.api.BindingsLibrary;
import io.nosqlbench.engine.api.activityconfig.StatementsLoader;
import io.nosqlbench.engine.api.activityconfig.yaml.StmtsDocList;
import io.nosqlbench.api.content.Content;
import io.nosqlbench.api.content.NBIO;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public class CGDefaultCqlBindings implements BindingsLibrary {

    private final Map<String, String> bindings;
    public final static String DEFAULT_CFG_DIR = "cqlgen";
    public final static String DEFAULT_BINDINGS_FILE = "bindings-cqlgen.yaml";

    public CGDefaultCqlBindings() {
        String yamlContent = NBIO.all()
            .name(DEFAULT_BINDINGS_FILE)
            .first()
            .map(Content::asString)
            .or(() -> loadLocal(DEFAULT_BINDINGS_FILE))
            .orElseThrow(() -> new RuntimeException("Unable to load " + DEFAULT_BINDINGS_FILE + ", from local dir or internally as cqlgen/" + DEFAULT_BINDINGS_FILE));
        StmtsDocList stmtsDocs = StatementsLoader.loadString(yamlContent, Map.of());
        this.bindings = stmtsDocs.getDocBindings();
    }

    private Optional<String> loadLocal(String path) {
        try {
            String resourceName = DEFAULT_CFG_DIR + File.separator + path;
            InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName);
            byte[] bytes = stream.readAllBytes();
            return Optional.of(new String(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Binding> resolveBindingsFor(CqlColumnBase def) {
        String typedef = def.getTrimmedTypedef();
        String recipe = bindings.get(def.getTrimmedTypedef());
        Binding optionalBinding = new Binding(typedef, recipe);
        return Optional.ofNullable(optionalBinding);
    }
}
