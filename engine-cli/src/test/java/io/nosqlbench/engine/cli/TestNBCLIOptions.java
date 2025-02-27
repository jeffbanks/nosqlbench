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

package io.nosqlbench.engine.cli;

import io.nosqlbench.docsys.core.PathWalker;
import io.nosqlbench.api.content.NBIO;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TestNBCLIOptions {

    @Test
    public void shouldRecognizeActivities() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{"start", "foo=wan", "start", "bar=lan"});
        assertThat(opts.getCommands()).isNotNull();
        assertThat(opts.getCommands().size()).isEqualTo(2);
        assertThat(opts.getCommands().get(0).getParams()).containsEntry("foo","wan");
        assertThat(opts.getCommands().get(1).getParams()).containsEntry("bar","lan");
    }

    @Test
    public void shouldParseLongActivityForm() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{"start", "param1=param2", "param3=param4",
                                                          "--report-graphite-to", "woot", "--report-interval", "23"});
        assertThat(opts.getCommands().size()).isEqualTo(1);
        assertThat(opts.getCommands().get(0).getParams()).containsEntry("param1","param2");
        assertThat(opts.getCommands().get(0).getParams()).containsEntry("param3","param4");
        assertThat(opts.wantsReportGraphiteTo()).isEqualTo("woot");
        assertThat(opts.getReportInterval()).isEqualTo(23);
    }

    @Test
    public void shouldRecognizeShortVersion() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{"--version"});
        assertThat(opts.isWantsVersionShort()).isTrue();
        assertThat(opts.wantsVersionCoords()).isFalse();
    }

    @Test
    public void shouldRecognizeVersion() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{"--version-coords"});
        assertThat(opts.isWantsVersionShort()).isFalse();
        assertThat(opts.wantsVersionCoords()).isTrue();
    }

    @Test
    public void shouldRecognizeScripts() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{"script", "ascriptaone", "script", "ascriptatwo"});
        assertThat(opts.getCommands()).isNotNull();
        assertThat(opts.getCommands().size()).isEqualTo(2);
        assertThat(opts.getCommands().get(0).getCmdType()).isEqualTo(Cmd.CmdType.script);
        assertThat(opts.getCommands().get(0).getArg("script_path")).isEqualTo("ascriptaone");
        assertThat(opts.getCommands().get(1).getCmdType()).isEqualTo(Cmd.CmdType.script);
        assertThat(opts.getCommands().get(1).getArg("script_path")).isEqualTo("ascriptatwo");
    }

    @Test
    public void shouldRecognizeWantsActivityTypes() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{"--list-activity-types"});
        assertThat(opts.wantsActivityTypes()).isTrue();
        opts = new NBCLIOptions(new String[]{"--version"});
        assertThat(opts.wantsActivityTypes()).isFalse();
        opts = new NBCLIOptions(new String[]{"--list-drivers"});
        assertThat(opts.wantsActivityTypes()).isTrue();

    }

    @Test
    public void shouldRecognizeWantsBasicHelp() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{"--help"});
        assertThat(opts.wantsBasicHelp()).isTrue();
        opts = new NBCLIOptions(new String[]{"--version"});
        assertThat(opts.wantsTopicalHelp()).isFalse();
    }

    @Test
    public void shouldRecognizeWantsActivityHelp() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{"--help", "foo"});
        assertThat(opts.wantsTopicalHelp()).isTrue();
        assertThat(opts.wantsTopicalHelpFor()).isEqualTo("foo");
        opts = new NBCLIOptions(new String[]{"--version"});
        assertThat(opts.wantsTopicalHelp()).isFalse();
    }

    @Test
    public void shouldErrorSanelyWhenNoMatch() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new NBCLIOptions(new String[]{"unrecognizable command"}));
    }

    @Test
    public void testShouldRecognizeScriptParams() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{"script", "ascript", "param1=value1"});
        assertThat(opts.getCommands().size()).isEqualTo(1);
        Cmd cmd = opts.getCommands().get(0);
        assertThat(cmd.getParams().size()).isEqualTo(2);
        assertThat(cmd.getParams()).containsKey("param1");
        assertThat(cmd.getParams().get("param1")).isEqualTo("value1");
    }

    @Test
    public void testShouldErrorSanelyWhenScriptNameSkipped() {
        assertThatExceptionOfType(InvalidParameterException.class)
                .isThrownBy(() -> new NBCLIOptions(new String[]{"script", "param1=value1"}));
    }

    @Test
    public void testShouldErrorForMissingScriptName() {
        assertThatExceptionOfType(InvalidParameterException.class)
                .isThrownBy(() -> new NBCLIOptions(new String[]{"script"}));
    }

    @Test
    public void shouldRecognizeStartActivityCmd() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{ "start", "driver=woot" });
        List<Cmd> cmds = opts.getCommands();
        assertThat(cmds).hasSize(1);
        assertThat(cmds.get(0).getCmdType()).isEqualTo(Cmd.CmdType.start);

    }

    @Test
    public void shouldRecognizeRunActivityCmd() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{ "run", "driver=runwoot" });
        List<Cmd> cmds = opts.getCommands();
        assertThat(cmds).hasSize(1);
        assertThat(cmds.get(0).getCmdType()).isEqualTo(Cmd.CmdType.run);

    }

    @Test
    public void shouldRecognizeStopActivityCmd() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{ "stop", "woah" });
        List<Cmd> cmds = opts.getCommands();
        assertThat(cmds).hasSize(1);
        assertThat(cmds.get(0).getCmdType()).isEqualTo(Cmd.CmdType.stop);
        assertThat(cmds.get(0).getArg("alias_name")).isEqualTo("woah");

    }

    @Test
    public void shouldThrowErrorForInvalidStopActivity() {
        assertThatExceptionOfType(InvalidParameterException.class)
                .isThrownBy(() -> new NBCLIOptions(new String[]{ "stop", "woah=woah" }));
    }

    @Test
    public void shouldRecognizeAwaitActivityCmd() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{ "await", "awaitme" });
        List<Cmd> cmds = opts.getCommands();
        assertThat(cmds.get(0).getCmdType()).isEqualTo(Cmd.CmdType.await);
        assertThat(cmds.get(0).getArg("alias_name")).isEqualTo("awaitme");

    }

    @Test
    public void shouldThrowErrorForInvalidAwaitActivity() {
        assertThatExceptionOfType(InvalidParameterException.class)
                .isThrownBy(() -> new NBCLIOptions(new String[]{ "await", "awaitme=notvalid" }));
    }

    @Test
    public void shouldRecognizewaitMillisCmd() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{ "waitmillis", "23234" });
        List<Cmd> cmds = opts.getCommands();
        assertThat(cmds.get(0).getCmdType()).isEqualTo(Cmd.CmdType.waitMillis);
        assertThat(cmds.get(0).getArg("millis_to_wait")).isEqualTo("23234");

    }

    @Test
    public void listWorkloads() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{ "--list-workloads"});
        assertThat(opts.wantsWorkloadsList()).isTrue();
    }

    @Test
    public void listScenarios() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{ "--list-scenarios"});
        assertThat(opts.wantsScenariosList()).isTrue();
    }

    @Test
    public void listScripts() {
        NBCLIOptions opts = new NBCLIOptions(new String[]{ "--list-scripts"});
        assertThat(opts.wantsListScripts()).isTrue();
    }

    @Test
    public void clTest() {
        String dir= "./";
        URL resource = getClass().getClassLoader().getResource(dir);
        assertThat(resource).isNotNull();
        Path basePath = NBIO.getFirstLocalPath(dir);
        List<Path> yamlPathList = PathWalker.findAll(basePath).stream().filter(f -> f.toString().endsWith(".yaml")).collect(Collectors.toList());
        assertThat(yamlPathList).isNotEmpty();
    }
}
