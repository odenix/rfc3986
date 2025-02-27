/*
 * Copyright (C) 2024 Hideki Ikeda
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
package org.czeal.rfc3986;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PortNormalizerTest
{
    @Test
    void normalize()
    {
        assertThat(new PortNormalizer().normalize(80, "http")).isEqualTo(-1);
        assertThat(new PortNormalizer().normalize(80, "custom")).isEqualTo(80);
        assertThat(new PortNormalizer().normalize(443, "https")).isEqualTo(-1);
        assertThat(new PortNormalizer().normalize(443, "custom")).isEqualTo(443);
    }
}
