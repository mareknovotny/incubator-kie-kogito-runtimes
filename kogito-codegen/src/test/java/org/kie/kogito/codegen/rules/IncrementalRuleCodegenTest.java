/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.codegen.rules;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.kie.api.io.ResourceType;
import org.kie.kogito.codegen.GeneratedFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncrementalRuleCodegenTest {

    @Test
    public void generateSingleFile() {
        IncrementalRuleCodegen incrementalRuleCodegen =
                IncrementalRuleCodegen.ofFiles(
                        Collections.singleton(
                                new File("src/test/resources/org/kie/kogito/codegen/rules/pkg1/file1.drl")),
                        ResourceType.DRL);
        incrementalRuleCodegen.setPackageName("com.acme");

        List<GeneratedFile> generatedFiles = incrementalRuleCodegen.withHotReloadMode().generate();
        assertRules(2, 1, generatedFiles.size());
    }

    @Test
    public void generateSinglePackage() {
        IncrementalRuleCodegen incrementalRuleCodegen =
                IncrementalRuleCodegen.ofFiles(
                        Arrays.asList(new File("src/test/resources/org/kie/kogito/codegen/rules/pkg1").listFiles()),
                        ResourceType.DRL);
        incrementalRuleCodegen.setPackageName("com.acme");

        List<GeneratedFile> generatedFiles = incrementalRuleCodegen.withHotReloadMode().generate();
        assertRules(4, 1, generatedFiles.size());
    }

    @Test
    public void generateDirectoryRecursively() {
        IncrementalRuleCodegen incrementalRuleCodegen =
                IncrementalRuleCodegen.ofPath(
                        Paths.get("src/test/resources/org/kie/kogito/codegen/rules"),
                        ResourceType.DRL);
        incrementalRuleCodegen.setPackageName("com.acme");

        List<GeneratedFile> generatedFiles = incrementalRuleCodegen.withHotReloadMode().generate();
        assertRules(7, 3, 1, generatedFiles.size());
    }

    @Test
    public void generateSingleDtable() {
        IncrementalRuleCodegen incrementalRuleCodegen =
                IncrementalRuleCodegen.ofFiles(
                        Collections.singleton(
                                new File("src/test/resources/org/drools/simple/candrink/CanDrink.xls")));
        incrementalRuleCodegen.setPackageName("com.acme");

        List<GeneratedFile> generatedFiles = incrementalRuleCodegen.withHotReloadMode().generate();
        assertRules(2, 1, generatedFiles.size());
    }

    @Test
    public void generateSingleUnit() {
        IncrementalRuleCodegen incrementalRuleCodegen =
                IncrementalRuleCodegen.ofPath(
                        Paths.get("src/test/resources/org/kie/kogito/codegen/rules/myunit"),
                        ResourceType.DRL);
        incrementalRuleCodegen.setPackageName("com.acme");

        List<GeneratedFile> generatedFiles = incrementalRuleCodegen.withHotReloadMode().generate();
        assertRules(1, 1, 1, generatedFiles.size());
    }

    private static void assertRules(int expectedRules, int expectedPackages, int expectedUnits, int actualGeneratedFiles) {
        assertEquals(expectedRules +
                             expectedPackages * 2 + /* package descriptor for rules + package metadata */
                             expectedUnits * 3 +/* ruleUnit + ruleUnit instance + unit model */
                             1 /* rule unit register */, actualGeneratedFiles);
    }

    private static void assertRules(int expectedRules, int expectedPackages, int actualGeneratedFiles) {
        assertEquals(expectedRules +
                             expectedPackages * 2 /* package descriptor for rules + package metadata */
                             , actualGeneratedFiles);
    }
}
