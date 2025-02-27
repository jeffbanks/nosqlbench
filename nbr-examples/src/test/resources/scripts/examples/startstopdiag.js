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

activitydef = {
    "alias" : "teststartstopdiag",
    "driver" : "diag",
    "cycles" : "0..1000000000",
    "threads" : "25",
    "interval" : "2000",
    "op" : "noop"
};

print('starting activity teststartstopdiag');
scenario.start(activitydef);

print('waiting 500 ms');

scenario.waitMillis(500);
print('waited, stopping activity teststartstopdiag');
scenario.stop(activitydef);

print('stopped activity teststartstopdiag');
